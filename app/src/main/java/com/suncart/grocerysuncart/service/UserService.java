package com.suncart.grocerysuncart.service;

import android.content.Context;
import android.util.Log;


import com.suncart.grocerysuncart.activity.OrderList;
import com.suncart.grocerysuncart.bus.OrderListLoadedEvent;
import com.suncart.grocerysuncart.bus.SuccessStatusLoadedEvent;
import com.suncart.grocerysuncart.config.GroceryApp;
import com.suncart.grocerysuncart.exception.ApiErrorEvent;
import com.suncart.grocerysuncart.model.SuccessStatus;
import com.suncart.grocerysuncart.model.content.OrderStatus;

import java.util.List;

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

    public void postOrderData(String userId, String orderID, String signature, String paymentId, String status){
        Call<SuccessStatus> successStatusCallBack = GroceryApp.Companion.getPhoneValidation().postOrderData(userId, orderID, signature, paymentId, status);
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

    public void postOrderProduct(String jsonData, String userId, String orderId){
        Call<SuccessStatus> successStatusCallBack = GroceryApp.Companion.getPhoneValidation()
                .postOrderProductData(jsonData, userId, orderId);

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

    public void getOrderDataList(String userId){
        Call<List<OrderStatus>> successStatusCallBack = GroceryApp.Companion.getPhoneValidation().getOrderList(userId);
        successStatusCallBack.enqueue(new Callback<List<OrderStatus>>() {
            @Override
            public void onResponse(Call<List<OrderStatus>> call, Response<List<OrderStatus>> response) {
                Log.i(TAG, "### Get Items Response : " + response.body().toString());
                eventBus.post(new OrderListLoadedEvent(response.body()));
            }

            @Override
            public void onFailure(Call<List<OrderStatus>> call, Throwable t) {
                eventBus.post(new ApiErrorEvent(t));
            }
        });
    }
}
