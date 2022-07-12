package com.vet24.models.enums;

public enum NewsType {
        UPDATING("UPDATING"), ADVERTISING_ACTIONS("ADVERTISING_ACTIONS"), DISCOUNTS("DISCOUNTS"), PROMOTION("PROMOTION");

    private String code;

    NewsType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
