package com.suncart.grocerysuncart.service;

import android.content.Context;
import android.util.Log;


import com.suncart.grocerysuncart.bus.SuccessStatusLoadedEvent;
import com.suncart.grocerysuncart.config.GroceryApp;
import com.suncart.grocerysuncart.exception.ApiErrorEvent;
import com.suncart.grocerysuncart.model.SuccessStatus;

import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserService {
    public static final String TAG = UserService.class.getCanonicalName();
    Context context;
    private final EventBus eventBus = EventBus.getDefault();

    public UserService(Context context) {
        this.context = context;
    }

    public void getPhoneVerficationStatus(String phoneNumber){
        Call<SuccessStatus> successStatusCallBack = GroceryApp.Companion.getPhoneValidation().getPhoneVerification(phoneNumber);
        successStatusCallBack.enqueue(new Callback<SuccessStatus>() {
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
