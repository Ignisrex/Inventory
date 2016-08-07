package com.example.android.inventory;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    private InventoryDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mDbHelper =new InventoryDbHelper(getApplicationContext());
        String name = "";
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String[] projection ={InventoryContract.ProductEntry.TABLE_NAME, InventoryContract.ProductEntry._ID,InventoryContract.ProductEntry.COLUMN_NAME_PRODC_NAME, InventoryContract.ProductEntry.COLUMN_NAME_QUANTITY, InventoryContract.ProductEntry.COLUMN_NAME_PRICE};
        String sortOrder = InventoryContract.ProductEntry._ID +"DESC";

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("currentProductName");
        }
        String[] currentProductName = new String[]{name};
        Cursor c = db.query(InventoryContract.ProductEntry.TABLE_NAME,
                projection,
                InventoryContract.ProductEntry.COLUMN_NAME_PRODC_NAME,currentProductName,
                null,
                null,
                sortOrder);

        TextView productNameTextView = (TextView) findViewById(R.id.name);
        productNameTextView.setText(c.getString(0));

        TextView quantityTextView = (TextView) findViewById(R.id.amount);
        quantityTextView.setText(c.getInt(0));

        TextView priceTextView =(TextView) findViewById(R.id.price);
        String priceTag = "$" + c.getFloat(0);
        priceTextView.setText(priceTag);
    }
}
