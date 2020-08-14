package com.suncart.grocerysuncart.config

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.suncart.grocerysuncart.util.SharedPrefsUtils

class GroceryApp : Application(){

    companion object {
        fun saveLoginNumber(context: Context, number : Int){
            SharedPrefsUtils.setIntegerPreference(context, "loginNumber", number)
        }

        fun saveLogin(context: Context, yesNo : Boolean){
            SharedPrefsUtils.setBooleanPreference(context, "isLogin", yesNo)
        }

        fun getLoginNumber(context: Context) : Int{
            return SharedPrefsUtils.getIntegerPreference(context, "loginNumber", 0)
        }

        fun isUserLoged(context: Context) : Boolean{
            return SharedPrefsUtils.getBooleanPreference(context, "isLogin", false)
        }
    }

}