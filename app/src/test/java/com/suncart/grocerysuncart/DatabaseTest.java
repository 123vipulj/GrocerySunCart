package com.suncart.grocerysuncart;

import com.dbflow5.adapter.ModelAdapter;
import com.dbflow5.config.FlowManager;
import com.dbflow5.query.SQLite;
import com.suncart.grocerysuncart.database.AppDatabase;
import com.suncart.grocerysuncart.database.tables.ProductItems;

import org.junit.Test;

import java.util.Date;
import java.util.List;

public class DatabaseTest {


    @Test
    public void size_isCorrect(){
        List<ProductItems> productItems = SQLite.select().
                from(ProductItems.class).
                queryList(FlowManager.getDatabase(AppDatabase.class));
        assert productItems.size() > 0;
    }

    @Test
    public void insert_isCorrect(){
        Date date = new Date();
        ProductItems productItems = new ProductItems();
        productItems.ids = date.getTime();
        productItems.productName = "fdsafaf";
        productItems.productMrp = "dsafa";
        productItems.productSp = "fsdfa";
        productItems.productWeight = "fdaf";
        productItems.totalQty = 12;

        ModelAdapter<ProductItems> productItemsModelAdapter = FlowManager.getModelAdapter(ProductItems.class);
        productItemsModelAdapter.insert(productItems, FlowManager.getDatabase(AppDatabase.class));
    }
}
