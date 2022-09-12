package com.example.client.controllers;

import com.example.client.exceptions.UserNotFoundException;
import com.example.client.model.CatalogItem;
import com.example.client.model.ClientDto;
import com.example.client.model.User;
import com.example.client.services.CatalogService;
import com.example.client.services.RestClientService;
import com.example.client.services.SiteService;
import com.example.client.services.UserService;
import com.example.client.site_engine.SiteBuilder;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

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
    @Autowired
    CopyOnWriteArrayList getLoggedUsers;


    @Value("${appHost}")
    private  String appHost;

    private final Integer catalogListLimit = 15;
    private final Integer top5ListLimit = 5;
    private List<User> users = new ArrayList<>();




    @GetMapping("/")
    //@ResponseBody
    public String appStart(HttpServletRequest request, HttpServletResponse response, Model model, HttpSession session) {
        System.out.println("App host : " + appHost);
        if(request.getCookies() != null){
            System.out.println("cookie : " + readServletCookie(request, "username"));
        }
        ClientDto respond = new ClientDto();

        User loginUser = getOrRegisterUser(request, session, response);

        var siteList = siteService.getSitesListByUserMap(loginUser.getSites());
        var map = catalogService.getAggregateMap(siteList,0,top5ListLimit);

        respond.setSiteList(siteList);
        model.addAttribute("user_name", loginUser.getUsername());
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

    private User getOrRegisterUser(HttpServletRequest request, HttpSession session, HttpServletResponse response){

        if(readServletCookie(request, "username").isPresent()) {
            String resultUserName = readServletCookie(request, "username").get();
            response.addCookie(createCookie(resultUserName));
            //System.out.println(resultUserName);
            User loginUser = Optional.ofNullable(userService.getByUserName(resultUserName)).orElseThrow(()->
                    new UserNotFoundException("Error - user : " + resultUserName + " not found in database!"));
            userService.addLoggedUser(loginUser);
            return loginUser;
        }else{
            String newUserName =  session.getId() + "-" + System.currentTimeMillis() + "@anonymous";
            response.addCookie(createCookie(newUserName));
            String newUserPassword = session.getId();
            User newUser = Optional.ofNullable(userService.registerNewUser(newUserName, newUserPassword)).orElseThrow(()->
                    new UserNotFoundException("Error - user : " + newUserName + " cannot be added in database!"));
            userService.addLoggedUser(newUser);
            System.out.println("New user registered : " + newUser.toString());
            return newUser;
        }

    }
    private Cookie createCookie(String userName){
        Cookie newCookie = new Cookie("username", userName);
        newCookie.setPath("/");
        newCookie.setMaxAge(86400 * 30);
        newCookie.setSecure(false);
        newCookie.setHttpOnly(false);
        return newCookie;
    }

    private Optional<String> readServletCookie(HttpServletRequest request, String name){
        if(request.getCookies() == null) return Optional.empty();
        return Arrays.stream(request.getCookies())
                .filter(cookie->name.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findAny();
    }

}
