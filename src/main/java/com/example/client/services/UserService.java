package com.example.client.services;

import com.example.client.model.Roles;
import com.example.client.model.User;
import com.example.client.repo.FavoritesRepo;
import com.example.client.repo.RolesRepo;
import com.example.client.repo.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;


@Service
public class UserService {

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    RolesRepo rolesRepo;

    @Autowired
    FavoritesRepo favorites;

    @Transactional
    public User registerNewUser(String userName, String password, Integer[] roles) {
        User newUser = new User();
        newUser.setUsername(userName);
        newUser.setPassword(password);
        usersRepository.save(newUser);
        newUser = usersRepository.findByUsername(newUser.getUsername());
        for(int role :roles){
            rolesRepo.save(new Roles(newUser.getId(), role));
        }
        return newUser;
    }
    public User getByUserName(String userName){
        return  usersRepository.findByUsername(userName);
    }
}
