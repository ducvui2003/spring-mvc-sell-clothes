package com.spring.websellspringmvc.utils.constraint;

public enum OrderStatus {
    PENDING(1),
    CONFIRMED(2),
    DELIVERY(3),
    COMPLETED(4),
    CANCELLED(5);

    private final int value;

    OrderStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static OrderStatus getByValue(int value) {
        for (OrderStatus state : OrderStatus.values()) {
            if (state.getValue() == value) {
                return state;
            }
        }
        return null;
    }
}
