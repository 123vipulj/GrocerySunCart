package com.suncart.grocerysuncart.activity

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mukesh.OtpView
import com.suncart.grocerysuncart.R

class OTPAccept : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.otp_accept_lay)

        val phoneNumber = intent.getBundleExtra("phoneNumber")
        val phonNumTxt = findViewById<TextView>(R.id.phoneNum)

        phonNumTxt.text = phoneNumber?.toString()

        val otpView = findViewById<OtpView>(R.id.otp_view)
        otpView.setOtpCompletionListener {
            
        }
    }
}