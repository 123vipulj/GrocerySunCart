package com.suncart.grocerysuncart.service;

import android.content.Context;
import android.util.Log;

import com.suncart.grocerysuncart.bus.ContentLoadedEvent;
import com.suncart.grocerysuncart.bus.ContentLoadedMoreDetailsEvent;
import com.suncart.grocerysuncart.config.GroceryApp;
import com.suncart.grocerysuncart.exception.ApiErrorEvent;
import com.suncart.grocerysuncart.model.content.ContentItems;
import com.suncart.grocerysuncart.model.content.ContentItemsMoreDetails;

import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContentService {

    public static final String TAG = ContentService.class.getCanonicalName();

    private final EventBus eventBus = EventBus.getDefault();
    private final EventBus eventBusLikes = EventBus.getDefault();

    private Context context;

    public ContentService(Context context) {
        this.context = context;
    }

    // get all news
    public void getAllContentItems() {

        Call<List<ContentItems>> contentResponseCall = GroceryApp.Companion.getNewsApi().getAllContent();

        contentResponseCall.enqueue(new Callback<List<ContentItems>>() {
            @Override
            public void onResponse(Call<List<ContentItems>> call, Response<List<ContentItems>> response) {
                Log.i(TAG, "### Get Items Response : " + response.body().toString());
                eventBus.post(new ContentLoadedEvent(response.body()));
            }

            @Override
            public void onFailure(Call<List<ContentItems>> call, Throwable t) {
                eventBus.post(new ApiErrorEvent(t));
            }
        });

    }

    // get all news
    public void getAllContentItemsMoreDetails(String ids) {

        Call<List<ContentItemsMoreDetails>> contentResponseCall = GroceryApp.Companion.getNewsApi().getAllContentDetails(ids);

        contentResponseCall.enqueue(new Callback<List<ContentItemsMoreDetails>>() {
            @Override
            public void onResponse(Call<List<ContentItemsMoreDetails>> call, Response<List<ContentItemsMoreDetails>> response) {
                Log.i(TAG, "### Get Items Response : " + response.body().toString());
                eventBus.post(new ContentLoadedMoreDetailsEvent(response.body()));
            }

            @Override
            public void onFailure(Call<List<ContentItemsMoreDetails>> call, Throwable t) {
                eventBus.post(new ApiErrorEvent(t));
            }
        });

    }

    // get all news
    public void getAllContentItemsByCategories(String catNames) {

        Call<List<ContentItems>> contentResponseCall = GroceryApp.Companion.getNewsApi().getAllContentByCat(catNames);

        contentResponseCall.enqueue(new Callback<List<ContentItems>>() {
            @Override
            public void onResponse(Call<List<ContentItems>> call, Response<List<ContentItems>> response) {
                Log.i(TAG, "### Get Items Response : " + response.body().toString());
                eventBus.post(new ContentLoadedEvent(response.body()));
            }

            @Override
            public void onFailure(Call<List<ContentItems>> call, Throwable t) {
                eventBus.post(new ApiErrorEvent(t));
            }
        });

    }

}
