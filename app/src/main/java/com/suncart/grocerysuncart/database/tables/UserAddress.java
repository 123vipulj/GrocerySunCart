package com.suncart.grocerysuncart.database.tables;

import com.dbflow5.annotation.Column;
import com.dbflow5.annotation.PrimaryKey;
import com.dbflow5.annotation.Table;
import com.dbflow5.structure.BaseModel;
import com.suncart.grocerysuncart.database.AppDatabase;

@Table(database = AppDatabase.class)
public class UserAddress extends BaseModel {

    @PrimaryKey(autoincrement = false)
    public long ids;

    @Column
    public String userName;

    @Column
    public String userAddress;

    @Column
    public String exactLoc;

    @Column
    public int isInUse;
}
