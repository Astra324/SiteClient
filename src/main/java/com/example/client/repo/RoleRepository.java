package com.example.client.repo;

import com.example.client.model.Role;
import com.example.client.model.User;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findByName(String name);
}
