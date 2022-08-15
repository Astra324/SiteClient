package com.example.client.repo;

import com.example.client.model.Favorites;
import com.example.client.model.User;
import org.springframework.data.repository.CrudRepository;

public interface FavoritesRepo extends CrudRepository<Favorites, Long> {
}
