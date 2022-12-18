package com.tw.ecommerceplatform.models;

import lombok.Data;

@Data
public class RegisterWarehouseModel extends RegisterUserModel{
    private String name;
    private String address;
    private String code;
}
