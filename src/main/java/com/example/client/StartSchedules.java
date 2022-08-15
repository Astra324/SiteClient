package com.example.client;

import com.example.client.services.CatalogService;
import com.example.client.services.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@EnableScheduling
public class StartSchedules {
    @Autowired
    SiteService siteService;

    @Autowired
    CatalogService catalogService;

    @Scheduled(fixedDelay = 3600000, initialDelay = 3000 )
    public void dbUpdate(){
        //siteService.callDbUpdate();
        System.out.println("Scheduled update");
    }
}
