package com.example.client.config;


import com.example.client.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class UsersConfig {
    @Bean
    public CopyOnWriteArrayList<User> getLoggedUsers(){
        return new CopyOnWriteArrayList<User>();
    }
}
