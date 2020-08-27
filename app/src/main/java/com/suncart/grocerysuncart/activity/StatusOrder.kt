package com.suncart.grocerysuncart.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dbflow5.config.FlowManager
import com.suncart.grocerysuncart.R
import com.suncart.grocerysuncart.adapter.OrderListRecyclerAdapter
import com.suncart.grocerysuncart.util.DbUtils
import com.suncart.grocerysuncart.util.customview.ProgressCircleWithLine
import kotlinx.android.synthetic.main.status_order.*

class StatusOrder : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.status_order)
        FlowManager.init(this)
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
        titleBar?.text = "Order Status"
        titleBar?.setTextColor(Color.BLACK)

        nav_icon?.visibility = View.GONE
        cartImg?.visibility = View.GONE
        totalCart?.visibility = View.GONE

        toolbar.setBackgroundColor(Color.parseColor("#FF7B5FAE"))
        toolbar.setNavigationOnClickListener {
            super.onBackPressed()
        }

        val circleWithLine = findViewById<ProgressCircleWithLine>(R.id.circle_with_line)
        circleWithLine.circleInTopColor = "#18c721"
        circleWithLine.circleInTopLineColor = "#18c721"

        var orderListData = DbUtils.getDataCart()
        var orderListRecyclerView = findViewById<RecyclerView>(R.id.order_recycler)
        var orderListAdapter = OrderListRecyclerAdapter(this, orderListData)
        orderListRecyclerView.adapter = orderListAdapter
        orderListRecyclerView.layoutManager = LinearLayoutManager(this)
        orderListRecyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL).apply {
            ContextCompat.getDrawable(this@StatusOrder,R.drawable.dotted_shape)?.let {
                setDrawable(it)
            }
        })
    }
}