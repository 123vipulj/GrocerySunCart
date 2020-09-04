package com.suncart.grocerysuncart.activity

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.suncart.grocerysuncart.MainActivity
import com.suncart.grocerysuncart.R

class OrderSuccess : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.order_success)

        Handler().postDelayed(Runnable {
            startActivity(Intent(this, StatusOrder::class.java))
            finish()
        }, 3000)
    }

    fun getScreenWidth(): Int {
        return Resources.getSystem().displayMetrics.widthPixels
    }

    fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }
}