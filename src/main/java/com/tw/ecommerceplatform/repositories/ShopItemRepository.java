package com.tw.ecommerceplatform.repositories;

import com.tw.ecommerceplatform.entities.ShopEntity;
import com.tw.ecommerceplatform.entities.ShopItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShopItemRepository extends org.springframework.data.jpa.repository.JpaRepository<ShopItem, Long> {
    @Query("SELECT si FROM ShopItem si WHERE si.shop = :shop")
    List<ShopItem> findByShop(@Param("shop") ShopEntity shop);

    @Query("SELECT si FROM ShopItem si WHERE si.shop = :shop and si.item.name = :name")
    ShopItem findByShopAndItem_Name(ShopEntity shop, String name);

    List<ShopItem> findByItemNameContainingIgnoreCase(String term);

    List<ShopItem> findByItemNameContainingIgnoreCaseAndShopIdIn(String searchedItem, List<Long> requiredShop);

    List<ShopItem> findAllByShopIdIn(List<Long> requiredShop);

    @Query("SELECT si FROM ShopItem si WHERE si.quantity > 0")
    List<ShopItem> findAllQuantityGreaterThanZero();
}
