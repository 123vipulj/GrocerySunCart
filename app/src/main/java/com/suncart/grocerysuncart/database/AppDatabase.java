package com.suncart.grocerysuncart.database;

import com.dbflow5.annotation.Database;
import com.dbflow5.config.DBFlowDatabase;

@Database(version = AppDatabase.VERSION)
public abstract class  AppDatabase extends DBFlowDatabase {

    public static final String NAME = "GroceryDb";
    public static final int VERSION = 1;
}
