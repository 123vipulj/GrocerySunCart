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
import com.suncart.grocerysuncart.model.CategoriesItems

public class CategoriesAdapter(var context: Context,var categoriesItems: MutableList<CategoriesItems>) : RecyclerView.Adapter<CategoriesAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoriesAdapter.MyViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.categories_lay, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categoriesItems.size
    }

    override fun onBindViewHolder(holder: CategoriesAdapter.MyViewHolder, position: Int) {
        Glide.with(context).load(categoriesItems.get(position).urlImg).into(holder.imgCat)
        holder.catTitle.text = categoriesItems.get(position).cat_title
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgCat = itemView.findViewById<ImageView>(R.id.cat_imgs)
        var catTitle = itemView.findViewById<TextView>(R.id.cate_names)
    }
}