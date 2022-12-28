package com.tw.ecommerceplatform.repositories;

import com.tw.ecommerceplatform.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
}
