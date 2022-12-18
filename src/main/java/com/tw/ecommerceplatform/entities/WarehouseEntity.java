package com.tw.ecommerceplatform.entities;

import com.tw.ecommerceplatform.Utility.RegistrationStatus;
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

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private RegistrationStatus status;

    //TODO add itemes many to many
    @OneToOne
    @JoinColumn(name = "admin_id")
    private UserEntity adminWarehouse;

    public WarehouseEntity(String name, String address, String code, UserEntity admin, RegistrationStatus status) {
        this.name = name;
        this.address = address;
        this.code = code;
        this.adminWarehouse = admin;
        this.status = status;
    }
}

