package com.suncart.grocerysuncart.bus;

import com.suncart.grocerysuncart.model.content.OrderStatus;

import java.util.List;

public class OrderListLoadedEvent {
    public List<OrderStatus> orderStatusList;

    public OrderListLoadedEvent(List<OrderStatus> orderStatusList) {
        this.orderStatusList = orderStatusList;
    }
}
