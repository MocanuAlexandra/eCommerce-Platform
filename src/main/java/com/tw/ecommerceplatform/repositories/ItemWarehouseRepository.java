package com.tw.ecommerceplatform.repositories;

import com.tw.ecommerceplatform.entities.ItemWarehouse;
import com.tw.ecommerceplatform.entities.WarehouseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemWarehouseRepository extends JpaRepository<ItemWarehouse,Long> {

    @Query("SELECT iw FROM ItemWarehouse iw WHERE iw.warehouse = :warehouse")
    List<ItemWarehouse> findByWarehouse(@Param("warehouse") WarehouseEntity warehouse);

}
