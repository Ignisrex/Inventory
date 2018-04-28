package com.example.android.inventory;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by keane on 8/7/2016.
 */
public class ProductLoader extends AsyncTaskLoader {

    private Cursor mProdcData;
    static final int COL_PRODC_NAME = 0;
    static final int COL_QUANTITY = 1;
    static final int COL_PRICE = 2;
    static final int COL_SOLD=3;

    public ProductLoader(Context context,Cursor c){
        super(context);
        mProdcData =c;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Product> loadInBackground() {
        ArrayList<Product> products = new ArrayList<>();
        if(mProdcData == null || mProdcData.getCount() == 0) {
            return null;
        }
        while (mProdcData.moveToNext()) {
            products.add(
                    new Product(mProdcData.getString(COL_PRODC_NAME),
                            mProdcData.getInt(COL_QUANTITY),
                            mProdcData.getFloat(COL_PRICE),
                            mProdcData.getInt(COL_SOLD))
            );
            Log.e("test",products.toString());
        }
        return products;
    }
}
