package com.tw.ecommerceplatform.repositories;

import com.tw.ecommerceplatform.entities.WarehouseEntity;
import com.tw.ecommerceplatform.entities.WarehouseItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WarehouseItemRepository extends JpaRepository<WarehouseItem, Long> {

    @Query("SELECT iw FROM WarehouseItem iw WHERE iw.warehouse = :warehouse")
    List<WarehouseItem> findByWarehouse(@Param("warehouse") WarehouseEntity warehouse);

    @Query("SELECT iw FROM WarehouseItem iw WHERE iw.warehouse = :warehouse and iw.item.id = :id")
    WarehouseItem findByItem_Id(@Param("warehouse") WarehouseEntity warehouse, Long id);

    WarehouseItem findByWarehouseAndItem_Name(WarehouseEntity warehouse, String itemName);
}
