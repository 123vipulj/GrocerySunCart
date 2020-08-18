package com.suncart.grocerysuncart.bus;

import com.suncart.grocerysuncart.model.content.ContentItems;

import java.util.List;


public class ContentLoadedEvent {

    public List<ContentItems> contentItemsList;

    public ContentLoadedEvent(List<ContentItems> contentItemsList) {
        this.contentItemsList = contentItemsList;
    }
}
