package com.tw.ecommerceplatform.repositories;

import com.tw.ecommerceplatform.entities.PurchaseItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseItemRepository extends JpaRepository<PurchaseItem, Long> {
}
