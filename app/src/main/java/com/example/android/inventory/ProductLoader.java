package com.example.android.inventory;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by keane on 8/7/2016.
 */
public class ProductLoader extends AsyncTaskLoader {

    private Cursor mProdcData;

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
        if (!(mProdcData == null)){
            for (int n= 0; n<mProdcData.getColumnCount(); n++){
                products.add(new Product(mProdcData.getString(n),mProdcData.getInt(n),mProdcData.getFloat(n)));
            }
        }
        return products;
    }
}
