package com.spring.websellspringmvc.services.voucher;

public enum VoucherState {
    VISIBLE("1"),
    HIDE("2");

    private String value;

    @Override
    public String toString() {
        return value;
    }

    private VoucherState(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
