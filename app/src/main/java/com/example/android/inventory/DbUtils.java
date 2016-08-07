package com.example.android.inventory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by keane on 8/7/2016.
 */
public class DbUtils {
    private static InventoryDbHelper mDbHelper;

    private DbUtils(){
    }

    public static void insertNewProduct(String productName, int quantity, float price, Context context){
        mDbHelper = new InventoryDbHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(InventoryContract.ProductEntry.COLUMN_NAME_PRODC_NAME,productName);
        values.put(InventoryContract.ProductEntry.COLUMN_NAME_QUANTITY,quantity);
        values.put(InventoryContract.ProductEntry.COLUMN_NAME_PRICE,price);

        db.insert(InventoryContract.ProductEntry.TABLE_NAME,null, values);
    }

    public static Cursor ReadAll(Context context){
        mDbHelper = new InventoryDbHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String[] projection ={InventoryContract.ProductEntry.TABLE_NAME, InventoryContract.ProductEntry._ID,InventoryContract.ProductEntry.COLUMN_NAME_PRODC_NAME, InventoryContract.ProductEntry.COLUMN_NAME_QUANTITY, InventoryContract.ProductEntry.COLUMN_NAME_PRICE};

        String sortOrder = InventoryContract.ProductEntry.COLUMN_NAME_PRODC_NAME +" DESC";

        Cursor c = db.query(InventoryContract.ProductEntry.TABLE_NAME,
                projection,
                InventoryContract.ProductEntry.COLUMN_NAME_PRODC_NAME,
                null,
                null,
                null,
                sortOrder);
        return c;
    }

    public static void deleteAll(Context context){
        mDbHelper = new InventoryDbHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        long cnt  = DatabaseUtils.queryNumEntries(db, InventoryContract.ProductEntry.TABLE_NAME);
        db.close();

        String selection = InventoryContract.ProductEntry._ID + " LIKE ?";
        ArrayList<String> args = new ArrayList<String>();
        int n = (int) cnt;
        String[] selectionArgs = new String[n];
        for (long rowId =0; rowId<cnt; rowId++){
            args.add(String.valueOf(rowId));
        }
        selectionArgs = args.toArray(selectionArgs);
        db.delete(InventoryContract.ProductEntry.TABLE_NAME, selection,selectionArgs);
    }

    public static void Update(String section,String column,String content, long rowId,Context context){
        mDbHelper = new InventoryDbHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values =new ContentValues();
        values.put(column,content);

        String selection = section + " LIKE ?";
        String[] selectionArgs = {String.valueOf(rowId)};

        db.update(
                InventoryContract.ProductEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }
}
