package com.example.client.services;

import com.example.client.model.CatalogItem;
import com.example.client.site_engine.SiteBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class SiteService {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    ExecutorService fixedThreadPool;
    @Autowired
    ExecutorService singleThreadExecutor;

    @Value("${dbHost}")
    private  String dbHost;

    public List<SiteBuilder> getSitesList(){
        return SiteBuilder.Sites.SITEMAP.map().values().stream().filter(SiteBuilder::isAggregated).toList();
    }
    public List<SiteBuilder> getSitesListByUserMap(Byte[] siteMask){
        var sites = getSitesList();
        List<SiteBuilder> resultList = new ArrayList<>();
        for (Byte bi : siteMask){
            if(bi.intValue() >= 0){
                resultList.add(sites.get(bi.intValue()));
            }
        }

        return resultList;
    }

    public SiteBuilder getSiteByName(String siteName){
        return SiteBuilder.Sites.SITEMAP.getSiteByName(siteName);
    }

    public SiteBuilder loadSiteCatalogContent(String siteName) {
        SiteBuilder site = SiteBuilder.Sites.SITEMAP.getSiteByName(siteName);
        SiteBuilder resultList = null;
        Future<SiteBuilder> result = null;
        if (site != null) {
            Callable<SiteBuilder> task = () -> {
                Document doc = load(site.getBaseUrl());
                site.getParserByName(SiteBuilder.ParserTypes.AGGREGATE).parseDoc(doc);
                return site;
            };
            result = singleThreadExecutor.submit(task);
        }

        try {
            resultList = result.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(resultList).orElseThrow(()->new NullPointerException("Load site content fail content map is null: " + siteName));
    }

    private Document load(String uri) {
        String result = restTemplate.getForObject(uri, String.class);
        Document document = Jsoup.parse(result);
        return document;
    }
    public void callDbUpdate(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                restTemplate.headForHeaders(dbHost);
            }
        };
        singleThreadExecutor.execute(runnable);

    }

    public List<SiteBuilder> getTop10(){
        var sites = SiteBuilder.Sites.SITEMAP.map().values().stream().filter(SiteBuilder::isAggregated).collect(Collectors.toList());
        getAggregateResult(sites);
        return new ArrayList<>(sites);
    }
    public List<CatalogItem> getAggregatedCatalogList(){
        var sites = SiteBuilder.Sites.SITEMAP.map().values().stream().filter(SiteBuilder::isAggregated).collect(Collectors.toList());
        getAggregateResult(sites);
        return sites.stream().flatMap((e)->e.getTagMaps().stream().map((map)->{
            map.setNewRegisteredItem(true);
            return new CatalogItem(map);
        })).collect(Collectors.toList());
    }
    public <T extends List<SiteBuilder>> T getAggregateResult(T sites) {
        ArrayList<SiteBuilder> resultSites = null;
        if (sites.size() > 0) {
            ArrayList<Callable<SiteBuilder>> tasks = new ArrayList<>();
            for (Object site : sites) {
                tasks.add(() -> {
                    SiteBuilder s = (SiteBuilder) site;
                    Document doc = load(s.getBaseUrl());
                    s.getParserByName(SiteBuilder.ParserTypes.AGGREGATE).parseDoc(doc);
                    return s;
                });
            }
            try {
                resultSites = (ArrayList<SiteBuilder>) fixedThreadPool.invokeAll(tasks).stream().map((e) -> {
                    try {
                        if (e.isDone()) {
                            return e.get();
                        }
                    } catch (InterruptedException | ExecutionException ex) {
                        ex.printStackTrace();
                    }
                    return null;
                }).filter(Objects::nonNull).collect(Collectors.toList());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                //executorService.shutdown();
                System.out.println("End task");
            }
        }
        return (T) Optional.ofNullable(resultSites).orElseThrow(() -> new NullPointerException("Aggregate array not loaded result is null: "));
    }
}
