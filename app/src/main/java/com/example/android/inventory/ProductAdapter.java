package com.example.android.inventory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by keane on 8/5/2016.
 */
public class ProductAdapter extends ArrayAdapter<Product> {

    public ProductAdapter(Context context,ArrayList<Product> products){
        super(context,0,products);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.product_list_item,parent,false);
        }

        final Product currentProduct = getItem(position);

        TextView productNameTextView = (TextView) listItemView.findViewById(R.id.productName);
        productNameTextView.setText(currentProduct.getProductName());

        TextView quantityTextView = (TextView) listItemView.findViewById(R.id.quantityAmt);
        quantityTextView.setText(Integer.toString(currentProduct.getQuantity()));

        TextView priceTextView =(TextView) listItemView.findViewById(R.id.productPrice);
        String priceTag = "$" + currentProduct.getPrice();
        priceTextView.setText(priceTag);

        TextView soldTextView = (TextView) listItemView.findViewById(R.id.sold);
        String soldString = "Sold:"+ currentProduct.getSold();
        soldTextView.setText(soldString);

        Button makeSale = (Button) listItemView.findViewById(R.id.saleButton);
        makeSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newQn = currentProduct.getQuantity() - 1;
                if(!(newQn < 0)){
                    int newSld = currentProduct.getSold() + 1;
                    DbUtils.Update(InventoryContract.ProductEntry.COLUMN_NAME_QUANTITY,Integer.toString(newQn),currentProduct.getProductName(),getContext());
                    DbUtils.Update(InventoryContract.ProductEntry.COLUMN_NAME_SOlD,Integer.toString(newSld),currentProduct.getProductName(),getContext());

                }
            }
        });

        return listItemView;
    }
}
