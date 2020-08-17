package com.suncart.grocerysuncart.bus;

import com.suncart.grocerysuncart.model.content.ContentItems;

import java.util.List;


public class ContentLoadedEvent {

    private List<ContentItems> newsItemsList;

    public ContentLoadedEvent(List<ContentItems> newsItemsList) {
        this.newsItemsList = newsItemsList;
    }
}
