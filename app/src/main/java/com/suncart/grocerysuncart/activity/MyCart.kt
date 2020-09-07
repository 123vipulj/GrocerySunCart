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
import com.suncart.grocerysuncart.MainActivity
import com.suncart.grocerysuncart.R
import com.suncart.grocerysuncart.adapter.BestDealRecyclerAdapter
import com.suncart.grocerysuncart.adapter.ShippingItemsAdapter
import com.suncart.grocerysuncart.bus.ContentLoadedEvent
import com.suncart.grocerysuncart.config.GroceryApp
import com.suncart.grocerysuncart.database.tables.ProductItems
import com.suncart.grocerysuncart.model.content.ContentItems
import com.suncart.grocerysuncart.service.ContentService
import com.suncart.grocerysuncart.util.DbUtils
import de.greenrobot.event.EventBus
import kotlinx.android.synthetic.main.checkout_layout.*
import kotlinx.android.synthetic.main.my_cart_layout.*

class MyCart : AppCompatActivity(){

    lateinit var recyclerBestDeal : RecyclerView
    lateinit var contentService : ContentService

    private lateinit var bestDealRecyclerAdapter: BestDealRecyclerAdapter
    private lateinit var shipingItemRecyclerAdapter: ShippingItemsAdapter
    private lateinit var productShipRecycler : RecyclerView
    var eventBus = EventBus.getDefault()

    lateinit var productCartList : MutableList<ProductItems>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_cart_activity)
        FlowManager.init(this);

        //get content for deal
        contentService = ContentService(this)
        contentService.getAllContentItems()

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

        if (DbUtils.getTtlQty().toInt() == 0){
            not_item_cart.visibility = View.VISIBLE
            ship_lay.visibility = View.INVISIBLE
            total_ship_lay.visibility = View.INVISIBLE
            checkout_btn.visibility = View.INVISIBLE
        }else {
            not_item_cart.visibility = View.GONE
            ship_lay.visibility = View.VISIBLE
            total_ship_lay.visibility = View.VISIBLE
            checkout_btn.visibility = View.VISIBLE
        }

        // get details about product in cart
        productCartList = DbUtils.getDataCart()
        // cart product
        productShipRecycler = findViewById<RecyclerView>(R.id.shipping_product_recycle)
        shipingItemRecyclerAdapter = ShippingItemsAdapter(this, productCartList)
        productShipRecycler.adapter = shipingItemRecyclerAdapter
        productShipRecycler.layoutManager = LinearLayoutManager(this)
        productShipRecycler.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        shipingItemRecyclerAdapter.setCartNumberListener(object :
            ShippingItemsAdapter.CartNumberListener {
            override fun setCurrentTotalQty(ttlQty: Int) {
                if (ttlQty == 0) {
                    not_item_cart.visibility = View.VISIBLE
                    ship_lay.visibility = View.INVISIBLE
                    total_ship_lay.visibility = View.INVISIBLE
                    checkout_btn.visibility = View.INVISIBLE
                } else {
                    not_item_cart.visibility = View.GONE
                    ship_lay.visibility = View.VISIBLE
                    total_ship_lay.visibility = View.VISIBLE
                    checkout_btn.visibility = View.VISIBLE
                }
            }

        })

        shipingItemRecyclerAdapter.setTriggerPriceFluctuation(object : ShippingItemsAdapter.TriggerPriceFluctuation {
            override fun setPriceFluctuationHappen(isFlucationHappen: Boolean) {
                if (isFlucationHappen){
                    var priceFluctuationList = DbUtils.getDataCart()
                    var mrpPrice = priceFluctuationList.sumByDouble { it.productMrp.toDouble() * it.totalQty}
                    var discountPrice = priceFluctuationList.sumByDouble {it.productMrp.toDouble() * it.totalQty * (it.discountProduct.toDouble()/100)}
                    var totalPrice = mrpPrice - String.format("%.2f", discountPrice).toDouble()
                    p_mrp.text = "Rs." + mrpPrice.toString()
                    p_discount.text = "-Rs." + String.format("%.2f", discountPrice).toString()
                    p_total.text = "Rs." +totalPrice.toString()
                    p_checkout_price.text = "Rs." +totalPrice.toString()
                }
            }
        })

        priceShow()

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
                val intent = Intent(this, ChangeAddress::class.java)
                startActivity(intent)
            }
        }

        go_to_home.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

    }

    private fun recyclerDeal(
        recyclerBestDeal: RecyclerView,
        bestDealRecyclerAdapter: BestDealRecyclerAdapter
    ){
        recyclerBestDeal.adapter = bestDealRecyclerAdapter;
        recyclerBestDeal.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        );
    }

    fun onEvent(contentLoadedEvent: ContentLoadedEvent){
        if (contentLoadedEvent != null){
            val contentItems = mutableListOf<ContentItems>()
            contentItems.addAll(contentLoadedEvent.contentItemsList)

            bestDealRecyclerAdapter = BestDealRecyclerAdapter(this, contentItems)
            recyclerDeal(recyclerBestDeal, bestDealRecyclerAdapter)
            bestDealRecyclerAdapter.setCartTrackListener { currentQty ->
                if (currentQty?.toInt()  == 0){
                    not_item_cart.visibility = View.VISIBLE
                    ship_lay.visibility = View.INVISIBLE
                    total_ship_lay.visibility = View.INVISIBLE
                    checkout_btn.visibility = View.INVISIBLE
                }else {
                    not_item_cart.visibility = View.GONE
                    ship_lay.visibility = View.VISIBLE
                    total_ship_lay.visibility = View.VISIBLE
                    checkout_btn.visibility = View.VISIBLE
                    productCartList = DbUtils.getDataCart()
                    shipingItemRecyclerAdapter.setItems(productCartList)
                }
            }

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

    fun priceShow(){
        var priceFluctuationList = DbUtils.getDataCart()
        var mrpPrice = priceFluctuationList.sumByDouble { it.productMrp.toDouble() * it.totalQty}
        var discountPrice = priceFluctuationList.sumByDouble {it.productMrp.toDouble() * it.totalQty * (it.discountProduct.toDouble() /100)}
        var totalPrice = mrpPrice - String.format("%.2f", discountPrice).toDouble()
        p_mrp.text = "Rs." + mrpPrice.toString()
        p_discount.text = "-Rs." + String.format("%.2f", discountPrice).toString()
        p_total.text = "Rs." +totalPrice.toString()
        p_checkout_price.text = "Rs." +totalPrice.toString()
    }

}