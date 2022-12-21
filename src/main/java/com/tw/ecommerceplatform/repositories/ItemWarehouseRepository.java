package com.tw.ecommerceplatform.repositories;

import com.tw.ecommerceplatform.entities.ItemWarehouseEntity;
import com.tw.ecommerceplatform.entities.WarehouseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemWarehouseRepository extends JpaRepository<ItemWarehouseEntity,Long> {

    @Query("SELECT iw FROM ItemWarehouseEntity iw WHERE iw.warehouse = :warehouse")
    List<ItemWarehouseEntity> findByWarehouse(@Param("warehouse") WarehouseEntity warehouse);

    ItemWarehouseEntity findByItem(Long itemId);
}
