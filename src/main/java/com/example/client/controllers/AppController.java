package com.example.client.controllers;

import com.example.client.model.CatalogItem;
import com.example.client.model.ClientDto;
import com.example.client.services.CatalogService;
import com.example.client.services.RestClientService;
import com.example.client.services.SiteService;
import com.example.client.site_engine.SiteBuilder;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class AppController {
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




    @GetMapping("/")
    //@ResponseBody
    public String appStart( Model model) {
        var map = catalogService.getAggregateMap(0,top5ListLimit);
        var siteList = siteService.getSitesList();
        System.out.println("App host : " + appHost);

        ClientDto respond = new ClientDto();
        respond.setCaller("/");
        respond.setDataSource("/app-data-top5");
        respond.setMaxResultCount(catalogService.getMaxCount() / (top5ListLimit + siteList.size()));
        respond.setCurrentIndex(0);
        respond.setSiteList(siteList);
        respond.setDataMap(map);

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

}
