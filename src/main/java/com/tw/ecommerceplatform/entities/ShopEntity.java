package com.tw.ecommerceplatform.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "shop", schema = "public")
public class ShopEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;
    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "business_code")
    private String business_code;

    @OneToOne
    @JoinColumn(name = "admin_id")
    private UserEntity adminShop;

    @OneToMany(mappedBy = "shop")
    private List<OrderEntity> orders;

    @OneToMany(mappedBy = "shop")
    private List<ShopItem> items;

    public ShopEntity(String name, String address, String business_code, UserEntity admin) {
        this.name = name;
        this.address = address;
        this.business_code = business_code;
        this.adminShop = admin;
    }
}
