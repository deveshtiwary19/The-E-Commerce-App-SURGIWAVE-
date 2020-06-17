package com.example.surgiwave;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import Prevelant.Prevelant;

public class ConfirmFinalOrderActivity extends AppCompatActivity {
    private EditText nameedittext,phonreditTextt,adresseditText,cityeditText;
    private Button confirmOrderBtn,ok;
    private String totalAmount="";
     private TextView one,two;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        confirmOrderBtn=findViewById(R.id.confirm_final_order_btn);

        nameedittext=findViewById(R.id.shipment_name);
        phonreditTextt=findViewById(R.id.shipment_phone_number);
        adresseditText=findViewById(R.id.shipment_adress);
        cityeditText=findViewById(R.id.shipment_city);
        one=findViewById(R.id.txt);
        two=findViewById(R.id.textView_message);
        ok=findViewById(R.id.ok);

        totalAmount=getIntent().getStringExtra("Total Price");

        nameedittext.setVisibility(View.VISIBLE);

        cityeditText.setVisibility(View.VISIBLE);
        adresseditText.setVisibility(View.VISIBLE);
        phonreditTextt.setVisibility(View.VISIBLE);
        confirmOrderBtn.setVisibility(View.VISIBLE);
        ok.setVisibility(View.INVISIBLE);
        two.setVisibility(View.INVISIBLE);



        confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(totalAmount.equals("0")){
                    Toast.makeText(ConfirmFinalOrderActivity.this, "Empty Order Cant be Placed!! Select Some Items in The Cart", Toast.LENGTH_SHORT).show();
                }
                else {






                    Check();
                }


            }
        });


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }

    private void Check() {

        if(TextUtils.isEmpty(nameedittext.getText().toString())){
            Toast.makeText(this, "Reciever's Name is Mandatory", Toast.LENGTH_SHORT).show();
        }

        if(TextUtils.isEmpty(phonreditTextt.getText().toString())){
            Toast.makeText(this, "Phone Number is Required to Place the Order", Toast.LENGTH_SHORT).show();
        }

        if(TextUtils.isEmpty(adresseditText.getText().toString())){
            Toast.makeText(this, "Adress is Mandatory", Toast.LENGTH_SHORT).show();
        }

        if(TextUtils.isEmpty(cityeditText.getText().toString())){
            Toast.makeText(this, "City  is Mandatory", Toast.LENGTH_SHORT).show();
        }
        else
        {
            ConfirmOrder();
        }

    }

    private void ConfirmOrder() {

         final String saveCurrentDate,saveCurrentTime;

        Calendar callForDate= Calendar.getInstance();
        SimpleDateFormat currentDate= new SimpleDateFormat("MMM dd,yyyy");

        saveCurrentDate= currentDate.format(callForDate.getTime());

        SimpleDateFormat currentTime= new SimpleDateFormat("HH:mm:ss a");

        saveCurrentTime= currentDate.format(callForDate.getTime());


        final DatabaseReference ordersRef= FirebaseDatabase.getInstance().getReference().child("Orders")
                .child(Prevelant.currentOnlineUser.getPhone());
        HashMap<String,Object> ordersmap= new HashMap<>();
        ordersmap.put("totalAmount",totalAmount);
        ordersmap.put("name",nameedittext.getText().toString());
        ordersmap.put("phone",phonreditTextt.getText().toString());
        ordersmap.put("adress",adresseditText.getText().toString());
        ordersmap.put("city",cityeditText.getText().toString());
        ordersmap.put("date",saveCurrentDate);
        ordersmap.put("time",saveCurrentTime);
        ordersmap.put("state","Not Accepted");

        ordersRef.updateChildren(ordersmap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference().child("Cart List")
                            .child("User View")
                            .child(Prevelant.currentOnlineUser.getPhone())
                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                nameedittext.setVisibility(View.INVISIBLE);
                                phonreditTextt.setVisibility(View.INVISIBLE);
                                adresseditText.setVisibility(View.INVISIBLE);
                                cityeditText.setVisibility(View.INVISIBLE);
                                confirmOrderBtn.setVisibility(View.INVISIBLE);
                                one.setText("Congratulations!!");
                                two.setText("Your Order is placed sucessfully!! You will recieve confirmation from Surgiwave soon. To prevent overlapping,you are advised not to place another order before recieving confirmation");
                                ok.setVisibility(View.VISIBLE);
                                two.setVisibility(View.VISIBLE);





                                Toast.makeText(ConfirmFinalOrderActivity.this, "Order Placed Sucessfully", Toast.LENGTH_LONG).show();


                            }


                        }
                    });
                }

            }
        });




    }
}
