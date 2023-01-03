package com.tw.ecommerceplatform.models;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
public class OrderCartModel {
    private List<OrderItemModel> orderItems = new ArrayList<>();

    public void addToOrderCart(OrderItemModel item) {
        this.orderItems.add(item);
    }
}
