package com.tw.ecommerceplatform.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
@Table(name = "user", schema = "public", catalog = "demo")
public class UserEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "password")
    private String password;
    @Transient
    private String passwordConfirm;
    @ManyToOne
    private RoleEntity role;

    public UserEntity(String email, String password, RoleEntity role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
