package com.example.client.services;


import com.example.client.exceptions.UserNotFoundException;
import com.example.client.model.*;


import com.example.client.repo.FavoritesRepository;
import com.example.client.repo.RoleRepository;
import com.example.client.repo.UserProfileEntityManager;
import com.example.client.repo.UsersRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;


@Service
public class UserService {
    @Autowired
    FavoritesRepository favoritesRepository;
    @Autowired
    UsersRepository usersRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserProfileEntityManager userQuery;

    @Autowired
    SiteService siteService;

    @Autowired
    CopyOnWriteArrayList getLoggedUsers;


    public void saveFavorites( Favorites favorites){
        if(!favoritesRepository.ifExists(favorites.getUserId(), favorites.getItemId())) {
            favoritesRepository.save(favorites);
        }
    }
    public void removeFavorites( Long userId, Long itemId){
        Favorites removeFav = favoritesRepository.findByUserIdAndItemId(userId, itemId);
        favoritesRepository.delete(removeFav);
    }

    public void addLoggedUser(User loggedUser){
        if(!isUserExists(loggedUser)) {
            getLoggedUsers.add(loggedUser);

        }
    }
    public void updateLoggedUser(User loggedUser){
        List<User> loggedUsers= (List<User>) getLoggedUsers.clone();
        for(int i = 0; i < loggedUsers.size(); i++){
            if(loggedUsers.get(i).getId() == loggedUser.getId()){
                Optional<User> updatedUser = usersRepository.findById(loggedUser.getId());
                if(updatedUser.isPresent()) {
                    getLoggedUsers.set(i, updatedUser.get());
                    System.out.println(updatedUser);
                }else{
                    throw new UserNotFoundException();
                }
            }
        }
    }
    public Optional<User> getLoggedUserByName(String userName){
        List<User> loggedUsers= (List<User>) getLoggedUsers.clone();
        return loggedUsers.stream().filter((e)->e.getUsername().equals(userName)).findFirst();
    }
    private boolean isUserExists(User user){
        Optional<User> checkUser = getLoggedUserByName(user.getUsername());
        if(checkUser.isPresent()) return true;
        return false;
    }

    @Transactional
    public User registerNewUser(String userName, String password) {
        User newUser = new User();
        Role newRole = roleRepository.findByName(Roles.ANONYMOUS.name());

        newUser.setUsername(userName);
        newUser.setPassword(password);
        newUser.setRegisterDate(new Date(System.currentTimeMillis()));
        newUser.setTimestamp(System.currentTimeMillis());
        newUser.setLastActivity(System.currentTimeMillis());
        newUser.setRoles(Arrays.asList(newRole));
        newUser = createSiteData(newUser);

        usersRepository.save(newUser);
        //newUser = usersRepository.findByUsername(newUser.getUsername());

        return newUser;
    }
    private User createSiteData(User user){
        var sites = siteService.getSitesList();
        Byte[] site_date = new Byte[sites.size()];
        byte b = 0;
        for ( Integer i = 0; i < sites.size(); i++){
            site_date[i] = i.byteValue();
        }
        user.setSites(site_date);
        return user;
    }
    @Transactional
    public UserProfileDto getUserProfile(String userName){
        UserProfileDto profile = new UserProfileDto();
        User foundUser = getByUserName(userName);
        var siteList = siteService.getSitesList();

        if ( foundUser != null) {
            profile.setUser(foundUser);
            profile.setDefaultSitesList(siteList);
        }else{
            throw new UserNotFoundException("User for name : " + userName + " not found");
        }
        return profile;
    }


    public User getByUserName(String userName){
        return  usersRepository.findByUsername(userName);
    }

    @Transactional
    public UserProfileDto updateUserProfile(String userName, String data) {
        UserProfileDto profile = new UserProfileDto();
        User foundUser = getLoggedUserByName(userName).get();
       // List<Integer> roles =userQuery.getUserRoles(foundUser.getId());

        JSONObject jsonObject = new JSONObject(data);

        if ( foundUser != null) {
            User updateUser = acceptJson(foundUser, jsonObject);
            usersRepository.save(updateUser);
            profile.setUser(updateUser);
            addLoggedUser(updateUser);
        }

        return profile;
    }
    private User acceptJson(User user, JSONObject jsonData){
        System.out.println("Accepted json : " + jsonData.toString());
//        for(String k : jsonData.keySet()){
//            System.out.println(k + " : " + jsonData.get(k));
//        }
        if(jsonData.has("userAlias")){
            Object alias = jsonData.get("userAlias");
            if(alias != JSONObject.NULL){
                String prefix = user.getUsername().substring(0, user.getUsername().indexOf("@"));
                String newName = prefix + "@" + (String) alias;
                user.setUsername(newName);
            }
        }
        if(jsonData.has("user")){
            JSONObject userObj = (JSONObject) jsonData.get("user");
            JSONArray siteMap = (JSONArray) userObj.get("sites");
            Byte[] newSiteMap = new Byte[siteMap.length()];
            for(int i = 0; i < user.getSites().length; i++){
                Integer newVal = (Integer) siteMap.get(i);
                newSiteMap[i] = newVal.byteValue();
            }
            user.setSites(newSiteMap);
        }
        return user;
    }

}
