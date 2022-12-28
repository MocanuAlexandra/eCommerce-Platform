package com.tw.ecommerceplatform.models;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
public class ListOrderItemModel {
    private List<OrderItemModel> orderItems = new ArrayList<>();

    public void addOrderItem(OrderItemModel item) {
        this.orderItems.add(item);
    }
}
