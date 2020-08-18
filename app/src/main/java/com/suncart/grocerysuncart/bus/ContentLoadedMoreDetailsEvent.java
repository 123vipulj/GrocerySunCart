package com.suncart.grocerysuncart.bus;

import com.suncart.grocerysuncart.model.content.ContentItemsMoreDetails;

import java.util.List;

public class ContentLoadedMoreDetailsEvent {

    public List<ContentItemsMoreDetails> loadedMoreDetailsEvents;

    public ContentLoadedMoreDetailsEvent(List<ContentItemsMoreDetails> loadedMoreDetailsEvents) {
        this.loadedMoreDetailsEvents = loadedMoreDetailsEvents;
    }
}
