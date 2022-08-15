package com.example.client.model;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

@Data
@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "username", length = 500 ,nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false, unique = true)
    private String password;

    @Column(name="email")
    private String email;

//    @ManyToMany
//    @JoinTable(name = "roles"
//            ,joinColumns = @JoinColumn(name="user_id")
//            ,inverseJoinColumns = @JoinColumn(name = "role_id"))
//    private Collection<Roles> rolesCollections;

}
