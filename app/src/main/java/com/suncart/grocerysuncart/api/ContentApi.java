package com.suncart.grocerysuncart.api;

import com.suncart.grocerysuncart.model.content.ContentItems;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;

public interface ContentApi {

    @POST("/v1/api/news/content/contentdata.php")
    Call<List<ContentItems>> getAllContent();
}
