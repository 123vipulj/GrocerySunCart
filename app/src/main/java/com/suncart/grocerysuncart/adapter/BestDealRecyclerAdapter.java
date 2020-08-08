package com.suncart.grocerysuncart.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.suncart.grocerysuncart.R;
import com.suncart.grocerysuncart.model.BestDealModel;

import java.util.List;

public class BestDealRecyclerAdapter extends RecyclerView.Adapter<BestDealRecyclerAdapter.MyViewHolder> {

    Context context;
    List<BestDealModel> bestDealModelList;

    public BestDealRecyclerAdapter(Context context, List<BestDealModel> bestDealModelList) {
        this.context = context;
        this.bestDealModelList = bestDealModelList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card_items_lay, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(bestDealModelList.get(position).getProductPics()).into(holder.productImg);
        holder.productTitle.setText(bestDealModelList.get(position).getProductName());
        holder.productMrp.setText(bestDealModelList.get(position).getProductMRP());
        holder.productMrp.setPaintFlags(holder.productMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.productSp.setText(bestDealModelList.get(position).getProductSP());
        holder.productUnit.setText(bestDealModelList.get(position).getProductWeight());
    }

    @Override
    public int getItemCount() {
        return bestDealModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView productImg;
        TextView productSp, productMrp, productTitle, productUnit;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            productImg = itemView.findViewById(R.id.product_img);
            productSp = itemView.findViewById(R.id.product_sp);
            productMrp = itemView.findViewById(R.id.product_mrp);
            productTitle = itemView.findViewById(R.id.product_title);
            productUnit = itemView.findViewById(R.id.product_unit);
        }
    }
}
