package com.tw.ecommerceplatform.utility;

public enum Role {
    CUSTOMER("ROLE_CUSTOMER"),
    ADMIN("ROLE_ADMIN"),
    WAREHOUSE_ADMIN("ROLE_WAREHOUSE_ADMIN"),
    SHOP_ADMIN("ROLE_SHOP_ADMIN");

    private final String name;

    Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
