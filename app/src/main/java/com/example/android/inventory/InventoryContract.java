package com.example.android.inventory;

import android.provider.BaseColumns;

/**
 * Created by keane on 8/5/2016.
 */
public final class InventoryContract {

    private InventoryContract(){}
    public static abstract class ProductEntry implements BaseColumns{
        public static final String TABLE_NAME = "Products";
        public static final String COLUMN_NAME_PRODC_NAME = "ProductName";
        public static final String COLUMN_NAME_QUANTITY ="Quantity";
        public static final String COLUMN_NAME_PRICE = "Price";


    }
}
