package com.example.client.repo;

import com.example.client.model.CatalogItem;
import com.example.client.model.Favorites;
import com.example.client.model.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

public interface FavoritesRepository extends CrudRepository<Favorites, Long> {

    List<Favorites> findByUserId(Long userId);

    Favorites findByUserIdAndItemId(Long userId, Long itemId);

     default Boolean ifExists(Long userId, Long itemId){
         Favorites res = findByUserIdAndItemId(userId, itemId);
         if(res != null){
             return true;
         }
        return false;
    }


}
