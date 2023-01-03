package com.tw.ecommerceplatform.repositories;

import com.tw.ecommerceplatform.entities.PurchaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<PurchaseEntity, Long> {
    PurchaseEntity findById(long id);
}
