package com.example.surgiwave;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class CategoryActivity extends AppCompatActivity {

    private ImageView icu,nicu,ot,genral,instruments,pipeline,others;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);




        icu=findViewById(R.id.category_icu);
        nicu=findViewById(R.id.category_nicu);
        ot=findViewById(R.id.category_ot);
        genral=findViewById(R.id.category_genral);
        instruments=findViewById(R.id.category_instruments);
        pipeline=findViewById(R.id.category_pipeline);
       others =findViewById(R.id.category_others);

       icu.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Intent intent=new Intent(getApplicationContext(),CategoryProductHome.class);
               intent.putExtra("category","icu");
               startActivity(intent);


           }
       });

       nicu.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Intent intent=new Intent(getApplicationContext(),CategoryProductHome.class);
               intent.putExtra("category","nicu");
               startActivity(intent);

           }
       });

       ot.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Intent intent=new Intent(getApplicationContext(),CategoryProductHome.class);
               intent.putExtra("category","ot");
               startActivity(intent);

           }
       });

       genral.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {


               Intent intent=new Intent(getApplicationContext(),CategoryProductHome.class);
               intent.putExtra("category","genral");
               startActivity(intent);
           }
       });
       instruments.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Intent intent=new Intent(getApplicationContext(),CategoryProductHome.class);
               intent.putExtra("category","instruments");
               startActivity(intent);
           }
       });

       pipeline.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Intent intent=new Intent(getApplicationContext(),CategoryProductHome.class);
               intent.putExtra("category","pipeline");
               startActivity(intent);

           }
       });

       others.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Intent intent=new Intent(getApplicationContext(),CategoryProductHome.class);
               intent.putExtra("category","others");
               startActivity(intent);

           }
       });










    }
}
