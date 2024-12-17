package com.spring.websellspringmvc.utils.constraint;

public enum TransactionStatus {
    UN_PAID(1),
    PROCESSING(2),
    PAID(3),
    ERROR(4);

    private final int value;

    TransactionStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static TransactionStatus getByValue(int value) {
        for (TransactionStatus state : TransactionStatus.values()) {
            if (state.getValue() == value) {
                return state;
            }
        }
        return null;
    }
}
