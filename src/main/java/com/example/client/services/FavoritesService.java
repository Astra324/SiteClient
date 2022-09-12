package com.example.client.services;

import com.example.client.model.CatalogItem;
import com.example.client.model.Favorites;
import com.example.client.model.User;
import com.example.client.repo.FavoritesRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class FavoritesService {
    @Autowired
    UserService userService;

    @Getter private Integer pageLimit = 15;

    @Autowired
    FavoritesRepository favoritesRepository;

    @Transactional
    public LinkedHashMap<String, List<CatalogItem>> getUserFavorites(String username, Integer start){
        System.out.println("getFavoritesTest : " + username );
        User user = userService.getLoggedUserByName(username).orElseThrow(NullPointerException::new);
        List<CatalogItem> catalogItems = user.getFavorites();

        List<Favorites> favorites = favoritesRepository.findByUserIdOrderByDateAddedDesc(user.getId())
                .stream().skip(start).limit(pageLimit).collect(toList());

        var titles = favorites.stream().map(e->e.getDateAdded())
                .distinct().sorted(Comparator.reverseOrder()).collect(Collectors.toList());

        var dataMap = new LinkedHashMap<String, List<CatalogItem>>();

        for(Date date : titles) {
            List<CatalogItem> resultItemsList = favorites.stream().filter((e) -> e.getDateAdded().getTime() == date.getTime())
                    .map((e) -> convertFavorites(e, catalogItems).get())
                    .sorted((i, i1)-> i1.getTimestamp().compareTo(i.getTimestamp()))
                    .collect(toList());
            dataMap.putIfAbsent(date.toString(), resultItemsList);
        }
        return dataMap;
    }
    private Optional<CatalogItem> convertFavorites(Favorites favorites, List<CatalogItem> catalogItems){
        return catalogItems.stream().filter((e)-> Objects.equals(e.getId(), favorites.getItemId())).findFirst();
    }

    public Long maxResult (){
        return favoritesRepository.count();
    }
}
