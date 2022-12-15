package com.tw.ecommerceplatform.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "user", schema = "public", catalog = "demo")
public class UserEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;
    @Column(name = "username",unique = true)
    private String username;
    @Column(name = "password")
    private String password;
    @Transient
    private String passwordConfirm;
    @ManyToOne
    private RoleEntity role;

    public UserEntity(){};
    public UserEntity(String username, String password, RoleEntity role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
