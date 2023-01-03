package com.tw.ecommerceplatform.models;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@RequiredArgsConstructor
public class PurchaseItemModel {
    private String itemName;
    private String shopName;
    private int itemQuantity;

    public PurchaseItemModel(String name, String shopName, int i) {
        this.itemName = name;
        this.shopName = shopName;
        this.itemQuantity = i;
    }
}
