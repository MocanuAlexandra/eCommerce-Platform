package com.tw.ecommerceplatform.repositories;

import com.tw.ecommerceplatform.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
