package com.example.client.model;

import com.example.client.site_engine.SiteBuilder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import java.util.LinkedHashMap;
import java.util.List;
@Getter @Setter
public class ClientDto {
    private  String appHost;
    private String caller;
    private String dataSource;
    private Long maxResultCount;
    private Integer currentIndex;
    private  String userName;
    private List<SiteBuilder> siteList;
    private List<Integer> clientSiteMap;
    private List<CatalogItem> favorites;
    private LinkedHashMap<SiteBuilder, List<CatalogItem>> dataMap;
    private LinkedHashMap<ListTitle, List<CatalogItem>> favoritesMap;


    @Override
    public String toString() {
        return "ClientDto{" +
                "dbHost='" + appHost + '\'' +
                ", caller='" + caller + '\'' +
                ", dataSource='" + dataSource + '\'' +
                ", maxResultCount=" + maxResultCount +
                ", currentIndex=" + currentIndex +
                ", siteList=" + siteList +
                ", dataMap=" + dataMap +
                '}';
    }
}
