package com.example.client.controllers;

import com.example.client.model.CatalogItem;
import com.example.client.model.ClientDto;
import com.example.client.model.User;
import com.example.client.repo.UsersRepository;
import com.example.client.services.CatalogService;
import com.example.client.services.RestClientService;
import com.example.client.services.SiteService;
import com.example.client.services.UserService;
import com.example.client.site_engine.SiteBuilder;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class AppController {
    @Autowired
    SiteService siteService;
    @Autowired
    CatalogService catalogService;
    @Autowired
    RestClientService clientService;

    @Autowired
    UserService userService;

    @Value("${appHost}")
    private  String appHost;

    private final Integer catalogListLimit = 15;
    private final Integer top5ListLimit = 5;
    private List<User> users = new ArrayList<>();




    @GetMapping("/")
    //@ResponseBody
    public String appStart(HttpServletRequest request, HttpServletResponse response, Model model, HttpSession session) {
        var map = catalogService.getAggregateMap(0,top5ListLimit);
        var siteList = siteService.getSitesList();
        System.out.println("App host : " + appHost);

        ClientDto respond = new ClientDto();

        if(readServletCookie(request, "username").isPresent()) {
            System.out.println("session id : " + session.getId());
            System.out.println("request cookie : " + userService.getByUserName(readServletCookie(request, "username").get()).getUsername());
            respond.setUserName(readServletCookie(request, "username").get());
            response.addCookie(createCookie(readServletCookie(request, "username").get()));

        }else{
            String newUserName =  session.getId() + "-" +System.currentTimeMillis() + "@anonymous";
            response.addCookie(createCookie(newUserName));
            String newUserPassword = session.getId();
            User newUser = userService.registerNewUser(newUserName, newUserPassword, new Integer[]{0});
            users.add(newUser);
            respond.setUserName(newUserName);
            System.out.println("New new set cookie : " + newUser.getUsername() + " id : " + newUser.getId());
        }


        respond.setCaller("/");
        respond.setDataSource("/app-data-top5");
        respond.setMaxResultCount(catalogService.getMaxCount() / (top5ListLimit + siteList.size()));
        respond.setCurrentIndex(0);
        respond.setSiteList(siteList);
        respond.setDataMap(map);

        model.addAttribute("user_name", respond.getUserName());
        model.addAttribute("data_source", "/app-data-top5");
        model.addAttribute("root_link", "/");
        model.addAttribute("app_title", "Top 5 news");
        model.addAttribute("data", respond);
        return "/app-start";
    }

    @GetMapping("/articles/{id}")
    //@ResponseBody
    public String articles(@PathVariable(value = "id") Long id, Model model){
        System.out.println(" article id : " + id);
        CatalogItem item = catalogService.getArticleById(id);
        Document doc = clientService.loadPage(item.getHref());
        SiteBuilder site = SiteBuilder.Sites.SITEMAP.getSiteByName(item.getSiteName());
        site.getParserByName(SiteBuilder.ParserTypes.ARTICLE).parseDoc(doc);
        var res =site.getParserByName(SiteBuilder.ParserTypes.ARTICLE).getArticleMap();
        model.addAttribute("site_data", res);
        return "/article_view";
    }

    private Cookie createCookie(String userName){
        Cookie newCookie = new Cookie("username", userName);
        newCookie.setPath("localhost:8080/");
        newCookie.setSecure(true);
        newCookie.setMaxAge(31536000);
        newCookie.setHttpOnly(false);
        return newCookie;
    }

    public Optional<String> readServletCookie(HttpServletRequest request, String name){
        return Arrays.stream(request.getCookies())
                .filter(cookie->name.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findAny();
    }

}
