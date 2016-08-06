package com.example.android.inventory;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private InventoryDbHelper mDbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDbHelper =new InventoryDbHelper(getApplicationContext());


    }

    public void addButton(View view){

    }

    public void insertNewProduct(String productName, int quantity, float price){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(InventoryContract.ProductEntry.COLUMN_NAME_PRODC_NAME,productName);
        values.put(InventoryContract.ProductEntry.COLUMN_NAME_QUANTITY,quantity);
        values.put(InventoryContract.ProductEntry.COLUMN_NAME_PRICE,price);

        db.insert(InventoryContract.ProductEntry.TABLE_NAME,null, values);
    }

    public Cursor Read(){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String[] projection ={InventoryContract.ProductEntry.TABLE_NAME, InventoryContract.ProductEntry._ID,InventoryContract.ProductEntry.COLUMN_NAME_PRODC_NAME, InventoryContract.ProductEntry.COLUMN_NAME_QUANTITY, InventoryContract.ProductEntry.COLUMN_NAME_PRICE};

        String sortOrder = InventoryContract.ProductEntry._ID +"DESC";

        Cursor c = db.query(InventoryContract.ProductEntry.TABLE_NAME,
                projection,
                InventoryContract.ProductEntry._ID,
                null,
                null,
                null,
                sortOrder);
        return c;
    }

    public void deleteAll(){
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

    public void Update(String section,String column,String content, long rowId){
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
