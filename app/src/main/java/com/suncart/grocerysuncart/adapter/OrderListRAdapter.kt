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

class OrderListRAdapter(var context: Context ,var orderStatusList: MutableList<OrderStatus> ) : RecyclerView.Adapter<OrderListRAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_list_sec_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.orderIdText.text = orderStatusList.get(position).orderId
        holder.dateOrderText.text = orderStatusList.get(position).orderDate
        holder.statusOrderText.text = orderStatusList.get(position).orderStatus
        holder.orderStatusLay.setOnClickListener {
            var intent = Intent(context, StatusOrder::class.java)
            intent.putExtra("order_id", orderStatusList.get(position).orderId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return orderStatusList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var orderIdText = itemView.findViewById<TextView>(R.id.order_id)
        var dateOrderText = itemView.findViewById<TextView>(R.id.order_date)
        var statusOrderText = itemView.findViewById<TextView>(R.id.order_status)
        var orderStatusLay = itemView.findViewById<ConstraintLayout>(R.id.order_status_lay)
    }
}