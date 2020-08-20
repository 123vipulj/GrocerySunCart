package com.suncart.grocerysuncart.config

import android.app.Application
import android.content.Context
import com.dbflow5.config.DatabaseConfig
import com.dbflow5.config.FlowConfig
import com.dbflow5.config.FlowManager
import com.dbflow5.database.AndroidSQLiteOpenHelper
import com.suncart.grocerysuncart.api.ContentApi
import com.suncart.grocerysuncart.database.AppDatabase
import com.suncart.grocerysuncart.util.SharedPrefsUtils
import org.acra.ACRA
import org.acra.BuildConfig
import org.acra.annotation.AcraCore
import org.acra.annotation.AcraMailSender

@AcraMailSender(mailTo = "123.vipulj@gmail.com")
@AcraCore(buildConfigClass = BuildConfig::class)
open class GroceryApp : Application(){

    companion object {
        fun saveLoginNumber(context: Context, number : String){
            SharedPrefsUtils.setStringPreference(context, "loginNumber", number)
        }

        fun saveLogin(context: Context, yesNo : Boolean){
            SharedPrefsUtils.setBooleanPreference(context, "isLogin", yesNo)
        }

        fun getLoginNumber(context: Context) : String? {
            return SharedPrefsUtils.getStringPreference(context, "loginNumber")
        }

        fun isUserLogged(context: Context) : Boolean{
            return SharedPrefsUtils.getBooleanPreference(context, "isLogin", false)
        }

        fun  getNewsApi() : ContentApi {
           return APIClient.getClient().create(ContentApi::class.java)
        }
    }

    override fun onCreate() {
        super.onCreate()
        FlowManager.init(
            FlowConfig.Builder(this)
                .database(
                    DatabaseConfig.builder(AppDatabase::class, AndroidSQLiteOpenHelper.createHelperCreator(this))
                        .databaseName("GroceryDb")
                        .build())
                .build());
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        // acra initialization
        ACRA.init(this)
    }


}