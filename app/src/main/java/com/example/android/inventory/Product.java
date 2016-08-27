package com.example.android.inventory;

/**
 * Created by keane on 8/5/2016.
 */
public class Product {
    private String mProductName;
    private int mQuantity;
    private float mPrice;
    private int mSold;

    public Product(String name,int quantity,float price, int sold){
        mProductName = name;
        mQuantity = quantity;
        mPrice = price;
    }

    public String getProductName() {
        return mProductName;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public float getPrice() {
        return mPrice;
    }

    public int getSold(){return mSold;}

}
