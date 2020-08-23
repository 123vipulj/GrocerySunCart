package com.suncart.grocerysuncart.database.tables;

import com.dbflow5.annotation.Column;
import com.dbflow5.annotation.Database;
import com.dbflow5.annotation.PrimaryKey;
import com.dbflow5.annotation.Table;
import com.suncart.grocerysuncart.database.AppDatabase;

@Table(database = AppDatabase.class)
public class UserOrder {

    @PrimaryKey(autoincrement = false)
    public long ids;

    @Column
    public String productTitle;

    @Column
    public float productMrp;

    @Column
    public float productDiscount;

    @Column
    public int productQty;

    @Column
    public String productWeight;

    @Column
    public String userAddress;
}
