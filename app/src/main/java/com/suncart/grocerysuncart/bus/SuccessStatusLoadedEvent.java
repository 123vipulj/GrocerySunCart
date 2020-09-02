package com.suncart.grocerysuncart.bus;

import com.suncart.grocerysuncart.model.SuccessStatus;

public class SuccessStatusLoadedEvent {
    public SuccessStatus successStatus;

    public SuccessStatusLoadedEvent(SuccessStatus successStatus) {
        this.successStatus = successStatus;
    }
}
