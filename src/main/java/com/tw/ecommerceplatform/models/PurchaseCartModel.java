package com.tw.ecommerceplatform.models;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
public class PurchaseCartModel {
    private List<PurchaseItemModel> purchaseItems = new ArrayList<>();

    public void addToPurchaseCart(PurchaseItemModel item) {
       this.purchaseItems.add(item);
    }
}
