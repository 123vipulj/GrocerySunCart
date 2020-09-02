package com.suncart.grocerysuncart.api;

import android.content.Context;

import com.suncart.grocerysuncart.config.GroceryApp;
import com.suncart.grocerysuncart.model.SuccessStatus;
import com.suncart.grocerysuncart.model.content.ContentItems;
import com.suncart.grocerysuncart.service.ContentService;

import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface TokenUpdaterApi {

    @FormUrlEncoded
    @POST("/v1/api/contentprod/content/updatetoken.php/{token_verification}")
    Call<SuccessStatus> setUpdateToken(@Field("token_verification") String tokenVerfication);

}
