package com.spring.websellspringmvc.utils.constraint;

import lombok.Getter;

@Getter
public enum OrderStatus {
    VERIFYING(6, "Chờ xác thực"),
    CHANGED(7, "Đơn hàng đã thay đổi chờ xác thực lại"),
    PENDING(1, "Chờ xác nhận"),
    PACKAGE(2, "Chờ đóng gói"),
    DELIVERY(3, "Đang vận chuyển"),
    COMPLETED(4, "Hoàn thành"),
    CANCELLED(5, "Hủy");

    private final int value;
    private final String displayName;

    OrderStatus(int value, String displayName) {
        this.value = value;
        this.displayName = displayName;
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
