package com.suncart.grocerysuncart.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.suncart.grocerysuncart.R
import com.suncart.grocerysuncart.activity.StatusOrder
import com.suncart.grocerysuncart.model.content.ContentItems
import com.suncart.grocerysuncart.model.content.OrderStatus

/*
* TODO
*  1. order list recycler
* */
class OrderListRAdapter(var context: Context ,var orderStatusList: MutableList<OrderStatus> ) : RecyclerView.Adapter<OrderListRAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_list_sec_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.orderIdText.text = orderStatusList[position].razorpayOrderId
        holder.dateOrderText.text = orderStatusList[position].createdAt
        holder.statusOrderText.text = orderStatusList[position].orderStatus
        holder.orderStatusLay.setOnClickListener {
            var intent = Intent(context, StatusOrder::class.java)
            intent.putExtra("user_id", orderStatusList[position].id)
            intent.putExtra("total_price", orderStatusList[position].product_mrp)
            intent.putExtra("total_qty", orderStatusList[position].order_qty)
            context.startActivity(intent)
        }

        holder.ttlPriceHeader.text = "Total Price : "+ (orderStatusList[position].product_mrp.toFloat() - orderStatusList[position].product_discount.toFloat())
        holder.ttlQtyHeader.text = "Total Qty :: " + orderStatusList[position].order_qty
    }

    override fun getItemCount(): Int {
        return orderStatusList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var orderIdText = itemView.findViewById<TextView>(R.id.order_id)
        var dateOrderText = itemView.findViewById<TextView>(R.id.order_date)
        var statusOrderText = itemView.findViewById<TextView>(R.id.order_status)
        var orderStatusLay = itemView.findViewById<ConstraintLayout>(R.id.order_status_lay)
        var ttlPriceHeader = itemView.findViewById<TextView>(R.id.ttl_price_bottom_header)
        var ttlQtyHeader = itemView.findViewById<TextView>(R.id.ttl_qty_bottom_header)
    }

}