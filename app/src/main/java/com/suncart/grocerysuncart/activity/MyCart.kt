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
import com.dbflow5.config.FlowManager
import com.suncart.grocerysuncart.R
import com.suncart.grocerysuncart.adapter.BestDealRecyclerAdapter
import com.suncart.grocerysuncart.adapter.ShippingItemsAdapter
import com.suncart.grocerysuncart.bus.ContentLoadedEvent
import com.suncart.grocerysuncart.config.GroceryApp
import com.suncart.grocerysuncart.model.BestDealModel
import com.suncart.grocerysuncart.model.content.ContentItems
import com.suncart.grocerysuncart.service.ContentService
import com.suncart.grocerysuncart.util.DbUtils
import de.greenrobot.event.EventBus
import kotlinx.android.synthetic.main.checkout_layout.*

class MyCart : AppCompatActivity(){

    lateinit var recyclerBestDeal : RecyclerView
    lateinit var contentService : ContentService

    private lateinit var bestDealRecyclerAdapter: BestDealRecyclerAdapter

    var eventBus = EventBus.getDefault()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_cart_activity)
        FlowManager.init(this);

        //get content for deal
        contentService = ContentService(this)
        contentService.getAllNewsItems()

        val checkoutBtn = findViewById<RelativeLayout>(R.id.checkout_btn)
        recyclerBestDeal = findViewById(R.id.first_slide_best_deal)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        //toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val nav_icon  = supportActionBar?.customView?.findViewById<ImageView>(R.id.navigation_drawer)
        val cartImg = supportActionBar?.customView?.findViewById<ImageView>(R.id.cart_icons)
        val totalCart = supportActionBar?.customView?.findViewById<TextView>(R.id.total_cart)
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

        // get details about product in cart
        val productCartList = DbUtils.getDataCart()
        // cart product
        val productShipRecycler = findViewById<RecyclerView>(R.id.shipping_product_recycle)
        val bestDealRecyclerAdapter = ShippingItemsAdapter(this, productCartList)
        productShipRecycler.adapter = bestDealRecyclerAdapter
        productShipRecycler.layoutManager = LinearLayoutManager(this)
        productShipRecycler.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))


        if(!GroceryApp.isUserLogged(this)){
            checkout_title.text = "Login to Checkout"
        }else {
            checkout_title.text = "Checkout"
        }

        checkoutBtn.setOnClickListener {
            if (!GroceryApp.isUserLogged(this)){
                val intent = Intent(this, OTPLogin::class.java)
                startActivity(intent)
            }else {
                val intent = Intent(this, MapPickAddress::class.java)
                startActivity(intent)
            }
        }

    }

    private fun recyclerDeal(recyclerBestDeal: RecyclerView, bestDealRecyclerAdapter: BestDealRecyclerAdapter){
        recyclerBestDeal.adapter = bestDealRecyclerAdapter;
        recyclerBestDeal.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
    }

    fun onEvent(contentLoadedEvent: ContentLoadedEvent){
        if (contentLoadedEvent != null){
            val contentItems = mutableListOf<ContentItems>()
            contentItems.addAll(contentLoadedEvent.contentItemsList)

            bestDealRecyclerAdapter = BestDealRecyclerAdapter(this, contentItems);
            recyclerDeal(recyclerBestDeal, bestDealRecyclerAdapter);

        }
    }

    override fun onStart() {
        super.onStart()
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (eventBus.hasSubscriberForEvent(ContentService::class.java)) {
            eventBus.unregister(this)
        }
    }

}