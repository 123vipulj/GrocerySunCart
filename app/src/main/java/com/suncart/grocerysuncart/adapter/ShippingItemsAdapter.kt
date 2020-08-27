package com.suncart.grocerysuncart.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.suncart.grocerysuncart.R
import com.suncart.grocerysuncart.database.tables.ProductItems
import com.suncart.grocerysuncart.util.DbUtils

open class ShippingItemsAdapter(
    var context: Context,
    var bestDealModel: MutableList<ProductItems>
) : RecyclerView.Adapter<ShippingItemsAdapter.MyViewHolder>(){

    lateinit var cartNumListener : ShippingItemsAdapter.cartNumberListener

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShippingItemsAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.shipping_lay_reycler,
            parent,
            false
        )
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return bestDealModel.size
    }

    override fun onBindViewHolder(holder: ShippingItemsAdapter.MyViewHolder, position: Int) {
        Glide.with(context).load(bestDealModel[position].productPics).into(holder.productImg)
        holder.productMrp.text = bestDealModel[position].productMrp
        holder.productSp.text = bestDealModel[position].productSp
        holder.productTitle.text = bestDealModel[position].productName
        holder.productUnit.text = bestDealModel[position].productWeight
        holder.totalQty.text = bestDealModel[position].totalQty.toString()

        holder.addBtn?.setOnClickListener(View.OnClickListener { v: View? ->
            if (DbUtils.getTtlQtyByIds(position, bestDealModel) == 0L) {
                DbUtils.insertRowDb(position, 1, bestDealModel)
                holder.totalQty.text = DbUtils.getTtlQtyByIds(position, bestDealModel).toString()
                cartNumListener.setCurrentTotalQty(DbUtils.getTtlQty().toInt())

            } else if (DbUtils.getTtlQtyByIds(position, bestDealModel) > 0L) {
                DbUtils.insertRowDb(position, 1, bestDealModel)
                holder.totalQty.text = DbUtils.getTtlQtyByIds(position, bestDealModel).toString()
                cartNumListener.setCurrentTotalQty(DbUtils.getTtlQty().toInt())
            }

        })

        holder.minusBtn?.setOnClickListener(View.OnClickListener {
            if (DbUtils.getTtlQtyByIds(position, bestDealModel) == 0L) {
//                bestDealModel.removeAt(position)
//                notifyItemChanged(position)
                //DbUtils.insertRowDb(position, -1, bestDealModel)
            } else if (DbUtils.getTtlQtyByIds(position, bestDealModel) > 0L) {
                DbUtils.insertRowDb(position, -1, bestDealModel)
                var ttQtsByIds = DbUtils.getTtlQtyByIds(position, bestDealModel)
                holder.totalQty.text = ttQtsByIds.toString()
                if (ttQtsByIds == 0L) {
                    bestDealModel.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, itemCount - position);
                }
                cartNumListener.setCurrentTotalQty(DbUtils.getTtlQty().toInt())
            }
        })
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var productImg = itemView.findViewById<ImageView>(R.id.product_img)
        var productSp = itemView.findViewById<TextView>(R.id.sp_price)
        var productMrp = itemView.findViewById<TextView>(R.id.mrp_price)
        var productTitle  = itemView.findViewById<TextView>(R.id.product_title)
        var productUnit = itemView.findViewById<TextView>(R.id.product_weight)
        var totalQty = itemView.findViewById<TextView>(R.id.total_quantity)
        var minusBtn = itemView.findViewById<ImageView?>(R.id.minus_btn)
        var addBtn = itemView.findViewById<ImageView?>(R.id.plus_btn)
    }

    interface cartNumberListener{
        fun setCurrentTotalQty(ttlQty: Int)
    }

    public fun setCartNumberListener(cartNumberListener: cartNumberListener) {
        this.cartNumListener = cartNumberListener
    }


}