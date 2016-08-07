package com.example.android.inventory;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;



public class AddActivity extends AppCompatActivity {

    private InventoryDbHelper mDbHelper = new InventoryDbHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        boolean dataValid = false;

        EditText enterName = (EditText) findViewById(R.id.enterName);
        String prodcName = enterName.getText().toString();
        EditText enterQuantity = (EditText) findViewById(R.id.enterQuantity);
        EditText enterPrice = (EditText) findViewById(R.id.enterPrice);

        if (prodcName.isEmpty()){
            Toast invalidData = Toast.makeText(this,"No Product Name Given",Toast.LENGTH_SHORT);
            invalidData.show();
        }else{
            try{
                int quantity = Integer.parseInt(enterQuantity.getText().toString());
                float price = Float.parseFloat(enterPrice.getText().toString());
            }catch (Exception e){
                Toast invalidData = Toast.makeText(this,"Invalid Data Entry",Toast.LENGTH_SHORT);
                invalidData.show();
                dataValid = true;
            }
        }

        if (dataValid){
            insertNewProduct();
        }


    }
}
