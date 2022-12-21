package com.tw.ecommerceplatform.models;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CreateItemModel {
    private String name;
    private int quantity;
}
