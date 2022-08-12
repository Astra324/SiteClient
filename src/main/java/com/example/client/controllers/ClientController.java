package com.example.client.controllers;

import com.example.client.model.ClientDto;
import com.example.client.services.CatalogService;
import com.example.client.services.RestClientService;
import com.example.client.services.SiteService;
import com.example.client.site_engine.SiteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


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
    RestClientService clientService;

    @Value("${appHost}")
    private  String appHost;

    private final Integer catalogListLimit = 15;
    private final Integer top5ListLimit = 5;


    @GetMapping("/app-data-top5/{start}")
    //@ResponseBody
    public  ClientDto dataTop10(@PathVariable(value = "start") Integer start,Model model){
        var map = catalogService.getAggregateMap(start * top5ListLimit,top5ListLimit);
        var siteList = siteService.getSitesList();
        ClientDto respond = new ClientDto();
        respond.setAppHost(appHost);
        respond.setCurrentIndex(start);
        respond.setCaller("/app-data-top5");
        respond.setDataSource("/app-data-top5");
        respond.setMaxResultCount(catalogService.getMaxCount() / (top5ListLimit + siteList.size()));
        respond.setSiteList(siteList);
        respond.setDataMap(map);

        return respond;
    }

    @GetMapping("/app-data-catalog/{site_name}/{start}")
    //@ResponseBody
    public ClientDto dataCatalog(@PathVariable(value = "site_name") String siteName
            , @PathVariable(required = false, value = "start") Integer start
            , Model model){
        System.out.println(siteName);
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
        respond.setDataMap(map);
        return respond;
    }
    @GetMapping("/app-data-search/{pattern}/{start}")
    //@ResponseBody
    public ClientDto search(@PathVariable(value = "pattern") String pattern
            ,@PathVariable(value = "start") Integer start
            ,Model model){
        System.out.println("search pattern : " + pattern);
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
        respond.setDataMap(map);

        return respond;
    }


}
