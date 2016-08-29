package com.example.android.inventory;


import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.stetho.Stetho;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Product>> {

    private InventoryDbHelper mDbHelper = new InventoryDbHelper(this);
    private ProductAdapter mAdapter;
    private TextView mEmptyStateTextView;
    private android.app.LoaderManager loaderManager;
    private static final int PRODUCT_LOADER_ID =1;
    private  ListView productsListView;
    private int REQUEST_CODE= 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Stetho.initializeWithDefaults(this);
        productsListView = (ListView) findViewById(R.id.list);
        mAdapter = new ProductAdapter(this,new ArrayList<Product>());

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        productsListView.setEmptyView(mEmptyStateTextView);

        productsListView.setAdapter(mAdapter);


        loaderManager  = getLoaderManager();
        loaderManager.initLoader(PRODUCT_LOADER_ID,null,this);

        productsListView.setOnItemClickListener( new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Product currentProduct = mAdapter.getItem(position);
                Intent productIntent =  new Intent(MainActivity.this, DetailsActivity.class);
                productIntent.putExtra("currentProduct", currentProduct.getProductName());
                startActivityForResult(productIntent,REQUEST_CODE);
            }
        });




    }

    @Override
    public android.content.Loader<List<Product>> onCreateLoader(int id, Bundle args) {
        Cursor c = null;
        try{
            c = DbUtils.ReadAll(this);
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
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<List<Product>> loader) {
        mAdapter.clear();
    }

    public void addButton(View view){
        Intent addIntent= new Intent(this,AddActivity.class);
        startActivityForResult(addIntent,REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            loaderManager.restartLoader(0, null, this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loaderManager.restartLoader(0, null, this);
    }

}
