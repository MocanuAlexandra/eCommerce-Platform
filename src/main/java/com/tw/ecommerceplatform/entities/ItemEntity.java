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
    private long item_id;
    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "item")
    private List<ItemWarehouseEntity> warehouses;

    public ItemEntity(String itemName) {
        this.name = itemName;
    }
}
