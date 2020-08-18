package com.suncart.grocerysuncart.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.suncart.grocerysuncart.R
import com.suncart.grocerysuncart.adapter.BestDealRecyclerAdapter
import com.suncart.grocerysuncart.adapter.ShippingItemsAdapter
import com.suncart.grocerysuncart.config.GroceryApp
import com.suncart.grocerysuncart.model.BestDealModel
import com.suncart.grocerysuncart.util.DbUtils

class MyCart : AppCompatActivity(){

    lateinit var recyclerBestDeal : RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_cart_activity)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val checkoutBtn = findViewById<RelativeLayout>(R.id.checkout_btn)

        var nav_icon  = supportActionBar?.customView?.findViewById<ImageView>(R.id.navigation_drawer)
        var cartImg = supportActionBar?.customView?.findViewById<ImageView>(R.id.cart_icons)
        var totalCart = supportActionBar?.customView?.findViewById<TextView>(R.id.total_cart)
        val titleBar = supportActionBar?.customView?.findViewById<TextView>(R.id.title_appbar)
        titleBar?.setText("My Cart")
        titleBar?.setTextColor(Color.BLACK)

        nav_icon?.visibility = View.GONE
        cartImg?.visibility = View.GONE
        totalCart?.visibility = View.GONE

        toolbar.setBackgroundColor(Color.parseColor("#FF7B5FAE"))
        toolbar.setNavigationOnClickListener {
            super.onBackPressed()
        }

        var bestDealModel = mutableListOf<BestDealModel>()
        bestDealModel.add(BestDealModel(1, "Plastic Free grocery deliver fast", "Rs. 960",  "Rs. 1080", "102","7 kg" , "http://vipultest.nbwebsolution.com/images/fortune.jpg"))
        bestDealModel.add(BestDealModel(2, "Plastic Free grocery deliver fast", "Rs. 960",  "Rs. 1080", "102","7 kg" , "http://vipultest.nbwebsolution.com/images/fortune.jpg"))
        bestDealModel.add(BestDealModel(3, "Plastic Free grocery deliver fast", "Rs. 960",  "Rs. 1080", "102","7 kg" , "http://vipultest.nbwebsolution.com/images/fortune.jpg"))

        var productCartList = DbUtils.getDataCart()

        // cart product
        var productShipRecycler = findViewById<RecyclerView>(R.id.shipping_product_recycle)
        var bestDealRecyclerAdapter = ShippingItemsAdapter(this, productCartList)
        productShipRecycler.adapter = bestDealRecyclerAdapter
        productShipRecycler.layoutManager = LinearLayoutManager(this)
        productShipRecycler.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        //best deal
//        recyclerBestDeal = findViewById<RecyclerView>(R.id.first_slide_best_deal);
//        var bestDealRecyclerAdapter_2 = BestDealRecyclerAdapter(this, bestDealModel)
//        recyclerDeal(recyclerBestDeal, bestDealRecyclerAdapter_2);

        checkoutBtn.setOnClickListener {
            if (!GroceryApp.isUserLoged(this)){
                var intent = Intent(this, OTPLogin::class.java)
                startActivity(intent)
            }else {
                // var intent = Intent(this, )
            }
        }

    }

    private fun recyclerDeal(recyclerBestDeal: RecyclerView, bestDealRecyclerAdapter: BestDealRecyclerAdapter){
        recyclerBestDeal.adapter = bestDealRecyclerAdapter;
        recyclerBestDeal.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
    }

}