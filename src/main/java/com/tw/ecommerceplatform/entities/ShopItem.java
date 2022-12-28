package com.tw.ecommerceplatform.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "shop_item", schema = "public")
public class ShopItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private ItemEntity item;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private ShopEntity shop;

    private int quantity;

    public ShopItem(ItemEntity item, ShopEntity shop, int quantity) {
        this.item = item;
        this.shop = shop;
        this.quantity = quantity;
    }
}

