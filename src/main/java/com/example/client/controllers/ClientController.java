package com.example.client.controllers;

import com.example.client.model.CatalogItem;
import com.example.client.model.ClientDto;
import com.example.client.model.Favorites;
import com.example.client.model.User;
import com.example.client.services.CatalogService;
import com.example.client.services.RestClientService;
import com.example.client.services.SiteService;
import com.example.client.services.UserService;
import com.example.client.site_engine.SiteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


//@CrossOrigin(
//        // Access-Control-Allow-Origin
//        origins = { "*" },
//
//        // Alternative to origins that supports more flexible originpatterns.
//        // Please, see CorsConfiguration.setAllowedOriginPatterns(List)for details.
//        // originPatterns = { "" },
//
//        // Access-Control-Allow-Credentials
//        allowCredentials = "false",
//
//        // Access-Control-Allow-Headers
//        allowedHeaders = { "*" },
//
//        // Access-Control-Expose-Headers
//        exposedHeaders = { "*" },
//
//        // Access-Control-Max-Age
//        maxAge = 60 * 30,
//
//        // Access-Control-Allow-Methods
//        methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT}
//)
@RestController
public class ClientController {
    @Autowired
    SiteService siteService;
    @Autowired
    CatalogService catalogService;
    @Autowired
    UserService userService;


    @Value("${appHost}")
    private  String appHost;

    private final Integer catalogListLimit = 15;
    private final Integer top5ListLimit = 5;


    @GetMapping("/app-data-top5/{start}/{username}")
    //@ResponseBody
    public  ClientDto dataTop10(
            @PathVariable(value = "start") Integer start
            ,@PathVariable(value = "username", required = false) String userName
            , Model model){

        User currentUser = userService.getLoggedUserByName(userName).orElseThrow(NullPointerException::new);

        var siteList = siteService.getSitesListByUserMap(currentUser.getSites());
        var map = catalogService.getAggregateMap(siteList, start * top5ListLimit,top5ListLimit);

        ClientDto respond = new ClientDto();

        respond.setClientSiteMap(Stream.of(currentUser.getSites()).map(Byte::intValue).collect(Collectors.toList()));
        respond.setAppHost(appHost);
        respond.setCurrentIndex(start);
        respond.setCaller("/app-data-top5");
        respond.setDataSource("/app-data-top5");
        respond.setMaxResultCount(catalogService.getMaxCount() / (top5ListLimit + siteList.size()));
        respond.setSiteList(siteList);
        respond.setFavorites(currentUser.getFavorites());
        respond.setDataMap(map);

        return respond;
    }

    @GetMapping("/app-data-catalog/{site_name}/{start}/{username}")
    //@ResponseBody
    public ClientDto dataCatalog(@PathVariable(value = "site_name") String siteName
            , @PathVariable(required = false, value = "start") Integer start
            , @PathVariable(required = false, value = "username") String userName
            , Model model){

        User currentUser = userService.getLoggedUserByName(userName).orElseThrow(NullPointerException::new);

        var map = catalogService.getCatalogMap(siteName, start * catalogListLimit, catalogListLimit);
        var siteList = siteService.getSitesList().stream().filter((e)->e.getName().equals(siteName)).collect(Collectors.toList());

        ClientDto respond = new ClientDto();
        respond.setAppHost(appHost);
        respond.setCaller("/app-data-catalog/" + siteName);
        respond.setDataSource("/app-data-catalog/" + siteName);
        respond.setCurrentIndex(start);
        Long maxResult =  Integer.toUnsignedLong(catalogService.maxResultCount(siteName) / catalogListLimit);
        respond.setMaxResultCount(maxResult);
        respond.setSiteList(siteList);
        respond.setFavorites(currentUser.getFavorites());
        respond.setDataMap(map);
        return respond;
    }
    @GetMapping("/app-data-search/{pattern}/{start}/{username}")
    //@ResponseBody
    public ClientDto search(@PathVariable(value = "pattern") String pattern
            ,@PathVariable(value = "start") Integer start
            ,@PathVariable(required = false, value = "username") String userName
            ,Model model
    , HttpServletRequest request){

        User currentUser = userService.getLoggedUserByName(userName).orElseThrow(NullPointerException::new);

        var map = catalogService.search(pattern, (long) (start * catalogListLimit), catalogListLimit);
        List<SiteBuilder> siteList = new ArrayList<>();
        siteList.add(siteService.getSiteByName("Search"));

        ClientDto respond = new ClientDto();
        respond.setAppHost(appHost);
        respond.setCaller("/app-data-search/");
        respond.setDataSource("/app-data-search/" + pattern);
        respond.setCurrentIndex(start);
        respond.setMaxResultCount((long) (catalogService.getMaxSearchResult() / catalogListLimit));
        respond.setSiteList(siteList);
        respond.setFavorites(currentUser.getFavorites());
        respond.setDataMap(map);

        return respond;
    }
    @GetMapping("/app-user-add-favorites/{username}/{articleId}")
    @ResponseBody
    public String addFavorites(HttpServletResponse response,
                                          @PathVariable(name = "articleId") Long articleId
            , @PathVariable(name = "username") String username){
        response.setHeader("result", "1");

        User currentUser = userService.getLoggedUserByName(username).orElseThrow(NullPointerException::new);

            Favorites newFavorites = new Favorites();
            newFavorites.setUserId(currentUser.getId());
            newFavorites.setItemId(articleId);
            newFavorites.setDateAdded(new Date(System.currentTimeMillis()));
            newFavorites.setTimestamp(System.currentTimeMillis());
            userService.saveFavorites(newFavorites);
            userService.updateLoggedUser(currentUser);

        System.out.println("favorites added article : " + username + " article id " + articleId);
        return  "";
    }
}
