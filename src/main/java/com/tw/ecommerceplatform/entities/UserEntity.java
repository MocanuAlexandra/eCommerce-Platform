package com.tw.ecommerceplatform.entities;

import com.tw.ecommerceplatform.utility.RegistrationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "user", schema = "public")
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

    @OneToMany(mappedBy = "customer")
    private List<PurchaseEntity> purchases;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private RegistrationStatus status;

    public UserEntity(String email, String password, RoleEntity role, RegistrationStatus status) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = status;
    }
}
