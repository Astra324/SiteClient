package com.example.client.services;

import com.example.client.model.CatalogItem;
import com.example.client.repo.CatalogEntityManager;
import com.example.client.repo.CatalogRepository;
import com.example.client.site_engine.SiteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Service
public class CatalogService {
    @Autowired
    CatalogEntityManager catalogQuery;
    @Autowired
    CatalogRepository repository;
    @Autowired
    ConcurrentHashMap catalogMap;
    @Autowired
    SiteService siteService;
    @Autowired
    ExecutorService fixedThreadPool;



    private Integer maxSearchResult;

    public Integer getMaxSearchResult() {
        return maxSearchResult;
    }

    public LinkedHashMap<SiteBuilder, List<CatalogItem>> getAggregateMap(long startIndex, int limit){
        var resultMap = new LinkedHashMap<SiteBuilder, List<CatalogItem>>();
        var sites = siteService.getSitesList();
        updateCatalogMap();
        List<CatalogItem> data = (ArrayList<CatalogItem>) catalogMap.values().stream().collect(Collectors.toList());
        for(SiteBuilder site : sites){
            var catalogList = data.stream()
                    .filter((e)->e.getSiteName().equals(site.getName()))
                    .sorted((e1,e2)->e2.getTimestamp().compareTo(e1.getTimestamp()))
                    .skip(startIndex).limit(limit)
                    .collect(Collectors.toList());
            if(catalogList.size() > 0) {
                resultMap.put(site, catalogList);
            }else {
                List<CatalogItem> preloadList = preloadCatalog(site, startIndex, limit);
                if(preloadList != null) resultMap.put(site, preloadList);
                System.out.println("catalog : " + site.getName() + " : is " +   catalogList.size() );
            }
        }
        return resultMap;
    }
    public LinkedHashMap<SiteBuilder, List<CatalogItem>> getCatalogMap(String siteName, long startIndex, int limit){
        updateCatalogMap();
        preloadCatalog(siteService.getSiteByName(siteName), startIndex, limit);

        var resultMap = new LinkedHashMap<SiteBuilder, List<CatalogItem>>();
        var sites = siteService.getSitesList().stream().filter((s)->s.getName().equals(siteName)).collect(Collectors.toList());

        List<CatalogItem> data = (ArrayList<CatalogItem>) catalogMap.values().stream().collect(Collectors.toList());

        for(SiteBuilder site : sites){
            var catalogList = data.stream()
                    .filter((e)->e.getSiteName().equals(site.getName()))
                    .sorted((e1,e2)->e2.getTimestamp().compareTo(e1.getTimestamp()))
                    .skip(startIndex)
                    .limit(limit)
                    .collect(Collectors.toList());
            if(catalogList.size() > 0) {
                resultMap.put(site, catalogList);
            }
//            else {
//                List<CatalogItem> preloadList = preloadCatalog(site, startIndex, limit);
//                if(preloadList != null) resultMap.put(site, preloadList);
//                System.out.println("catalog : " + site.getName() + " : is " +   catalogList.size() );
//            }
        }
        return resultMap;
    }
    private <T> List<T> preloadCatalog(SiteBuilder site, long startIndex, int limit){
        List<CatalogItem> data = (ArrayList<CatalogItem>) catalogMap.values().stream().collect(Collectors.toList());
        Long lastIndex = data.stream().filter((e)->e.getSiteName().equals(site.getName())).map(CatalogItem::getId).reduce(Long::min).orElse(0L);
        List<CatalogItem> preloadedCatalogList = catalogQuery.preloadCatalog(site.getName(), lastIndex);
        List<CatalogItem> resultList = null;
        if(preloadedCatalogList.size() > 0){
            for (CatalogItem item : preloadedCatalogList) {
                catalogMap.putIfAbsent(item.getHref(), item);
            }
            data = (ArrayList<CatalogItem>) catalogMap.values().stream().collect(Collectors.toList());
            resultList = data.stream()
                    .filter((e)->e.getSiteName().equals(site.getName()))
                    .sorted((e1,e2)->e2.getTimestamp().compareTo(e1.getTimestamp()))
                    .skip(startIndex)
                    .limit(limit)
                    .collect(Collectors.toList());
        }
        return (List<T>) resultList;
    }
    public LinkedHashMap<SiteBuilder, List<CatalogItem>> search(String pattern, Long startIndex, Integer limit){
        var searchResultList = catalogQuery.search(pattern);
        maxSearchResult = searchResultList.size();
        LinkedHashMap<SiteBuilder, List<CatalogItem>> resultMap = new LinkedHashMap<>();
        var site = siteService.getSiteByName("Search");
            resultMap.putIfAbsent( site, searchResultList.stream()
                    .sorted((e1,e2)->e2.getTimestamp().compareTo(e1.getTimestamp()))
                    .skip(startIndex)
                    .limit(limit)
                    .collect(Collectors.toList()));
        return resultMap;
    }

    public List<CatalogItem> getList(int startIndex, int limit){
        updateCatalogMap();
        List<CatalogItem> resultList = (ArrayList<CatalogItem>) catalogMap.values().stream().collect(Collectors.toList());
        return resultList.stream().sorted((e1,e2)->e2.getTimestamp().compareTo(e1.getTimestamp())).skip(startIndex).limit(limit).collect(Collectors.toList());

    }
    public Integer maxResultCount(String siteName){
        updateCatalogMap();
        List<CatalogItem> resultList= (List<CatalogItem>) catalogMap.values().stream().collect(Collectors.toList());
        Integer result = Math.toIntExact(resultList.stream().filter((e) -> e.getSiteName().equals(siteName)).count());
        return result;
    }
    public CatalogItem getArticleById(Long id){
        return catalogQuery.getArticleById(id);
    }

    private void updateCatalogMap(){
        Iterable<CatalogItem> loadData= catalogQuery.selectOrdered();
        if(loadData != null) {
            for (CatalogItem item : loadData) {
                catalogMap.putIfAbsent(item.getHref(), item);
            }
        }
    }
    public Long getMaxCount(){
        return catalogQuery.getMaxCount();
    }

}
