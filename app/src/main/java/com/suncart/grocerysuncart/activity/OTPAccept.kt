package com.suncart.grocerysuncart.activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.mukesh.OnOtpCompletionListener
import com.mukesh.OtpView
import com.suncart.grocerysuncart.MainActivity
import com.suncart.grocerysuncart.R
import java.util.concurrent.TimeUnit

class OTPAccept : AppCompatActivity(){

    lateinit var mAuth : FirebaseAuth
    lateinit var token : String
    lateinit var verificationId : String
    lateinit var phonNumTxtView : TextView
    lateinit var phoneNumber : String
    lateinit var progress : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.otp_accept_lay)
        mAuth = FirebaseAuth.getInstance()

        //progress bar
        progress = ProgressDialog(this)
        progress.setTitle("")
        progress.setMessage("Wait while you are logged ...")
        progress.setCancelable(false) // disable dismiss by tapping outside of the dialog


        phoneNumber = intent?.getBundleExtra("phoneNumber").toString()
        phonNumTxtView = findViewById(R.id.phoneNum)

        phonNumTxtView.text = phoneNumber

        val otpView = findViewById<OtpView>(R.id.otp_view)

        PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + phoneNumber,
            60,
            TimeUnit.SECONDS,
            this,
            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {

                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    Toast.makeText(this@OTPAccept, "Verification Failed", Toast.LENGTH_SHORT).show()
                }

                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(p0, p1)
                    verificationId = p0
                }

            }
        )
        otpView.setOtpCompletionListener {
            OnOtpCompletionListener { otp ->
                progress.show()
                verifyPhoneNumberWithCode(otp)
            }
        }
    }

    private fun verifyPhoneNumberWithCode(code: String?) {
        val credential = PhoneAuthProvider.getCredential(
            verificationId,
            code!!
        )
        mAuth.signInWithCredential(credential).addOnCompleteListener(
            this@OTPAccept
        ) { task ->
            if (task.isComplete) {
                navigateUpTo(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(
                    this@OTPAccept,
                    "Verification Failed !!! Recheck your OTP",
                    Toast.LENGTH_SHORT
                ).show()
                progress.dismiss()
            }
        }
    }
}