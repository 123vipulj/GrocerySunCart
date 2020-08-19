package com.suncart.grocerysuncart.model.fcm;

import com.google.firebase.messaging.RemoteMessage;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class NotificationData  implements Serializable {

    private String title;
    private String imageUrl;

    public NotificationData(RemoteMessage remoteMessage) {

        RemoteMessage.Notification notification = remoteMessage.getNotification();

        assert notification != null;
        this.title = notification.getTitle();

        if (notification.getImageUrl() != null) {
            if (notification.getImageUrl().toString().contains(",")){
                this.imageUrl = Arrays.asList(notification.getImageUrl().toString().split(",")).get(0);
            }else {
                this.imageUrl = notification.getImageUrl().toString();
            }

        }else {
            this.imageUrl = "";
        }

        if (remoteMessage.getData() != null) {
            Map<String, String> data = remoteMessage.getData();
            this.title = String.valueOf(Objects.requireNonNull(data.get("title")).isEmpty() ? "":data.get("title_t"));
            this.imageUrl = String.valueOf(Objects.requireNonNull(data.get("image")).isEmpty() ? "":data.get("image_i"));

        }

    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

}
