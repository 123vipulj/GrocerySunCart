package com.suncart.grocerysuncart.database.tables;

import com.dbflow5.annotation.Column;
import com.dbflow5.annotation.PrimaryKey;
import com.dbflow5.annotation.Table;
import com.dbflow5.structure.BaseModel;
import com.suncart.grocerysuncart.database.AppDatabase;

@Table(database = AppDatabase.class)
public class ProductItems extends BaseModel {

    @PrimaryKey(autoincrement = false)
    public long ids;

    @Column
    public String productName;

    @Column
    public String productPics;

    @Column
    public String productMrp;

    @Column
    public String productSp;

    @Column
    public String productWeight;

    @Column
    public String unitType;

    @Column
    public String discountProduct;


    @Column
    public int totalQty;
}
