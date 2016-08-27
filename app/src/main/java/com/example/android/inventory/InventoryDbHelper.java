package com.example.android.inventory;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by keane on 8/5/2016.
 */
public class InventoryDbHelper extends SQLiteOpenHelper {
    private final static String TEXT_TYPE =" TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String REAL_TYPE=" REAL";
    private static final String COMMA_SEP =",";
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "Products.db";
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + InventoryContract.ProductEntry.TABLE_NAME + " (" +
            InventoryContract.ProductEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEP + InventoryContract.ProductEntry.COLUMN_NAME_PRODC_NAME
            + TEXT_TYPE + COMMA_SEP + InventoryContract.ProductEntry.COLUMN_NAME_QUANTITY + INT_TYPE + COMMA_SEP+
            InventoryContract.ProductEntry.COLUMN_NAME_PRICE +REAL_TYPE+COMMA_SEP+InventoryContract.ProductEntry.COLUMN_NAME_IMAGE
            + TEXT_TYPE +COMMA_SEP+ InventoryContract.ProductEntry.COLUMN_NAME_SOlD + INT_TYPE+ ")";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + InventoryContract.ProductEntry.TABLE_NAME;

    public InventoryDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    public void delete(Context context){
        context.deleteDatabase(InventoryContract.ProductEntry.TABLE_NAME);
    }
}
