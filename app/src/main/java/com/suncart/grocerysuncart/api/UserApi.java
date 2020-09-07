package com.suncart.grocerysuncart.api;

import com.suncart.grocerysuncart.model.SuccessStatus;
import com.suncart.grocerysuncart.model.content.OrderStatus;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UserApi {
    @FormUrlEncoded
    @POST("/v1/api/contentprod/user/phone_verify.php/{phone_number}")
    Call<SuccessStatus> getPhoneVerification(@Field("phone_number") String phoneNumber);

    @FormUrlEncoded
    @POST("/v1/api/contentprod/user/order_transaction_product.php/{id}/{product_id}/{order_id}/{order_qty}/{product_mrp}/{product_discount}")
    Call<SuccessStatus> postOrderProductData(@Field("user_id") String userId, @Field("product_id") String productId,
                                      @Field("order_id") String orderId, @Field("order_qty") String orderQty,
                                      @Field("product_mrp") String productMrp, @Field("product_discount") String productDiscount);



    @FormUrlEncoded
    @POST("/v1/api/contentprod/user/order_transaction.php/{id}/{razor_pay_order_id}/{razor_signature}/{razor_payment_id}/{status}")
    Call<SuccessStatus> postOrderData(@Field("user_id") String userId, @Field("razor_pay_order_id") String orderId,
                                      @Field("razor_signature") String signature, @Field("razor_payment_id") String paymentID,
                                      @Field("status") String orderStatus);


    @FormUrlEncoded
    @POST("v1/api/contentprod/user/get_order_list.php/{user_id}")
    Call<List<OrderStatus>> getOrderList(@Field("user_id") String userId);

}
