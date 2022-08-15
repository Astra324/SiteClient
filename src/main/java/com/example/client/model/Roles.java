package com.example.client.model;



import lombok.Data;
import org.springframework.context.annotation.Primary;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Data
@Entity
@Table(name = "roles")
public class Roles {
    public Roles(){
    }
    public Roles(Long userId, Integer roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "role_id", nullable = false)
    private Integer roleId;
}
