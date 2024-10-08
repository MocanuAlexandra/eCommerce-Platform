package com.tw.ecommerceplatform.entities;

import com.tw.ecommerceplatform.utility.ContractStatus;
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
@Table(name = "shop_warehouse_contract", schema = "public")
public class ShopWarehouseContract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "shop_id")
    private ShopEntity shop;
    @OneToOne
    @JoinColumn(name = "warehouse_id")
    private WarehouseEntity warehouse;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ContractStatus status;
}
