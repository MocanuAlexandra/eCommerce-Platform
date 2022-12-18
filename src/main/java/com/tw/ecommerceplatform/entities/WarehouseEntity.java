package com.tw.ecommerceplatform.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
@Table(name = "warehouse", schema = "public")
public class WarehouseEntity{

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;
    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "code")
    private String code;

    @OneToOne
    @JoinColumn(name = "admin_id")
    private UserEntity adminWarehouse;

    public WarehouseEntity(String name, String address, String code, UserEntity admin) {
        this.name = name;
        this.address = address;
        this.code = code;
        this.adminWarehouse = admin;
    }
}

