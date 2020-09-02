package com.suncart.grocerysuncart.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;
import com.suncart.grocerysuncart.MainActivity;
import com.suncart.grocerysuncart.R;
import com.suncart.grocerysuncart.api.TokenUpdaterApi;
import com.suncart.grocerysuncart.bus.SuccessStatusLoadedEvent;
import com.suncart.grocerysuncart.config.GroceryApp;
import com.suncart.grocerysuncart.service.ContentService;
import com.suncart.grocerysuncart.service.TokenUpdatatonService;
import com.suncart.grocerysuncart.service.UserService;

import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;

public class OTPAccept extends AppCompatActivity {

   FirebaseAuth mAuth;
   String token;
   String verificationId;
   TextView phonNumTxtView;
   String phoneNumber;
   ProgressDialog progress;

   EventBus eventBus = EventBus.getDefault();
   TokenUpdatatonService tokenUpdaterService;
   UserService userService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_accept_lay);
        mAuth = FirebaseAuth.getInstance();
        tokenUpdaterService = new TokenUpdatatonService(this);
        userService = new UserService(this);

        //progress bar
        progress = new ProgressDialog(this);
        progress.setTitle("");
        progress.setMessage("Wait while you are logged ...");
        progress.setCancelable(false);// disable dismiss by tapping outside of the dialog


        phoneNumber = getIntent().getStringExtra("phoneNumber");
        phonNumTxtView = findViewById(R.id.phoneNum);

        phonNumTxtView.setText(phoneNumber);

        OtpView otpView = findViewById(R.id.otp_view);

        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber.replace(" ", ""),
                60,
                TimeUnit.SECONDS,
                this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                       Toast.makeText(OTPAccept.this, "Verification Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verificationId = s;
                    }
                });

        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                progress.show();
                if (verificationId != null){
                    verifyPhoneNumberWithCode(otp);
                }
            }
        });

    }


    private void verifyPhoneNumberWithCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isComplete()){
                    userService.getPhoneVerficationStatus(phoneNumber);
                    GroceryApp.Companion.saveLoginNumber(OTPAccept.this, phoneNumber.replace(" ",""));
                    GroceryApp.Companion.saveLogin(OTPAccept.this, true);
                    navigateUpTo(new Intent(OTPAccept.this, MainActivity.class));
                    finish();
                }else {
                    Toast.makeText(
                            OTPAccept.this,
                    "Verification Failed !!! Recheck your OTP",
                            Toast.LENGTH_SHORT
                ).show();
                    progress.dismiss();
                }

            }

        });

    }

    public void onEvent(SuccessStatusLoadedEvent successStatusLoadedEvent){
        if (successStatusLoadedEvent != null){
            if(successStatusLoadedEvent.successStatus.getSuccess().equals("yes")){
                tokenUpdaterService.setUpdatedToken(verificationId);
            }else if (successStatusLoadedEvent.successStatus.getSuccess().equals("true")){
                Log.d(OTPAccept.class.getCanonicalName(), "Token Updated");
                GroceryApp.Companion.setTokenLocally(OTPAccept.this, verificationId);
            }
        }
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
        if (eventBus.hasSubscriberForEvent(ContentService.class)) {
            eventBus.unregister(this);
        }
    }

}