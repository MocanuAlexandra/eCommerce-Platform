package com.tw.ecommerceplatform.models;

import lombok.Data;

@Data
public class ChangePasswordModel {
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
}

