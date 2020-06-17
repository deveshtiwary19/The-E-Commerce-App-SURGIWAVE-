package com.example.surgiwave;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainProductActivity extends AppCompatActivity {

    private Button applyChangesbtn,delButton;
    private EditText name,price,description;
    private ImageView imageView;
    private String productID="";
    private DatabaseReference productsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_product);

        applyChangesbtn=findViewById(R.id.apply_changes_btn);
        delButton=findViewById(R.id.delete_product_btn);
        name=findViewById(R.id.product_name_maintain);
        price=findViewById(R.id.product_price_maintain);
        description =findViewById(R.id.product_descri_maintain);
        imageView=findViewById(R.id.product_image_maintain);

        productID=getIntent().getStringExtra("pid");
        productsRef= FirebaseDatabase.getInstance().getReference().child("Products").child(productID);


        displaySpecificProductInfo();

        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                CharSequence options[]= new CharSequence[]
                        {
                                "Yes",
                                "No"

                        };
                AlertDialog.Builder builder=new AlertDialog.Builder(AdminMaintainProductActivity.this);
                builder.setTitle("Are you sure to delete the Product?");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(which==0){

                            productsRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){
                                        Toast.makeText(AdminMaintainProductActivity.this, "Product Removed Sucessfully", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }

                                }
                            });




                        }

                        else
                        {

                        }







                    }
                });
                        builder.show();
            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AdminMaintainProductActivity.this, "Image is not subject to change!! ", Toast.LENGTH_LONG).show();
            }
        });

        applyChangesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                applyChanges();



            }
        });





    }


    private void applyChanges() {

        String Pname= name.getText().toString();
        String Pprice= price.getText().toString();
        String Pdescri= description.getText().toString();

        if(Pname.equals("")){
            Toast.makeText(this, "Please Enter Product Name", Toast.LENGTH_SHORT).show();
        }
        else if(Pprice.equals("")){
            Toast.makeText(this, "Please Enter Product Price", Toast.LENGTH_SHORT).show();
        }
        else if(Pdescri.equals("")){
            Toast.makeText(this, "Please Enter Product Description", Toast.LENGTH_SHORT).show();
        }
        else
        {
            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put("pid", productID);
            productMap.put("description", Pdescri);
            productMap.put("price", Pprice);
            productMap.put("pname", Pname);

            productsRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful()){
                        Toast.makeText(AdminMaintainProductActivity.this, "Changes Apllied Sucessfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                    {
                        Toast.makeText(AdminMaintainProductActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    }


                }
            });




        }



    }

    private void displaySpecificProductInfo() {

        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    String Pname=dataSnapshot.child("pname").getValue().toString();
                    String Pprice=dataSnapshot.child("price").getValue().toString();
                    String Pdescription=dataSnapshot.child("description").getValue().toString();
                    String Pimage=dataSnapshot.child("image").getValue().toString();

                    name.setText(Pname);
                    price.setText(Pprice);
                    description.setText(Pdescription);
                    Picasso.get().load(Pimage).into(imageView);

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
