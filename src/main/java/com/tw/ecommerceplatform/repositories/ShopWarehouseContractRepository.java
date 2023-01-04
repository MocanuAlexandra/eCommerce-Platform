package com.tw.ecommerceplatform.repositories;

import com.tw.ecommerceplatform.entities.ShopEntity;
import com.tw.ecommerceplatform.entities.ShopWarehouseContract;
import com.tw.ecommerceplatform.entities.WarehouseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShopWarehouseContractRepository extends JpaRepository<ShopWarehouseContract, Long> {

    @Query("SELECT c FROM ShopWarehouseContract c WHERE c.shop.id=:shopId and c.status = 'APPROVED'")
    List<ShopWarehouseContract> findAllByShopAndApprovedStatus(Long shopId);

    @Query("SELECT c FROM ShopWarehouseContract c WHERE c.warehouse.warehouse_id=:warehouseId and c.status = 'PENDING'")
    List<ShopWarehouseContract> findAllByWarehouseAndPendingStatus(Long warehouseId);

    @Query("SELECT c FROM ShopWarehouseContract c WHERE c.shop.id=:shopId and c.status = 'NON_EXISTENT'")
    List<ShopWarehouseContract> findAllByShopAndNonExistingStatus(Long shopId);

    ShopWarehouseContract findByShopAndWarehouse(ShopEntity shop, WarehouseEntity warehouse);
}
