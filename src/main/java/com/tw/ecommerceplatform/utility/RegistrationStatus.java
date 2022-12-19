package com.tw.ecommerceplatform.utility;

public enum RegistrationStatus {
    APPROVED("Approved"),
    PENDING("Pending"),
    REJECTED("Rejected");

    private final String name;

    RegistrationStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
