package com.suncart.grocerysuncart.model;

import com.google.gson.annotations.SerializedName;

public class ServerResponse {
    // variable name should be same as in the json response from php
    @SerializedName("success")
    boolean success;

    @SerializedName("message")
    String message;

    @SerializedName(("url"))
    String url;

    public ServerResponse() {
    }

    public ServerResponse(ServerResponse response) {
        this.message = response.message;
        this.success = response.success;
        this.url = response.url;
    }

    public String getUrl() {
        return url;
    }

    String getMessage() {
        return message;
    }
    boolean getSuccess() {
        return success;
    }
}
