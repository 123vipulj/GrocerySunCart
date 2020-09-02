package com.suncart.grocerysuncart.api;

import com.suncart.grocerysuncart.model.SuccessStatus;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UserApi {
    @FormUrlEncoded
    @POST("/v1/api/contentprod/user/phone_verify.php/{phone_number}")
    Call<SuccessStatus> getPhoneVerification(@Field("phone_number") String phoneNumber);
}
