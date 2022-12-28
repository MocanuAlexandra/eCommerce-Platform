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
@Table(name = "warehouse", schema = "public")
public class WarehouseEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long warehouse_id;
    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "business_code")
    private String business_code;

    @OneToOne
    @JoinColumn(name = "admin_id")
    private UserEntity adminWarehouse;

    @OneToMany(mappedBy = "warehouse")
    private List<OrderEntity> orders;

    public WarehouseEntity(String name, String address, String business_code, UserEntity admin) {
        this.name = name;
        this.address = address;
        this.business_code = business_code;
        this.adminWarehouse = admin;
    }

    @OneToMany(mappedBy = "warehouse")
    private List<WarehouseItem> items;
}

