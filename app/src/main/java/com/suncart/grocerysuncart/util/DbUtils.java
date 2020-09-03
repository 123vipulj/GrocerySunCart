package com.suncart.grocerysuncart.util;

import com.dbflow5.config.FlowManager;
import com.dbflow5.database.DatabaseWrapper;
import com.dbflow5.query.SQLite;
import com.dbflow5.transaction.ITransaction;
import com.suncart.grocerysuncart.config.GroceryApp;
import com.suncart.grocerysuncart.database.AppDatabase;
import com.suncart.grocerysuncart.database.tables.ProductItems;
import com.suncart.grocerysuncart.database.tables.ProductItems_Table;
import com.suncart.grocerysuncart.database.tables.UserAddress;
import com.suncart.grocerysuncart.database.tables.UserAddress_Table;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
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

    public static Boolean insertRowInDbAddress(String name, String addr, String exactLoc){
        return FlowManager.getDatabase(AppDatabase.class).executeTransaction(new ITransaction<Boolean>() {
            @Override
            public Boolean execute(@NotNull DatabaseWrapper databaseWrapper) {
                UserAddress userAddress = new UserAddress();
                userAddress.ids = System.currentTimeMillis();
                userAddress.userName = name;
                userAddress.userAddress = addr;
                userAddress.exactLoc = exactLoc;
                userAddress.isInUse = 0;
                userAddress.insert(databaseWrapper);
                return true;
            }
        });
    }

    public static List<UserAddress> getRowInDbAddress(){
        return FlowManager.getDatabase(AppDatabase.class).executeTransaction(new ITransaction<List<UserAddress>>() {
            @Override
            public List<UserAddress> execute(@NotNull DatabaseWrapper databaseWrapper) {
                return SQLite.select()
                        .from(UserAddress.class)
                        .queryList(databaseWrapper);
            }
        });
    }

    public static List<UserAddress> getRowSelectedDbAddress(){
        return FlowManager.getDatabase(AppDatabase.class).executeTransaction(new ITransaction<List<UserAddress>>() {
            @Override
            public List<UserAddress> execute(@NotNull DatabaseWrapper databaseWrapper) {
                return SQLite.select(UserAddress_Table.userName,UserAddress_Table.userAddress)
                        .from(UserAddress.class)
                        .where(UserAddress_Table.isInUse.eq(1))
                        .queryList(databaseWrapper);
            }
        });
    }

    public static void selectAddress(Long ids){
         FlowManager.getDatabase(AppDatabase.class).executeTransaction(new ITransaction<Object>() {
            @Override
            public Object execute(@NotNull DatabaseWrapper databaseWrapper) {
                SQLite.update(UserAddress.class).set(UserAddress_Table.isInUse.eq(0)).execute(databaseWrapper);
                SQLite.update(UserAddress.class).set(UserAddress_Table.isInUse.eq(1)).where(UserAddress_Table.ids.eq(ids)).execute(databaseWrapper);
                return null;
            }
        });
    }

    // set value to cart value
    public static void setTtlQty(){
        Long totalQty = FlowManager.getDatabase(AppDatabase.class).executeTransaction(new ITransaction<Long>() {
            @Override
            public Long execute(@NotNull DatabaseWrapper databaseWrapper) {
                return SQLite.select(sum(ProductItems_Table.totalQty)).from(ProductItems.class)
                        .longValue(databaseWrapper);
            }
        });

    }

    public static Long getTtlQty(){
        return FlowManager.getDatabase(AppDatabase.class).executeTransaction(new ITransaction<Long>() {
            @Override
            public Long execute(@NotNull DatabaseWrapper databaseWrapper) {
                return SQLite.select(sum(ProductItems_Table.totalQty)).from(ProductItems.class)
                        .longValue(databaseWrapper);
            }
        });
    }

    public static void insertRowDb(int pos, int ttQty, List<ProductItems> bestDealModelList){
        FlowManager.getDatabase(AppDatabase.class).executeTransaction(new ITransaction<Object>() {
            @Override
            public Object execute(@NotNull DatabaseWrapper databaseWrapper) {
                List<ProductItems> productItemsList = SQLite.select()
                        .from(ProductItems.class)
                        .where(ProductItems_Table.ids.eq((long) bestDealModelList.get(pos).ids))
                        .queryList(databaseWrapper);


                if (productItemsList.size() > 0){
                    SQLite.update(ProductItems.class)
                            .set(ProductItems_Table.totalQty.eq((int) (getTtlQtyByIds(pos, bestDealModelList) + ttQty)))
                            .where(ProductItems_Table.ids.eq(bestDealModelList.get(pos).ids))
                            .execute(databaseWrapper);


                }else {
                    Date date = new Date();
                    ProductItems productItems = new ProductItems();
                    productItems.ids = bestDealModelList.get(pos).ids;
                    productItems.productPics = bestDealModelList.get(pos).productPics;
                    productItems.productName = bestDealModelList.get(pos).productName;
                    productItems.productMrp = bestDealModelList.get(pos).productMrp;
                    productItems.productSp = bestDealModelList.get(pos).productSp;
                    productItems.productWeight = bestDealModelList.get(pos).productWeight;
                    productItems.totalQty = 0;
                    productItems.insert(databaseWrapper);

                }
                return null;
            }
        });

    }

    public static void insertRowDb(long ids, int ttQty){
        FlowManager.getDatabase(AppDatabase.class).executeTransaction(new ITransaction<Object>() {
            @Override
            public Object execute(@NotNull DatabaseWrapper databaseWrapper) {
                List<ProductItems> productItemsList = SQLite.select()
                        .from(ProductItems.class)
                        .where(ProductItems_Table.ids.eq(ids))
                        .queryList(databaseWrapper);


                if (productItemsList.size() > 0){
                    SQLite.update(ProductItems.class)
                            .set(ProductItems_Table.totalQty.eq((int) (getTtlQtyByIds(ids) + ttQty)))
                            .where(ProductItems_Table.ids.eq(ids))
                            .execute(databaseWrapper);
                }
                return null;
            }
        });

    }

    public static Long getTtlQtyByIds(int pos, List<ProductItems> bestDealModelList){
        return FlowManager.getDatabase(AppDatabase.class).executeTransaction(new ITransaction<Long>() {
            @Override
            public Long execute(@NotNull DatabaseWrapper databaseWrapper) {
                if (bestDealModelList.size() != 0 && bestDealModelList.size() > pos){
                    return SQLite.select(sum(ProductItems_Table.totalQty)).from(ProductItems.class)
                            .where(ProductItems_Table.ids.eq( bestDealModelList.get(pos).ids)).longValue(databaseWrapper);
                }
                return 0L;
            }
        });
    }

    public static Long getTtlQtyByIds(long id){
        return FlowManager.getDatabase(AppDatabase.class).executeTransaction(new ITransaction<Long>() {
            @Override
            public Long execute(@NotNull DatabaseWrapper databaseWrapper) {
                    return SQLite.select(sum(ProductItems_Table.totalQty)).from(ProductItems.class)
                            .where(ProductItems_Table.ids.eq(id)).longValue(databaseWrapper);
            }
        });
    }
}
