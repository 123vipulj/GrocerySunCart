package com.suncart.grocerysuncart.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.suncart.grocerysuncart.R
import com.suncart.grocerysuncart.adapter.OrderListRAdapter
import com.suncart.grocerysuncart.bus.OrderListLoadedEvent
import com.suncart.grocerysuncart.service.ContentService
import de.greenrobot.event.EventBus

class OrderList : AppCompatActivity() {

    lateinit var orderListRecyclerView : RecyclerView
    lateinit var orderListAdapter : OrderListRAdapter

    var eventBus = EventBus.getDefault()
    lateinit var contentService : ContentService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.order_list)
        orderListRecyclerView = findViewById(R.id.order_list_recyclerView)
        contentService = ContentService(this)
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