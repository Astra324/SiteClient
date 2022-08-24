package com.example.client.model;

import com.example.client.site_engine.SiteBuilder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import java.util.LinkedHashMap;
import java.util.List;

public class ClientDto {
    @Getter @Setter private  String appHost;
    @Getter @Setter private String caller;
    @Getter @Setter private String dataSource;
    @Getter @Setter private Long maxResultCount;
    @Getter @Setter private Integer currentIndex;
    @Getter @Setter private  String userName;
    @Getter @Setter private List<SiteBuilder> siteList;
    @Getter @Setter private List<Integer> clientSiteMap;
    @Getter @Setter private LinkedHashMap<SiteBuilder, List<CatalogItem>> dataMap;

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
