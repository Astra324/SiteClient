package com.example.client.controllers;

import com.example.client.model.UserProfileDto;
import com.example.client.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
public class UserProfileController {

    @Autowired
    UserService userService;

    @PostMapping("/app-user-profiler")
    public UserProfileDto getUserProfile(HttpServletRequest request){
        UserProfileDto profile = null;
        System.out.println("User : " +  request.getHeader("username")+ " ask profile.");
        String userName = request.getHeader("username");
        if(!userName.isEmpty()){
            profile = userService.getUserProfile(userName);
        }
        return profile;
    }
    @PostMapping("/app-user-profiler-update")
    public UserProfileDto updateUserProfile(@RequestBody String data, HttpServletRequest request, HttpServletResponse response){
        UserProfileDto profile = null;
        System.out.println("User : " +  request.getHeader("username")+ " ask profile update.");
        System.out.println("Request body : " + data);

        String userName = request.getHeader("username");

        if(!userName.isEmpty()){
            profile = userService.updateUserProfile(userName, data);

            //response.addCookie(createCookie(profile.getUser().getUsername()));
        }
        System.out.println("#### : " + profile);
        return profile;
    }
    private Cookie createCookie(String userName){
        Cookie newCookie = new Cookie("username", userName);
        newCookie.setPath("/");
        newCookie.setSecure(true);
        newCookie.setMaxAge(31536000);
        newCookie.setHttpOnly(false);
        return newCookie;
    }
}
