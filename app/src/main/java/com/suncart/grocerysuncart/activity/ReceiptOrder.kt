package com.suncart.grocerysuncart.activity

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.suncart.grocerysuncart.R
import com.suncart.grocerysuncart.adapter.OrderListRecyclerAdapter
import com.suncart.grocerysuncart.bus.OrderReceiptLoadedEvent
import com.suncart.grocerysuncart.service.ContentService
import com.suncart.grocerysuncart.service.UserService
import de.greenrobot.event.EventBus

class ReceiptOrder : AppCompatActivity(){

    var eventBus = EventBus.getDefault()
    lateinit var userService : UserService
    lateinit var orderListRecyclerView : RecyclerView

    override fun setContentView(view: View?) {
        super.setContentView(view)
        setContentView(R.layout.receipt_layout)
        orderListRecyclerView = findViewById<RecyclerView>(R.id.order_recycler)
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

    fun onEvent(orderListLoadedEvent: OrderReceiptLoadedEvent){
        if (orderListLoadedEvent != null){
//            lottie_anim.visibility = View.GONE
            orderListRecyclerView.visibility = View.VISIBLE

            val oList = orderListLoadedEvent.orderStatusReceiptList

            var orderListAdapter = OrderListRecyclerAdapter(this, oList)
            orderListRecyclerView.adapter = orderListAdapter
            orderListRecyclerView.layoutManager = LinearLayoutManager(this)
            orderListRecyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL).apply {
                ContextCompat.getDrawable(this@ReceiptOrder,R.drawable.dotted_shape)?.let {
                    setDrawable(it)
                }
            })
        }
    }
}