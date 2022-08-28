package com.example.client;

import com.example.client.model.CatalogItem;
import com.example.client.model.Favorites;
import com.example.client.model.ListTitle;
import com.example.client.model.User;
import com.example.client.repo.FavoritesRepository;
import com.example.client.services.UserService;
import net.bytebuddy.dynamic.DynamicType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.OpenOption;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@SpringBootTest
class ClientApplicationTests {
	@Autowired
	UserService userService;

	@Autowired
	FavoritesRepository favoritesRepository;

	@Test
	@Transactional
	void contextLoads() {

	}

}
