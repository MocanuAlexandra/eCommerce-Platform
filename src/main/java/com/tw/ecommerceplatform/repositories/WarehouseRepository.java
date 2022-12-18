package com.tw.ecommerceplatform.repositories;

import com.tw.ecommerceplatform.entities.WarehouseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseRepository extends JpaRepository<WarehouseEntity, Long> {

    WarehouseEntity findByName(String name);
}
