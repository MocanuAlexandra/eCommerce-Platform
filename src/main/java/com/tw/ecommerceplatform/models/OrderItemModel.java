package com.tw.ecommerceplatform.models;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@RequiredArgsConstructor
public class OrderItemModel {
    private String itemName;
    private int itemQuantity;

    public OrderItemModel(String name, int i) {
        this.itemName = name;
        this.itemQuantity = i;
    }
}
