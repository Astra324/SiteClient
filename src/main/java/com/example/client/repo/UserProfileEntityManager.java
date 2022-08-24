package com.example.client.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserProfileEntityManager {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    UsersRepository usersRepository;

    public List<Integer> getUserRoles(Long userId){
        var resultList = entityManager
                .createQuery("SELECT p.roleId FROM Roles p where p.userId = " + userId, Integer.class)
                .getResultList();
        return resultList;
    }

}
