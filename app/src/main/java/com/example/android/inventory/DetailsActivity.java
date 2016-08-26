package com.example.android.inventory;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    private InventoryDbHelper mDbHelper;
    static final int COL_PRODC_NAME = 1;
    static final int COL_QUANTITY = 2;
    static final int COL_PRICE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mDbHelper =new InventoryDbHelper(getApplicationContext());
        String name = "";
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection ={InventoryContract.ProductEntry._ID,InventoryContract.ProductEntry.COLUMN_NAME_PRODC_NAME, InventoryContract.ProductEntry.COLUMN_NAME_QUANTITY, InventoryContract.ProductEntry.COLUMN_NAME_PRICE};

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("currentProduct");
        }
        String selection = InventoryContract.ProductEntry.COLUMN_NAME_PRODC_NAME + " LIKE '" + name+"'";



        Cursor c = db.query(InventoryContract.ProductEntry.TABLE_NAME,
                projection,
                selection,
                null,
                null,
                null,
                null);

        c.moveToFirst();
        TextView productNameTextView = (TextView) findViewById(R.id.name);
        productNameTextView.setText(c.getString(COL_PRODC_NAME));

        TextView quantityTextView = (TextView) findViewById(R.id.amount);
        quantityTextView.setText(Integer.toString(c.getInt(COL_QUANTITY)));

        TextView priceTextView =(TextView) findViewById(R.id.price);
        String priceTag = "$" + c.getFloat(COL_PRICE);
        priceTextView.setText(priceTag);

        Button finButton = (Button) findViewById(R.id.button);
        finButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                setResult(RESULT_OK,returnIntent);
                finish();
            }
        });
    }
}
