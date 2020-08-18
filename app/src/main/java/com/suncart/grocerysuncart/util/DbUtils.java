package com.suncart.grocerysuncart.util;

import com.dbflow5.config.FlowManager;
import com.dbflow5.database.DatabaseWrapper;
import com.dbflow5.query.SQLite;
import com.dbflow5.transaction.ITransaction;
import com.suncart.grocerysuncart.config.GroceryApp;
import com.suncart.grocerysuncart.database.AppDatabase;
import com.suncart.grocerysuncart.database.tables.ProductItems;
import com.suncart.grocerysuncart.database.tables.ProductItems_Table;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.dbflow5.query.MethodKt.sum;

public class DbUtils extends GroceryApp {

    public static Long getDataForTrack(){
        return FlowManager.getDatabase(AppDatabase.class).executeTransaction(new ITransaction<Long>() {
            @Override
            public Long execute(@NotNull DatabaseWrapper databaseWrapper) {
                return SQLite.select(sum(ProductItems_Table.totalQty)).from(ProductItems.class).longValue(databaseWrapper);
            }
        });
    }

    public static List<ProductItems> getDataCart(){
       return FlowManager.getDatabase(AppDatabase.class).executeTransaction(new ITransaction<List<ProductItems>>() {
            @Override
            public List<ProductItems> execute(@NotNull DatabaseWrapper databaseWrapper) {
                return SQLite.select().from(ProductItems.class)
                        .where(ProductItems_Table.totalQty.greaterThan(0))
                        .queryList(databaseWrapper);
            }
        });

    }
}
