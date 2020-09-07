package com.suncart.grocerysuncart.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.suncart.grocerysuncart.R
import com.suncart.grocerysuncart.adapter.OrderListRAdapter
import com.suncart.grocerysuncart.bus.OrderListLoadedEvent
import com.suncart.grocerysuncart.config.GroceryApp
import com.suncart.grocerysuncart.service.ContentService
import com.suncart.grocerysuncart.service.UserService
import de.greenrobot.event.EventBus

class OrderList : AppCompatActivity() {

    lateinit var orderListRecyclerView : RecyclerView
    lateinit var orderListAdapter : OrderListRAdapter

    var eventBus = EventBus.getDefault()
    lateinit var userService : UserService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.order_list)

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
        titleBar?.setText("Your Orders")
        titleBar?.setTextColor(Color.BLACK)

        nav_icon?.visibility = View.GONE
        cartImg?.visibility = View.GONE
        totalCart?.visibility = View.GONE

        toolbar.setBackgroundColor(Color.parseColor("#FF7B5FAE"))
        toolbar.setNavigationOnClickListener {
            super.onBackPressed()
        }

        orderListRecyclerView = findViewById(R.id.order_list_recyclerView)
        userService = UserService(this)
        userService.getOrderDataList(GroceryApp.getUserId(applicationContext))
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

    fun onEvent(orderListLoadedEvent: OrderListLoadedEvent){
        if (orderListLoadedEvent != null){
            val oList = orderListLoadedEvent.orderStatusList
            orderListAdapter = OrderListRAdapter(this, oList)
            orderListRecyclerView.adapter = orderListAdapter
            orderListRecyclerView.layoutManager = LinearLayoutManager(this)
        }
    }
}