package com.example.client.controllers;

import com.example.client.model.CatalogItem;
import com.example.client.services.CatalogService;
import com.example.client.services.RestClientService;
import com.example.client.services.SiteService;
import com.example.client.site_engine.SiteBuilder;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
public class MainController {
    @Autowired
    SiteService siteService;
    @Autowired
    CatalogService catalogService;
    @Autowired
    RestClientService clientService;

    private final Integer catalogListLimit = 15;
    private final Integer top5ListLimit = 5;

    @GetMapping("/static")
    //@ResponseBody
    public RedirectView index(@PathVariable(required = false, value = "start") Long start, Model model) {
        return new RedirectView("/top5/0");
    }

    @GetMapping("/top5/{start}")
    //@ResponseBody
    public  String top10(@PathVariable(value = "start") Long start,Model model){
        var map = catalogService.getAggregateMap(start * 5,5);
        var siteList = siteService.getSitesList();
        model.addAttribute("data_caller","/data_top5");
        model.addAttribute("caller_title", "Top 5 news");
        model.addAttribute("caller", "/top5");
        model.addAttribute("current_index", start);
        model.addAttribute("site_list", siteList);
        model.addAttribute("site_map", map);
        return "/top5_block";
    }
    @GetMapping("/catalog/{site_name}/{start}")
    //@ResponseBody
    public  String catalog(@PathVariable(value = "site_name") String siteName
            ,@PathVariable(required = false, value = "start") Long start
            ,Model model){
        System.out.println(siteName);
        var map = catalogService.getCatalogMap(siteName, start * catalogListLimit, catalogListLimit);
        var siteList = siteService.getSitesList();
        model.addAttribute("data_caller","/data_catalog/" + siteName );
        model.addAttribute("caller_title", siteName.replace("Site", ""));
        model.addAttribute("caller", "/catalog/" + siteName );
        model.addAttribute("max_result_count", catalogService.maxResultCount(siteName) / catalogListLimit);
        model.addAttribute("site_list", siteList);
        model.addAttribute("site_map", map);
        return "/catalog_view_block";
    }

    @PostMapping("/search/{start}")
    //@ResponseBody
    public String search(@RequestParam String pattern
            ,@PathVariable(required = false, value = "start") Long start
            ,Model model){
        var map = catalogService.search(pattern, start, catalogListLimit);
        var siteList = siteService.getSitesList();
        model.addAttribute("data_caller","/data_search");
        model.addAttribute("caller_title", "Search result");
        model.addAttribute("caller", "/search");
        model.addAttribute("current_index", start);
        model.addAttribute("site_list", siteList);
        model.addAttribute("site_map", map);
        return "/search_view";
    }
    @PostMapping("/article")
    //@ResponseBody
    public String article(@RequestParam String siteName, @RequestParam String url, Model model){
        System.out.println(url + " : " + siteName);
        Document doc = clientService.loadPage(url);
        SiteBuilder site = SiteBuilder.Sites.SITEMAP.getSiteByName(siteName);
        site.getParserByName(SiteBuilder.ParserTypes.ARTICLE).parseDoc(doc);
        var res =site.getParserByName(SiteBuilder.ParserTypes.ARTICLE).getArticleMap();
        model.addAttribute("site_data", res);
        return "/article_view";
    }

}
