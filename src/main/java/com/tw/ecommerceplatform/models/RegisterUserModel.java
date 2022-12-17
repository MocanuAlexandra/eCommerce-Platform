package com.tw.ecommerceplatform.models;

import lombok.Data;

@Data
public class RegisterUserModel {
    private String username;
    private String password;
    private String confirmPassword;
}
