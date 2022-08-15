package com.example.client.repo;

import com.example.client.model.Roles;
import com.example.client.model.User;
import org.springframework.data.repository.CrudRepository;

public interface RolesRepo extends CrudRepository<Roles, Long> {
}
