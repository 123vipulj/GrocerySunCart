package com.suncart.grocerysuncart.service;

import android.content.Context;
import android.util.Log;


import com.suncart.grocerysuncart.bus.ContentLoadedEvent;
import com.suncart.grocerysuncart.bus.SuccessStatusLoadedEvent;
import com.suncart.grocerysuncart.config.GroceryApp;
import com.suncart.grocerysuncart.exception.ApiErrorEvent;
import com.suncart.grocerysuncart.model.SuccessStatus;

import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TokenUpdatatonService {
    public static final String TAG = ContentService.class.getCanonicalName();

    private final EventBus eventBus = EventBus.getDefault();

    private Context context;

    public TokenUpdatatonService(Context context) {
        this.context = context;
    }

    public void setUpdatedToken(String userId, String tokenVerification){

        Call<SuccessStatus> tokenUpdatedCallBack = GroceryApp.Companion.tokenUpdatation().setUpdateToken(userId, tokenVerification);
        tokenUpdatedCallBack.enqueue(new Callback<SuccessStatus>() {
            @Override
            public void onResponse(Call<SuccessStatus> call, Response<SuccessStatus> response) {
                Log.i(TAG, "### Get Items Response : " + response.body().toString());
                eventBus.post(new SuccessStatusLoadedEvent(response.body()));
            }

            @Override
            public void onFailure(Call<SuccessStatus> call, Throwable t) {
                eventBus.post(new ApiErrorEvent(t));
            }
        });
    }

}
