package com.example.client.controllers;


import com.example.client.model.ClientDto;
import com.example.client.model.User;
import com.example.client.services.CatalogService;
import com.example.client.services.SiteService;
import com.example.client.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@AllArgsConstructor
public class ConflictResolveController {
     SiteService siteService;
     UserService userService;
     CatalogService catalogService;
     private final Integer top5ListLimit = 5;

    @PostMapping("/app-user-not-found-conflict-resolver")
    public String resolveUserNotFound(@RequestParam(name = "alias") String alias,
                                      Model model, HttpSession session, HttpServletResponse response){
        ClientDto respond = new ClientDto();
        String newPassword = session.getId() + "-" + System.currentTimeMillis();
        String newUserName = newPassword + "@" + alias;
        User resolvedUser = userService.registerNewUser(newUserName, newPassword);

        response.addCookie(createCookie(newUserName));

        var siteList = siteService.getSitesListByUserMap(resolvedUser.getSites());
        var map = catalogService.getAggregateMap(siteList,0,top5ListLimit);

        respond.setSiteList(siteList);
        model.addAttribute("user_name", resolvedUser.getUsername());
        model.addAttribute("data_source", "/app-data-top5");
        model.addAttribute("root_link", "/");
        model.addAttribute("app_title", "Top 5 news");
        model.addAttribute("data", respond);
        return "/app-start";
    }

    private Cookie createCookie(String userName){
        Cookie newCookie = new Cookie("username", userName);
        newCookie.setPath("/");
        newCookie.setSecure(false);
        newCookie.setMaxAge(31536000);
        newCookie.setHttpOnly(false);
        return newCookie;
    }
}
