package com.example.client.repo;


import com.example.client.model.CatalogItem;
import com.example.client.model.Favorites;
import com.example.client.services.FavoritesService;
import com.example.client.services.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FavoritesEntityManager {
    private Long lastSavedId = 0L;
    private Integer limit = 0;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    SiteService siteService;

    @Autowired
    FavoritesRepository favoritesRepository;

    public FavoritesEntityManager(){

    }

    public List<Favorites> selectOrdered(){
        limit = 100;
        Long checkId = getLastSavedId();
        System.out.println("Max id in catalog : " + checkId + " : last saved id : " + lastSavedId);

            var resultList = entityManager
                    .createQuery("SELECT p FROM Favorites p ORDER BY p.timestamp desc", Favorites.class)
                    .setMaxResults(limit)
                    .getResultList();
            lastSavedId = resultList.stream().mapToLong(v->v.getId()).max().getAsLong();
            return resultList;

    }
    public List<CatalogItem> preloadCatalog(String siteName, Long lastIndex){
        return entityManager
                .createQuery("SELECT p FROM CatalogItem p where p.siteName = '"
                        + siteName + "' AND p.id < "
                        + lastIndex + " ORDER BY p.id desc", CatalogItem.class)
                .getResultList();
    }
    public List<CatalogItem> search(String pattern){
        return entityManager
                .createQuery("SELECT p FROM CatalogItem p where p.title LIKE '%" +  pattern + "%' ", CatalogItem.class)
                .setMaxResults(60)
                .getResultList();
    }

    public Integer ifExists(String href){
        ArrayList<CatalogItem> result = new ArrayList<>();
        result = (ArrayList<CatalogItem>) entityManager
                  .createQuery("SELECT p  FROM CatalogItem p WHERE p.href = \'" + href + "\'", CatalogItem.class).getResultList();
        return result.size();
    }


    private Long getLastSavedId(){
        return entityManager.createQuery("SELECT MAX(p.id) FROM CatalogItem p", Long.class)
                .getSingleResult();
    }
    public long getMaxCount(){
        return favoritesRepository.count();
    }
}
