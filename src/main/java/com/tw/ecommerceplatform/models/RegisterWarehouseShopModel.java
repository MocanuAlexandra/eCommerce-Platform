package com.tw.ecommerceplatform.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RegisterWarehouseShopModel extends RegisterUserModel {
    private String name;
    private String address;
    private String code;
}
