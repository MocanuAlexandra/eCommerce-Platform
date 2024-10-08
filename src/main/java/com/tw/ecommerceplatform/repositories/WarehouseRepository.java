package com.tw.ecommerceplatform.repositories;

import com.tw.ecommerceplatform.entities.WarehouseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WarehouseRepository extends JpaRepository<WarehouseEntity, Long> {
    WarehouseEntity findByName(String name);

    void deleteById(Long id);

    WarehouseEntity findByAdminWarehouse_Email(String email);

    @Query("SELECT w FROM WarehouseEntity w WHERE w.adminWarehouse.status = 'PENDING'")
    List<WarehouseEntity> findAllPendingWarehouses();

    @Query("SELECT w FROM WarehouseEntity w WHERE w.adminWarehouse.status = 'APPROVED'")
    List<WarehouseEntity> findAllApprovedWarehouses();
}
