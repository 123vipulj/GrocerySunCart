package com.suncart.grocerysuncart.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.paytm.pg.merchant.PaytmChecksum;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultListener;
import com.razorpay.PaymentResultWithDataListener;
import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment;
import com.shreyaspatil.EasyUpiPayment.listener.PaymentStatusListener;
import com.shreyaspatil.EasyUpiPayment.model.PaymentApp;
import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails;
import com.suncart.grocerysuncart.R;
import com.suncart.grocerysuncart.adapter.ProductShippingPayment;
import com.suncart.grocerysuncart.adapter.ShippingItemsAdapter;
import com.suncart.grocerysuncart.bus.SuccessStatusLoadedEvent;
import com.suncart.grocerysuncart.config.GroceryApp;
import com.suncart.grocerysuncart.database.tables.ProductItems;
import com.suncart.grocerysuncart.database.tables.UserAddress;
import com.suncart.grocerysuncart.service.UserService;
import com.suncart.grocerysuncart.util.DbUtils;
import com.suncart.grocerysuncart.util.UtilApp;


import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.Security;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

import de.greenrobot.event.EventBus;

public class ProceedToPayment extends AppCompatActivity implements PaymentResultWithDataListener {
    private static final String TAG = ProceedToPayment.class.getSimpleName();
    Checkout checkout;

    Button button;
    TextView nameFieldTxt, addrFieldTxt,changeAddrTxt;
    EventBus eventBus = EventBus.getDefault();
    UserService userService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.procced_to_pay_activity);
        nameFieldTxt = findViewById(R.id.nameField);
        addrFieldTxt = findViewById(R.id.addrField);

        userService = new UserService(this);

        RadioButton razorPay = findViewById(R.id.razor_pay);
        RadioButton payOnDelivery = findViewById(R.id.cash_on_delivery);
        RadioButton payOnUpi = findViewById(R.id.upi_pay);

        String getChargeDetails = UtilApp.Companion.getTotalPriceOnView();

        TextView mrpTxt = findViewById(R.id.mrp_p);
        TextView discountTxt = findViewById(R.id.discount_p);
        TextView totalTxt = findViewById(R.id.ttl_c);

        mrpTxt.setText(getChargeDetails.split(" ")[0]);
        discountTxt.setText("-" + getChargeDetails.split(" ")[1]);
        totalTxt.setText(getChargeDetails.split(" ")[2]);
        //nVe7QSss8gMdYc5U1sLT2r5w secret
        //rzp_test_xvrBJAhHttawNV keyid

        Checkout.preload(this);

        //toolbar
        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ImageView nav_icon  = getSupportActionBar().getCustomView().findViewById(R.id.navigation_drawer);
        ImageView cartImg = getSupportActionBar().getCustomView().findViewById(R.id.cart_icons);
        TextView totalCart = getSupportActionBar().getCustomView().findViewById(R.id.total_cart);
        TextView titleBar = getSupportActionBar().getCustomView().findViewById(R.id.title_appbar);
        titleBar.setText("Review your Order");
        titleBar.setTextColor(Color.BLACK);

        nav_icon.setVisibility(View.GONE);
        cartImg.setVisibility(View.GONE);
        totalCart.setVisibility(View.GONE);

        toolbar.setBackgroundColor(Color.parseColor("#FF7B5FAE"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProceedToPayment.super.onBackPressed();
            }
        });

        // get details about product in cart
        List<ProductItems> productCartList = DbUtils.getDataCart();
        // cart product
        RecyclerView productShipRecycler = findViewById(R.id.shipping_product_recycle);
        ProductShippingPayment bestDealRecyclerAdapter = new ProductShippingPayment(this, productCartList);
        productShipRecycler.setAdapter(bestDealRecyclerAdapter);
        productShipRecycler.setLayoutManager(new LinearLayoutManager(this));
        productShipRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        //addr field
        List<UserAddress> userAddress = DbUtils.getRowSelectedDbAddress();
        if (userAddress.size() != 0){
            nameFieldTxt.setText(userAddress.get(0).userName);
            addrFieldTxt.setText(userAddress.get(0).userAddress);
        }

        button = findViewById(R.id.startPayment);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (razorPay.isChecked()){
                    try {
                        startPaymentRazorPay();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (payOnDelivery.isChecked()){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProceedToPayment.this);
                    alertDialog.setTitle("Messaeg");
                    alertDialog.setMessage("Do you want to Confirm Pay on Delivery ?");
                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(ProceedToPayment.this, OrderSuccess.class));
                            finish();
                        }
                    });

                    alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    alertDialog.show();
                } else if(payOnUpi.isChecked()){
                    UUID uniqueKey = UUID.randomUUID();

                    final EasyUpiPayment easyUpiPayment = new EasyUpiPayment.Builder()
                            .with(ProceedToPayment.this)
                            .setPayeeVpa("ekyodha@ybl")
                            .setPayeeName("vipul")
                            .setTransactionId(UtilApp.Companion.generatingRandomString())
                            .setTransactionRefId(UtilApp.Companion.generatingRandomString())
                            .setDescription("Grocery Product")
                            .setAmount("120.25")
                            .build();

                    easyUpiPayment.startPayment();
                    easyUpiPayment.setPaymentStatusListener(new PaymentStatusListener() {
                        @Override
                        public void onTransactionCompleted(TransactionDetails transactionDetails) {

                        }

                        @Override
                        public void onTransactionSuccess() {

                        }

                        @Override
                        public void onTransactionSubmitted() {

                        }

                        @Override
                        public void onTransactionFailed() {

                        }

                        @Override
                        public void onTransactionCancelled() {

                        }

                        @Override
                        public void onAppNotFound() {

                        }
                    });
                }
            }
        });

        changeAddrTxt  = findViewById(R.id.chng_add);
        changeAddrTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProceedToPayment.this, ChangeAddress.class));
                finish();
            }
        });

    }

    public void startPaymentRazorPay(){
        final Activity activity = this;
        final Checkout co = new Checkout();
        co.setKeyID("rzp_test_xvrBJAhHttawNV");

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Razorpay Corp");
            options.put("description", "Demoing Charges");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", "100");

            JSONObject preFill = new JSONObject();
            preFill.put("email", "test@razorpay.com");
            preFill.put("contact", "9876543210");

            options.put("prefill", preFill);

            co.open(activity, options);
        }catch (Exception e){
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }

    }


    public void startPaymentPaytm() throws Exception {

        PaytmPGService Service = PaytmPGService.getStagingService("");

        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("MID", "rxazcv89315285244163");
        params.put("ORDERID", "order1");

        String paytmChecksum = PaytmChecksum.generateSignature(params, "k&oh3#wV&aZp%frd");
        System.out.println("generateSignature Returns: " + paytmChecksum);

        HashMap<String, String> paramMap = new HashMap<String,String>();
        paramMap.put( "MID" , "rxazcv89315285244163");
        // Key in your staging and production MID available in your dashboard
        paramMap.put( "ORDER_ID" , "order1");
        paramMap.put( "CUST_ID" , "cust123");
        paramMap.put( "MOBILE_NO" , "7777777777");
        paramMap.put( "EMAIL" , "username@emailprovider.com");
        paramMap.put( "CHANNEL_ID" , "WAP");
        paramMap.put( "TXN_AMOUNT" , "100.12");
        paramMap.put( "WEBSITE" , "WEBSTAGING");
        // This is the staging value. Production value is available in your dashboard
        paramMap.put( "INDUSTRY_TYPE_ID" , "Retail");
        // This is the staging value. Production value is available in your dashboard
        paramMap.put( "CALLBACK_URL", "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=order1");
        paramMap.put( "CHECKSUMHASH" , paytmChecksum);
        PaytmOrder Order = new PaytmOrder(paramMap);

        Service.initialize(Order, null);

        Service.startPaymentTransaction(this, true, true,
                new PaytmPaymentTransactionCallback() {
            /*Call Backs*/
            public void someUIErrorOccurred(String inErrorMessage) {}
            public void onTransactionResponse(Bundle inResponse) {
                Toast.makeText(ProceedToPayment.this, "response :: " + inResponse, Toast.LENGTH_SHORT).show();
            }
            public void networkNotAvailable() {}
            public void clientAuthenticationFailed(String inErrorMessage) {
                Toast.makeText(ProceedToPayment.this, "error :: " + inErrorMessage, Toast.LENGTH_SHORT).show();
            }
            public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {}
            public void onBackPressedCancelTransaction() {}
            public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {}
        });
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID, PaymentData paymentData) {
        userService.postOrderData(GroceryApp.Companion.getUserId(ProceedToPayment.this),
                paymentData.getOrderId(),
                paymentData.getSignature(),
                paymentData.getPaymentId(),
                "In Progress");
        Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        eventBus.unregister(this);
    }

    public void onEvent(SuccessStatusLoadedEvent successStatusLoadedEvent){
        if (successStatusLoadedEvent != null){
            Toast.makeText(ProceedToPayment.this, successStatusLoadedEvent.successStatus.getMessage(), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, SuccessPayment.class));
            finish();
        }
    }


}
