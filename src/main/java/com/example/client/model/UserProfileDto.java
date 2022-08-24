package com.example.client.model;

import com.example.client.site_engine.SiteBuilder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class UserProfileDto {
    @Getter @Setter User user;
    @Getter @Setter User userAlias;
    @Getter @Setter List<Integer> roles;
    @Getter @Setter List<SiteBuilder> defaultSitesList;
    @Getter @Setter List<CatalogItem> favorites;

    @Override
    public String toString() {
        return "UserProfileDto{" +
                "user=" + user +
                ", userAlias=" + userAlias +
                ", roles=" + roles +
                ", sites=" + defaultSitesList +
                ", favorites=" + favorites +
                '}';
    }
}
