package com.suncart.grocerysuncart.bus;

import com.suncart.grocerysuncart.model.content.OrderStatusTrack;

public class OrderStatusTrackLoadedEvent {
    public OrderStatusTrack orderStatusTrack;

    public OrderStatusTrackLoadedEvent(OrderStatusTrack orderStatusTrack) {
        this.orderStatusTrack = orderStatusTrack;
    }
}
