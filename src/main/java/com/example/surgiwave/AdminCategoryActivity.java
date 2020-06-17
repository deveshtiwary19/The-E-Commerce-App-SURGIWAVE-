package com.example.surgiwave;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import io.paperdb.Paper;

public class AdminCategoryActivity extends AppCompatActivity {

    private ImageView icu,nicu,ot,genral,instruments,pipeline,others;

    private Button logOut,checkOrders,maintainProducts;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        icu=findViewById(R.id.category_icu);
        nicu=findViewById(R.id.category_nicu);
        genral=findViewById(R.id.category_genral);
        ot=findViewById(R.id.category_ot);
        instruments=findViewById(R.id.category_instruments);
        pipeline=findViewById(R.id.category_pipeline);
        others=findViewById(R.id.category_others);

        logOut=findViewById(R.id.admin_logout_btn);
        checkOrders=findViewById(R.id.check_orders_btn);
        maintainProducts=findViewById(R.id.maintain_products);



        maintainProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(getApplicationContext(),AdminDisplayHome.class);

                startActivity(in);





            }
        });


        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(AdminCategoryActivity.this)
                        .setIcon(R.drawable.common_google_signin_btn_icon_light_normal_background)
                        .setMessage("Are you sure to logout?").setTitle("LOGOUT")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Paper.book().destroy();
                                Intent in=new Intent(getApplicationContext(),MainActivity.class);
                                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(in);
                                finish();
                            }
                        })
                        .setNegativeButton("No",null).show();


            }
        });

        checkOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in=new Intent(getApplicationContext(),AdminNewOrdersActivity.class);

                startActivity(in);


            }
        });



        icu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(),AdminAddProductActivity.class);
                intent.putExtra("category","icu");
                startActivity(intent);
            }
        });

        nicu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(),AdminAddProductActivity.class);
                intent.putExtra("category","nicu");
                startActivity(intent);
            }
        });

        genral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(),AdminAddProductActivity.class);
                intent.putExtra("category","genral");
                startActivity(intent);
            }
        });

        ot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(),AdminAddProductActivity.class);
                intent.putExtra("category","ot");
                startActivity(intent);
            }
        });
        instruments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(),AdminAddProductActivity.class);
                intent.putExtra("category","instruments");
                startActivity(intent);
            }
        });
        pipeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(),AdminAddProductActivity.class);
                intent.putExtra("category","pipeline");
                startActivity(intent);
            }
        });
        others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(),AdminAddProductActivity.class);
                intent.putExtra("category","others");
                startActivity(intent);
            }
        });

    }
}
