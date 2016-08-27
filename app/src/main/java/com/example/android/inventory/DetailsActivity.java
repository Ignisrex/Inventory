package com.example.android.inventory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

public class DetailsActivity extends AppCompatActivity {

    private InventoryDbHelper mDbHelper;
    static final int COL_PRODC_NAME = 1;
    static final int COL_QUANTITY = 2;
    static final int COL_PRICE = 3;
    static final int COL_IMAGE = 4;
    static final int COL_SOLD=5;
    private Cursor c;
    private SQLiteDatabase db;
    private String selection;
    private String[] projection ={InventoryContract.ProductEntry._ID,InventoryContract.ProductEntry.COLUMN_NAME_PRODC_NAME, InventoryContract.ProductEntry.COLUMN_NAME_QUANTITY, InventoryContract.ProductEntry.COLUMN_NAME_PRICE, InventoryContract.ProductEntry.COLUMN_NAME_IMAGE, InventoryContract.ProductEntry.COLUMN_NAME_SOlD};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mDbHelper =new InventoryDbHelper(getApplicationContext());
        String name = "";
        db = mDbHelper.getWritableDatabase();


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("currentProduct");
        }
        selection = InventoryContract.ProductEntry.COLUMN_NAME_PRODC_NAME + " LIKE '" + name+"'";

        UpdateUi();

        Button finButton = (Button) findViewById(R.id.button);
        finButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                setResult(RESULT_OK,returnIntent);
                finish();
            }
        });

        Button plusBtn = (Button) findViewById(R.id.plus);
        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newQn = c.getInt(COL_QUANTITY) + 1;

                DbUtils.Update(InventoryContract.ProductEntry.COLUMN_NAME_QUANTITY,Integer.toString(newQn),c.getInt(0),getApplicationContext());
                UpdateUi();

            }
        });
        Button minusBtn =(Button) findViewById(R.id.minus);
        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newQn = c.getInt(COL_QUANTITY) - 1;
                if(!(newQn < 0)){
                    DbUtils.Update(InventoryContract.ProductEntry.COLUMN_NAME_QUANTITY,Integer.toString(newQn),c.getInt(0),getApplicationContext());
                    UpdateUi();
                }
            }
        });

        Button makeSaleBtn = (Button) findViewById(R.id.sale);
        makeSaleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newQn = c.getInt(COL_QUANTITY) - 1;
                if(!(newQn < 0)){
                    DbUtils.Update(InventoryContract.ProductEntry.COLUMN_NAME_QUANTITY,Integer.toString(newQn),c.getInt(0),getApplicationContext());
                    UpdateUi();
                }
            }
        });

        Button orderBtn =(Button) findViewById(R.id.shipment);
        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent orderIntent = new Intent(Intent.ACTION_SENDTO);
                orderIntent.setData(Uri.parse("mailto:"));
                orderIntent.putExtra(Intent.EXTRA_SUBJECT, "Order For " + c.getString(COL_PRODC_NAME));
                orderIntent.putExtra(Intent.EXTRA_TEXT, "This is an Order request for "+ c.getString(COL_PRODC_NAME)+". \n"+"Requested Amount:");
                if (orderIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(orderIntent);
                }
            }
        });

        Button deleteBtn =(Button) findViewById(R.id.deleteButton);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailsActivity.this);
                builder.setMessage(R.string.delConfirmationMsg);

                builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DbUtils.delete(c.getInt(0),DetailsActivity.this);
                        Intent returnIntent = new Intent();
                        setResult(RESULT_OK,returnIntent);
                        finish();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog delProdcAlert = builder.create();
                delProdcAlert.show();
            }
        });
    }
    private void UpdateUi(){
        c = db.query(InventoryContract.ProductEntry.TABLE_NAME,
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

        Uri imageUri= Uri.parse(c.getString(COL_IMAGE));
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

            ImageView imageView = (ImageView) findViewById(R.id.image);
            imageView.setImageBitmap(bitmap);
        }catch (IOException e) {
            e.printStackTrace();
        }

        TextView soldTextView = (TextView) findViewById(R.id.soldAmt);
        String soldString = "Sold:"+ c.getInt(COL_SOLD);
        soldTextView.setText(soldString);


    }
}
