package com.example.client.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.*;


@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Getter @Setter private Long id;

    @Column(name = "username", nullable = false, unique = true)
    @Getter @Setter private String username;

    @Column(name = "password", nullable = false)
    @Getter @Setter private String password;

    @Column(name="email", unique = true)
    @Getter @Setter private String email;

    @Temporal(TemporalType.DATE)
    @Column(name = "register_date")
    @Getter @Setter private Date registerDate;

    @Column(name = "timestamp")
    @Getter @Setter private Long timestamp;

    @Column(name = "last_activity")
    @Getter @Setter private Long lastActivity;

    @Column (name = "sites_data")
    @Getter @Setter private Byte[] sites;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    @Getter @Setter private Collection<Role> roles;

    @ManyToMany(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinTable(
            name = "users_favorites",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "item_id", referencedColumnName = "id"))

    @Getter @Setter private List<CatalogItem> favorites = new ArrayList<>();

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", registerDate=" + registerDate +
                ", timestamp=" + timestamp +
                ", lastActivity=" + lastActivity +
                ", sites=" + Arrays.toString(sites) +
                ", roles=" + roles +
                ", favorites=" + favorites +
                '}';
    }
}
