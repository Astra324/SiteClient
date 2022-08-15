package com.example.client.repo;


import com.example.client.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UsersRepository extends CrudRepository<User, Long> {
    public User findByUsername(String username);
}
