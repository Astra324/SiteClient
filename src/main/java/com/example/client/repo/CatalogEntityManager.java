package com.example.client.repo;


import com.example.client.model.CatalogItem;
import com.example.client.services.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CatalogEntityManager {
    private Long lastSavedId = 0L;
    private Integer limit = 0;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    SiteService siteService;

    @Autowired
    CatalogRepository catalogRepository;

    public CatalogEntityManager(){

    }

    public List<CatalogItem> selectOrdered(){
        limit = siteService.getSitesList().size() * 300;
        Long checkId = getLastSavedId();
        System.out.println("Max id in catalog : " + checkId + " : last saved id : " + lastSavedId);
        if(lastSavedId < checkId) {
            var resultList = entityManager
                    .createQuery("SELECT p FROM CatalogItem p where p.id > " + lastSavedId + " ORDER BY p.id desc", CatalogItem.class)
                    .setMaxResults(limit)
                    .getResultList();
            lastSavedId = checkId;
            return resultList;
        }
        return null;
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
    public CatalogItem getArticleById(Long id){
        CatalogItem result = catalogRepository.findById(id).get();
        System.out.println("Found article : " + result.toJson());
        return result;
    }

    private Long getLastSavedId(){
        return entityManager.createQuery("SELECT MAX(p.id) FROM CatalogItem p", Long.class)
                .getSingleResult();
    }
    public long getMaxCount(){
        return catalogRepository.count();
    }
}
