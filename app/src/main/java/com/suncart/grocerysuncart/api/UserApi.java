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

    @FormUrlEncoded
    @POST("/v1/api/contentprod/user/order_transaction.php/{id}/{razor_pay_order_id}/{razor_signature}/{razor_payment_id}/{status}")
    Call<SuccessStatus> postOrderData(@Field("id") String userId, @Field("razor_pay_order_id") String orderId,
                                      @Field("razor_signature") String signature, @Field("razor_payment_id") String paymentID,
                                      @Field("status") String orderStatus);

}
