package com.suncart.grocerysuncart.activity

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.suncart.grocerysuncart.R
import kotlinx.android.synthetic.main.payment_success.*

class SuccessPayment : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.payment_success)

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