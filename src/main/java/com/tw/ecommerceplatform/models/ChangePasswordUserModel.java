package com.tw.ecommerceplatform.models;

import lombok.Data;

@Data
public class ChangePasswordUserModel {
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
}

