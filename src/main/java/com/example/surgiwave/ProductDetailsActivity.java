package com.example.surgiwave;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import Model.Products;
import Prevelant.Prevelant;

public class ProductDetailsActivity extends AppCompatActivity {

    private FloatingActionButton addToCart;
    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private TextView productPrice,productDescription,productName;
    EditText manually;
    private Button manuallyBtn;
    private String productID="";
    int x=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);


        //Buttons
        addToCart=(FloatingActionButton) findViewById(R.id.add_product_to_cart_btn);
        numberButton=(ElegantNumberButton)findViewById(R.id.number_btn);
        //TextViews
        productDescription=(TextView)findViewById(R.id.product_description_details);
        productPrice=(TextView)findViewById(R.id.product_price_details);
        productName=(TextView)findViewById(R.id.product_name_details);
        //ImageView
        productImage=(ImageView)findViewById(R.id.product_image_details);
        //ProductID
        productID=getIntent().getStringExtra("pid");
        //Manual Quantity things
        manually=(EditText)findViewById(R.id.manually_get_quantity);
        manuallyBtn=(Button)findViewById(R.id.manually_button);

        manually.setVisibility(View.INVISIBLE);

        manuallyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(x==0)
                {
                    manually.setVisibility(View.VISIBLE);
                    numberButton.setVisibility(View.INVISIBLE);
                    manuallyBtn.setText("Get Quantity Button");
                    x=1;
                }
                else
                {

                    manually.setVisibility(View.INVISIBLE);
                    numberButton.setVisibility(View.VISIBLE);
                    manuallyBtn.setText("Enter Quantity Manually");
                    x=0;

                }


            }
        });




        getProducrDetails(productID);

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(Integer.parseInt(numberButton.getNumber()) >3000 && Integer.parseInt(manually.getText().toString())>3000 ){
                    Toast.makeText(ProductDetailsActivity.this, "Maximum limits is 3000 Units", Toast.LENGTH_SHORT).show();
                }
                else {
                    addingToCartList();
                }
            }
        });


    }

    private void addingToCartList() {

        String saveCurrentTime,saveCurrentDate;

        Calendar callForDate= Calendar.getInstance();
        SimpleDateFormat currentDate= new SimpleDateFormat("MMM dd,yyyy");

        saveCurrentDate= currentDate.format(callForDate.getTime());

        SimpleDateFormat currentTime= new SimpleDateFormat("HH:mm:ss a");

        saveCurrentTime= currentDate.format(callForDate.getTime());


         final DatabaseReference cartListRef= FirebaseDatabase.getInstance().getReference().child("Cart List");
        final HashMap<String,Object> cartMap= new HashMap<>();
        cartMap.put("pid",productID);
        cartMap.put("pname",productName.getText().toString());
        cartMap.put("price",productPrice.getText().toString());
        cartMap.put("date",saveCurrentDate);
        cartMap.put("time",saveCurrentTime);
        if(x==0){
            cartMap.put("quantity",numberButton.getNumber());
        }
        else
        {
            cartMap.put("quantity",manually.getText().toString());
        }
        cartMap.put("discount","");

        cartListRef.child("User View").child(Prevelant.currentOnlineUser.getPhone()).child("Products")
                .child(productID).updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            cartListRef.child("Admin View").child(Prevelant.currentOnlineUser.getPhone()).child("Products")
                                    .child(productID).updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {


                                            if(task.isSuccessful()){
                                                Toast.makeText(ProductDetailsActivity.this, "Added To Cart sucessfully", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        }
                                    });

                        }

                    }
                });






    }

    private void getProducrDetails(String productID) {

        DatabaseReference productRef= FirebaseDatabase.getInstance().getReference().child("Products");
        productRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    Products products=dataSnapshot.getValue(Products.class);

                    productName.setText(products.getPname());
                    productDescription.setText(products.getDescription());
                    productPrice.setText(products.getPrice());
                    Picasso.get().load(products.getImage()).into(productImage);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
