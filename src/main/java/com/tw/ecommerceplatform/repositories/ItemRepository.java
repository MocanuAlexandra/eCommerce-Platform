package com.tw.ecommerceplatform.repositories;

import com.tw.ecommerceplatform.entities.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<ItemEntity, Long> {
}
