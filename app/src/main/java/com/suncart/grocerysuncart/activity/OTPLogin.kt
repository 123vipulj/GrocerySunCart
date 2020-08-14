package com.suncart.grocerysuncart.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.asdev.phoneedittext.PhoneEditText
import com.suncart.grocerysuncart.R

class OTPLogin : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.otp_login_lay)

        val phoneEditText = findViewById<PhoneEditText>(R.id.phoneEditText)
        val nextBtn = findViewById<Button>(R.id.nextBtn)

        nextBtn.setOnClickListener {
            if (!phoneEditText.text?.isEmpty()!!){
                val intent = Intent(this, OTPAccept::class.java)
                intent.putExtra("phoneNumber", phoneEditText.text.toString())
                startActivity(intent)
            }
        }
    }
}