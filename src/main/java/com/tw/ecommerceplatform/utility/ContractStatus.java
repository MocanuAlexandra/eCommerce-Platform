package com.tw.ecommerceplatform.utility;

public enum ContractStatus {
    NON_EXISTENT("Non_Existent"),
    APPROVED("Approved"),
    PENDING("Pending"),
    REJECTED("Rejected");

    private final String name;

    ContractStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
