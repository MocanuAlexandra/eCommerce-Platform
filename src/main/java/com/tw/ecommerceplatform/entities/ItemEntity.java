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
@Table(name = "item", schema = "public")
public class ItemEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;
    @Column(name = "name", unique = true)
    private String name;

    @OneToMany(mappedBy = "item")
    private List<WarehouseItem> warehouses;

    @OneToMany(mappedBy = "item")
    private List<ShopItem> shops;
    @OneToMany(mappedBy = "item")
    private List<OrderItem> orders;

    @OneToMany(mappedBy = "item")
    private List<PurchaseItem> purchases;

    public ItemEntity(String itemName) {
        this.name = itemName;
    }
}
