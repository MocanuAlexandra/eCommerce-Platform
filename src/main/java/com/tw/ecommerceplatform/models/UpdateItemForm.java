package com.tw.ecommerceplatform.models;

import lombok.Data;

@Data
public class UpdateItemForm {
    private String itemId;
    private int quantity;
}
