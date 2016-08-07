package com.example.android.inventory;


import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Product>> {

    private InventoryDbHelper mDbHelper = new InventoryDbHelper(this);
    private ProductAdapter mAdapter;
    private TextView mEmptyStateTextView;
    private static final int PRODUCT_LOADER_ID =1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView productsListView = (ListView) findViewById(R.id.list);
        mAdapter = new ProductAdapter(this,new ArrayList<Product>());

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        productsListView.setEmptyView(mEmptyStateTextView);

        productsListView.setAdapter(mAdapter);

        productsListView.setOnItemClickListener( new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Product currentProduct = mAdapter.getItem(position);
                Intent productIntent =  new Intent(MainActivity.this, DetailsActivity.class);
                productIntent.putExtra("currentProduct", currentProduct.getProductName());
                startActivity(productIntent);
            }
        });

        android.app.LoaderManager loaderManager  = getLoaderManager();
        loaderManager.initLoader(PRODUCT_LOADER_ID,null,this);
    }

    @Override
    public android.content.Loader<List<Product>> onCreateLoader(int id, Bundle args) {
        Cursor c = null;
        try{
            c = ReadAll();
        }catch(RuntimeException e){
            Log.e("MainActivity","Error reading from Database");
        }

        return new ProductLoader(this,c);
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<Product>> loader, List<Product> products) {
        mEmptyStateTextView.setText(R.string.no_products);
        mAdapter.clear();

        if (products != null && !products.isEmpty()){
            mAdapter.addAll(products);
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<List<Product>> loader) {
        mAdapter.clear();
    }

    public void addButton(View view){
        Intent addIntent= new Intent(this,AddActivity.class);
        startActivity(addIntent);
    }

    public void insertNewProduct(String productName, int quantity, float price){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(InventoryContract.ProductEntry.COLUMN_NAME_PRODC_NAME,productName);
        values.put(InventoryContract.ProductEntry.COLUMN_NAME_QUANTITY,quantity);
        values.put(InventoryContract.ProductEntry.COLUMN_NAME_PRICE,price);

        db.insert(InventoryContract.ProductEntry.TABLE_NAME,null, values);
    }

    public Cursor ReadAll(){
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
