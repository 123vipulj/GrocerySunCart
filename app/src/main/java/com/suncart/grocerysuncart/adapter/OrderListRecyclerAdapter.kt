package com.suncart.grocerysuncart.adapter

import android.content.Context
import android.graphics.Color
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.suncart.grocerysuncart.R
import com.suncart.grocerysuncart.database.tables.ProductItems

class OrderListRecyclerAdapter(var context: Context, var bestDealModel: MutableList<ProductItems>) : RecyclerView.Adapter<OrderListRecyclerAdapter.MyViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(
            R.layout.order_list_layout,
            parent,
            false
        )
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        addShowMoreDots(bestDealModel[position].productName, holder.productTitle,35)
        holder.productPrice.text = "Rs. "+bestDealModel[position].productSp
        holder.productQty.text = "Qty : "+ bestDealModel[position].totalQty.toString()
        holder.productPrice.setTextColor(Color.parseColor("#0cc418"))
    }

    override fun getItemCount(): Int {
        return bestDealModel.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var productTitle = itemView.findViewById<TextView>(R.id.productTitle)
        var productQty = itemView.findViewById<TextView>(R.id.productQty)
        var productPrice = itemView.findViewById<TextView>(R.id.productPrice)
    }

    fun addShowMoreDots(targetString: String, tvStringHolder: TextView, charactersLimit: Int) {
        var targetString = targetString
        if (targetString.length > charactersLimit) {
            val dotsString = " ... "
            targetString = targetString.substring(0, charactersLimit) + dotsString
            val spannableDots = SpannableString(targetString)
            tvStringHolder.movementMethod = LinkMovementMethod.getInstance()
            tvStringHolder.setText(spannableDots, TextView.BufferType.SPANNABLE)
        } else {
            tvStringHolder.text = targetString
        }
    }
}