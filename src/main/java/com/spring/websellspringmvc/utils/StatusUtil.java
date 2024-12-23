package com.spring.websellspringmvc.utils;

import com.spring.websellspringmvc.utils.constraint.OrderStatus;

import java.util.List;

public class StatusUtil {

    public final static List<OrderStatus> STATUS_CAN_CHANGED_ORDER_ITEMS = List.of
            (OrderStatus.VERIFYING,
                    OrderStatus.CHANGE,
                    OrderStatus.PENDING,
                    OrderStatus.PACKAGE);
}
