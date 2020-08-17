package com.suncart.grocerysuncart.database.tables;

import com.dbflow5.annotation.Column;
import com.dbflow5.annotation.PrimaryKey;
import com.dbflow5.annotation.Table;
import com.dbflow5.structure.BaseModel;
import com.suncart.grocerysuncart.database.AppDatabase;

@Table(database = AppDatabase.class)
public class ProductItems extends BaseModel {

    @PrimaryKey(autoincrement = false)
    int ids;

    @Column
    String productName;

    @Column
    String productMrp;

    @Column
    String productSp;

    @Column
    String productWeight;

    @Column
    int totalQty;
}
