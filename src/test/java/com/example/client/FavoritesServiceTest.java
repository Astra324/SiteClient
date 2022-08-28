package com.example.client;

import com.example.client.model.CatalogItem;
import com.example.client.model.Favorites;
import com.example.client.model.ListTitle;
import com.example.client.model.User;
import com.example.client.repo.FavoritesRepository;
import com.example.client.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@SpringBootTest
public class FavoritesServiceTest {
    @Autowired
    UserService userService;

    @Autowired
    FavoritesRepository favoritesRepository;

    @Test
    @Transactional
    void getFavoritesTest(){
        System.out.println("getFavoritesTest"); //7E31BF86C0AE496FAA54F073A7D22339-1661367633681@admin--
        User user = userService.getByUserName("7E31BF86C0AE496FAA54F073A7D22339-1661367633681@admin--");
        List<CatalogItem> catalogItems = user.getFavorites();

        List<Favorites> favorites = favoritesRepository.findByUserId(user.getId());
        var titles = favorites.stream().map(e->e.getDateAdded()).distinct().sorted().collect(Collectors.toList());

        var dataMap = new LinkedHashMap<ListTitle, List<CatalogItem>>();

        for(Date date : titles) {
            List<CatalogItem> resultItemsList = favorites.stream().filter((e) -> e.getDateAdded().getTime() == date.getTime())
                    .map((e) -> convertFavorites(e, catalogItems).get()).collect(toList());
            dataMap.putIfAbsent(new ListTitle(date.toString()), resultItemsList);
        }


    }
    private Optional<CatalogItem> convertFavorites(Favorites favorites, List<CatalogItem> catalogItems){
        return catalogItems.stream().filter((e)-> Objects.equals(e.getId(), favorites.getItemId())).findFirst();
    }
}
