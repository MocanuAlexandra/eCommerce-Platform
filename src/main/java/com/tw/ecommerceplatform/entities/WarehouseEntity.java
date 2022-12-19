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
@Table(name = "warehouse", schema = "public")
public class WarehouseEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;
    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "business_code")
    private String business_code;

    //TODO add itemes many to many
    @OneToOne
    @JoinColumn(name = "admin_id")
    private UserEntity adminWarehouse;

    public WarehouseEntity(String name, String address, String business_code, UserEntity admin) {
        this.name = name;
        this.address = address;
        this.business_code = business_code;
        this.adminWarehouse = admin;
    }
}

