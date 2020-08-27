package com.suncart.grocerysuncart.api;

import com.suncart.grocerysuncart.model.content.ContentItems;
import com.suncart.grocerysuncart.model.content.ContentItemsMoreDetails;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;

public interface ContentApi {

    @POST("/v1/api/contentprod/content/contentdata.php")
    Call<List<ContentItems>> getAllContent();

    @FormUrlEncoded
    @POST("/v1/api/contentprod/content/contentdatamoredetails.php/{ids}")
    Call<List<ContentItemsMoreDetails>> getAllContentDetails(@Field("ids") String ids);

    @FormUrlEncoded
    @POST("v1/api/contentprod/content/contentdatabycategories.php/{cat_names}")
    Call<List<ContentItems>> getAllContentByCat(@Field("cat_names") String catNames);
}
