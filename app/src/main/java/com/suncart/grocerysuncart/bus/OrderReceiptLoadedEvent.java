package com.suncart.grocerysuncart.bus;

import com.suncart.grocerysuncart.model.content.OrderStatusReceipt;

import java.util.List;

public class OrderReceiptLoadedEvent {
    public List<OrderStatusReceipt> orderStatusReceiptList;

    public OrderReceiptLoadedEvent(List<OrderStatusReceipt> orderStatusReceiptList) {
        this.orderStatusReceiptList = orderStatusReceiptList;
    }
}
