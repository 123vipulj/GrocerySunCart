package com.suncart.grocerysuncart.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dbflow5.config.FlowManager;
import com.dbflow5.database.DatabaseWrapper;
import com.dbflow5.query.SQLite;
import com.dbflow5.transaction.ITransaction;
import com.suncart.grocerysuncart.R;
import com.suncart.grocerysuncart.activity.ProductDetails;
import com.suncart.grocerysuncart.database.AppDatabase;
import com.suncart.grocerysuncart.database.tables.ProductItems;
import com.suncart.grocerysuncart.database.tables.ProductItems_Table;
import com.suncart.grocerysuncart.model.BestDealModel;
import com.suncart.grocerysuncart.model.content.ContentItems;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.dbflow5.query.MethodKt.sum;

public class CategoriesRecyclerAdapter extends RecyclerView.Adapter<CategoriesRecyclerAdapter.MyViewHolder> {

    Context context;
    List<ContentItems> bestDealModelList;
    CartTrack cartTrackListener = null;
    int _countProductCart = 0;
    AtomicInteger countQty = new AtomicInteger();

    public CategoriesRecyclerAdapter(Context context, List<ContentItems> bestDealModelList) {
        this.context = context;
        this.bestDealModelList = bestDealModelList;
    }

    @Override
    public void onViewAttachedToWindow(@NonNull MyViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        countQty.set(Integer.parseInt(getTtlQty().toString()));
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_adapter_lay, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        AtomicInteger ttQty = new AtomicInteger();

        Glide.with(context).load(bestDealModelList.get(position).getProductPics()).into(holder.productImg);
        addShowMoreDots(bestDealModelList.get(position).getProductName(), holder.productTitle,50);
        // holder.productTitle.setText(bestDealModelList.get(position).getProductName());
        holder.productMrp.setText("Rs." + bestDealModelList.get(position).getProductMrp());
        holder.productMrp.setPaintFlags(holder.productMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.productSp.setText("Rs." + bestDealModelList.get(position).getProductSp());
        holder.productUnit.setText(bestDealModelList.get(position).getProductWeight());

        holder.productImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetails.class);
                context.startActivity(intent);
            }
        });
        holder.addBtn.setOnClickListener(v -> {
            //countQty.incrementAndGet();
            ttQty.incrementAndGet();
            if (ttQty.get() == 0){
                insertRowDb(position, 1);
                holder.addTxt.setVisibility(View.VISIBLE);
                holder.totalQty.setVisibility(View.GONE);

            }else if( ttQty.get() > 0){
                insertRowDb(position, 1);
                holder.addTxt.setVisibility(View.GONE);
                holder.totalQty.setVisibility(View.VISIBLE);
                holder.totalQty.setText(getTtlQty()+"");

            }
            // cartTrackListener.setCurrentQty(String.valueOf(ttQty));

        });

        holder.minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                countQty.decrementAndGet();
//                int ttQty = countQty.get();
                ttQty.decrementAndGet();

                if (ttQty.get() == 0){
                    holder.addTxt.setVisibility(View.VISIBLE);
                    holder.totalQty.setVisibility(View.GONE);
                   // insertRowDb(position, -1);
                }else if( ttQty.get() > 0){
                    insertRowDb(position, -1);
                    holder.addTxt.setVisibility(View.GONE);
                    holder.totalQty.setVisibility(View.VISIBLE);
                    holder.totalQty.setText(getTtlQty()+"");
                }
                // cartTrackListener.setCurrentQty(String.valueOf(ttQty));

            }
        });
    }

    @Override
    public int getItemCount() {
        return bestDealModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView productImg,addBtn,minusBtn;
        TextView productSp, productMrp, productTitle, productUnit, totalQty, addTxt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            productImg = itemView.findViewById(R.id.product_img);
            productSp = itemView.findViewById(R.id.product_sp);
            productMrp = itemView.findViewById(R.id.product_mrp);
            productTitle = itemView.findViewById(R.id.product_title);
            productUnit = itemView.findViewById(R.id.product_unit);
            totalQty = itemView.findViewById(R.id.ttl_qty);
            addTxt  = itemView.findViewById(R.id.add_txt);
            addBtn = itemView.findViewById(R.id.add_btn);
            minusBtn = itemView.findViewById(R.id.minus_btn);
        }
    }

    public void setCartTrackListener(CartTrack cartTrackListener){
        this.cartTrackListener = cartTrackListener;
    }

    public interface CartTrack {

        void setCurrentQty(String currentQty);
    }

    public void insertRowDb(int pos, int ttQty){
        FlowManager.getDatabase(AppDatabase.class).executeTransaction(new ITransaction<Object>() {
            @Override
            public Object execute(@NotNull DatabaseWrapper databaseWrapper) {
                List<ProductItems> productItemsList = SQLite.select()
                        .from(ProductItems.class)
                        .where(ProductItems_Table.ids.eq((long) bestDealModelList.get(pos).getId()))
                        .queryList(databaseWrapper);


                if (productItemsList.size() > 0){
                    SQLite.update(ProductItems.class)
                            .set(ProductItems_Table.totalQty.eq((int) (getTtlQtyByIds(pos) + ttQty)))
                            .where(ProductItems_Table.productName.eq(bestDealModelList.get(pos).getProductName()))
                            .execute(databaseWrapper);

                    setTtlQty(pos);

                }else {
                    Date date = new Date();
                    ProductItems productItems = new ProductItems();
                    productItems.ids = bestDealModelList.get(pos).getId();
                    productItems.productPics = bestDealModelList.get(pos).getProductPics();
                    productItems.productName = bestDealModelList.get(pos).getProductName();
                    productItems.productMrp = bestDealModelList.get(pos).getProductMrp();
                    productItems.productSp = bestDealModelList.get(pos).getProductSp();
                    productItems.productWeight = bestDealModelList.get(pos).getProductWeight();
                    productItems.totalQty = 0;
                    productItems.insert(databaseWrapper);
                    setTtlQty(pos);
                }
                return null;
            }
        });


//        ModelAdapter<ProductItems>  productItemsModelAdapter = FlowManager.getModelAdapter(ProductItems.class);
//        productItemsModelAdapter.insert(productItems, FlowManager.getDatabase(AppDatabase.class));
    }

    // set value to cart value
    public void setTtlQty(int pos){
        Long totalQty = FlowManager.getDatabase(AppDatabase.class).executeTransaction(new ITransaction<Long>() {
            @Override
            public Long execute(@NotNull DatabaseWrapper databaseWrapper) {
                return SQLite.select(sum(ProductItems_Table.totalQty)).from(ProductItems.class)
                        .longValue(databaseWrapper);
            }
        });
        if (cartTrackListener != null){
            cartTrackListener.setCurrentQty(String.valueOf(totalQty));
        }

    }

    public Long getTtlQty(){
        return FlowManager.getDatabase(AppDatabase.class).executeTransaction(new ITransaction<Long>() {
            @Override
            public Long execute(@NotNull DatabaseWrapper databaseWrapper) {
                return SQLite.select(sum(ProductItems_Table.totalQty)).from(ProductItems.class)
                        .longValue(databaseWrapper);
            }
        });
    }

    public Long getTtlQtyByIds(int pos){
        return FlowManager.getDatabase(AppDatabase.class).executeTransaction(new ITransaction<Long>() {
            @Override
            public Long execute(@NotNull DatabaseWrapper databaseWrapper) {
                return SQLite.select(sum(ProductItems_Table.totalQty)).from(ProductItems.class)
                        .where(ProductItems_Table.ids.eq((long) bestDealModelList.get(pos).getId())).longValue(databaseWrapper);
            }
        });
    }

    public static void addShowMoreDots(String targetString, TextView tvStringHolder, int charactersLimit) {

        if (targetString.length() > charactersLimit) {
            String dotsString = " ... ";
            targetString = targetString.substring(0, charactersLimit).concat(dotsString);
            SpannableString spannableDots = new SpannableString(targetString);

            tvStringHolder.setMovementMethod(LinkMovementMethod.getInstance());
            tvStringHolder.setText(spannableDots, TextView.BufferType.SPANNABLE);
        } else {
            tvStringHolder.setText(targetString);
        }
    }
}
