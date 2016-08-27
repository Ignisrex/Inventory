package com.example.android.inventory;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;


public class AddActivity extends AppCompatActivity {

    private Uri imageUri;
    private int IMAGE_REQUEST= 1;
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
                if (imageUri != null) {
                    String image = imageUri.toString();
                }else {
                    Toast invalidData = Toast.makeText(getApplicationContext(),"No Product Image Given",Toast.LENGTH_SHORT);
                    invalidData.show();
                    dataValid = false;
                }
                String prodcName = enterName.getText().toString();

                if (prodcName.isEmpty()){
                    Toast invalidData = Toast.makeText(getApplicationContext(),"No Product Name Given",Toast.LENGTH_SHORT);
                    invalidData.show();
                    dataValid = false;
                    }else {
                    try {
                        quantity = Integer.parseInt(enterQuantity.getText().toString());
                        price = Float.parseFloat(enterPrice.getText().toString());
                    } catch (Exception e) {
                        Toast invalidDataQP = Toast.makeText(getApplicationContext(), "Invalid Data Entry", Toast.LENGTH_SHORT);
                        invalidDataQP.show();
                        dataValid = false;
                    }
                }


                if (dataValid){
                    DbUtils.insertNewProduct(prodcName,quantity,price,getApplicationContext());
                    Intent returnIntent = new Intent();
                    setResult(RESULT_OK,returnIntent);
                    finish();

                }
            }
        });

        Button addImageBtn = (Button) findViewById(R.id.imagebutton);
        addImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imageIntent;
                if (Build.VERSION.SDK_INT < 19) {
                    imageIntent = new Intent(Intent.ACTION_GET_CONTENT);
                } else {
                    imageIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    imageIntent.addCategory(Intent.CATEGORY_OPENABLE);
                }

                checkPermissions();
                imageIntent.setType("image/*");
                startActivityForResult(Intent.createChooser(imageIntent, "Select Picture"), IMAGE_REQUEST);

            }

        });
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

                ImageView imageView = (ImageView) findViewById(R.id.prodcImage);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

