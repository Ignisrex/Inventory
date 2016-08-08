package com.example.android.inventory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class AddActivity extends AppCompatActivity {

    private InventoryDbHelper mDbHelper = new InventoryDbHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);


        final EditText enterName = (EditText) findViewById(R.id.enterName);
        final EditText enterQuantity = (EditText) findViewById(R.id.enterQuantity);
        final EditText enterPrice = (EditText) findViewById(R.id.enterPrice);

        Button finishbutton = (Button) findViewById(R.id.finishbutton);
        finishbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = 0;
                float price = 1;
                boolean dataValid = true;

                String prodcName = enterName.getText().toString();

                if (prodcName.isEmpty()){
                    Toast invalidData = Toast.makeText(getApplicationContext(),"No Product Name Given",Toast.LENGTH_SHORT);
                    invalidData.show();
                    dataValid = false;
                }else{
                    try{
                        quantity = Integer.parseInt(enterQuantity.getText().toString());
                        price = Float.parseFloat(enterPrice.getText().toString());
                    }catch (Exception e){
                        Toast invalidData = Toast.makeText(getApplicationContext(),"Invalid Data Entry",Toast.LENGTH_SHORT);
                        invalidData.show();
                        dataValid =false;
                    }
                }

                if (dataValid){
                    DbUtils.insertNewProduct(prodcName,quantity,price,getApplicationContext());
                    Intent returnIntent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(returnIntent);
                }
            }
        });

    }
}
