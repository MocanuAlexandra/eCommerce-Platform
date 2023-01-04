package com.tw.ecommerceplatform.repositories;

import com.tw.ecommerceplatform.entities.ShopEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShopRepository extends JpaRepository<ShopEntity, Long> {

    ShopEntity findByName(String name);

    @Query("SELECT s FROM ShopEntity s WHERE s.adminShop.status = 'PENDING'")
    List<ShopEntity> findAllPendingShops();

    @Query("SELECT s FROM ShopEntity s WHERE s.adminShop.status = 'APPROVED'")
    List<ShopEntity> findAllApprovedShops();

    void deleteById(Long id);

    ShopEntity findByAdminShop_Email(String username);

    List<ShopEntity> findAll();
}
