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
        //Constructor is Empty and private to prevent someone from creating an instance of the DBUtils Class.
    }
    public static void insertNewProduct(String productName, int quantity, float price,String image,int sold, Context context){
        mDbHelper = new InventoryDbHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(InventoryContract.ProductEntry.COLUMN_NAME_PRODC_NAME,productName);
        values.put(InventoryContract.ProductEntry.COLUMN_NAME_QUANTITY,quantity);
        values.put(InventoryContract.ProductEntry.COLUMN_NAME_PRICE,price);
        values.put(InventoryContract.ProductEntry.COLUMN_NAME_IMAGE,image);
        values.put(InventoryContract.ProductEntry.COLUMN_NAME_SOlD,sold);

        db.insert(InventoryContract.ProductEntry.TABLE_NAME,null, values);
    }
    public static Cursor ReadAll(Context context){
        mDbHelper = new InventoryDbHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String[] projection ={InventoryContract.ProductEntry.COLUMN_NAME_PRODC_NAME, InventoryContract.ProductEntry.COLUMN_NAME_QUANTITY, InventoryContract.ProductEntry.COLUMN_NAME_PRICE, InventoryContract.ProductEntry.COLUMN_NAME_SOlD};

        String sortOrder = InventoryContract.ProductEntry.COLUMN_NAME_PRODC_NAME +" DESC";

        Cursor c = db.query(InventoryContract.ProductEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder);
        return c;
    }
    public static void DeleteAll(Context context){
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

    public static void Update(String column,String content, long rowId,Context context){
        mDbHelper = new InventoryDbHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values =new ContentValues();
        values.put(column,content);

        String selection = InventoryContract.ProductEntry._ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(rowId)};

        db.update(
                InventoryContract.ProductEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        db.close();
    }
    public static void Update(String column,String content, String name,Context context){
        mDbHelper = new InventoryDbHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values =new ContentValues();
        values.put(column,content);

        String selection = InventoryContract.ProductEntry.COLUMN_NAME_PRODC_NAME+ " LIKE ?";
        String[] selectionArgs = {name};

        db.update(
                InventoryContract.ProductEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        db.close();
    }
    public static void delete(int rowId,Context context){
        mDbHelper = new InventoryDbHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String selection = InventoryContract.ProductEntry._ID + " LIKE ?";
        ArrayList<String> args = new ArrayList<String>();

        String[] selectionArgs = {""+rowId};
        db.delete(InventoryContract.ProductEntry.TABLE_NAME, selection,selectionArgs);
        db.close();
    }
}
