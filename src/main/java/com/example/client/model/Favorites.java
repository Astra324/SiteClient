package com.example.client.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.lang.model.element.Name;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;


@Getter
@Setter
@Entity
@Table(name="users_favorites")
public class Favorites {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id", unique = false)
    private Long userId;

    @Column(name = "item_id", unique = false)
    private Long itemId;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_added")
    private Date  dateAdded;


    @Column(name = "timestamp")
    @Getter @Setter private Long  timestamp;




}
