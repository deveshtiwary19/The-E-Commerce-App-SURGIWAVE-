package com.example.surgiwave;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Model.Users;
import Prevelant.Prevelant;
import io.paperdb.Paper;

import static io.paperdb.Paper.*;

public class MainActivity extends AppCompatActivity {

    private Button joinNow, Login;
    private ProgressDialog loadingBar;
    String ParentDBname="";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init(this);




        joinNow = findViewById(R.id.join_now);
        Login = findViewById(R.id.main_login);
        loadingBar = new ProgressDialog(this);
      Paper.init(this);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        joinNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

      String UserPhone=Paper.book().read(Prevelant.UserPhoneKey);
        String UserPassword=Paper.book().read(Prevelant.UserPasswordKey);
           ParentDBname= Paper.book().read(Prevelant.UserParentDBname);

        if(UserPhone!="" && UserPassword!= "") {

            if (!TextUtils.isEmpty(UserPhone) && !TextUtils.isEmpty(UserPassword)) {

                loadingBar.setTitle("Logged in");
                loadingBar.setMessage("Please wait!! Setting up the Enviroment");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                AllowAcess(UserPhone,UserPassword);
            }
        }

    }

    private void AllowAcess( final String phone,  final String password) {

        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(ParentDBname).child(phone).exists()){

                    Users userData=dataSnapshot.child(ParentDBname).child(phone).getValue(Users.class);
                    if(userData.getPhone().equals(phone)){
                        if(userData.getPassword().equals(password)){


                            if (ParentDBname.equals("Admins")) {
                                Toast.makeText(getApplicationContext(), "Admin Login Scessfull!!", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                finish();
                                Intent a = new Intent(getApplicationContext(), AdminCategoryActivity.class);
                                startActivity(a);
                            } else {
                                Toast.makeText(getApplicationContext(), "Logged in Scessfully!!", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                finish();
                                Prevelant.currentOnlineUser=userData;
                                Intent b = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(b);
                            }



                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Incorrect User ID or Password!!", Toast.LENGTH_SHORT).show();

                            loadingBar.dismiss();
                        }
                    } else
                    {
                        Toast.makeText(MainActivity.this, "Incorrect User ID or Password!!", Toast.LENGTH_SHORT).show();

                        loadingBar.dismiss();
                    }

                }
                else
                {
                    Toast.makeText(MainActivity.this, "Account with "+phone+"  do not exists!! Please Join to Create a New Account", Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}


