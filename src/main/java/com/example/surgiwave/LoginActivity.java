package com.example.surgiwave;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import Model.Users;
import Prevelant.Prevelant;
import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private EditText InputPhoneNumber, InputPassword;
    private Button LoginButton;
    private ProgressDialog loadingBar;
    private String ParentDBname = "Users";
    private CheckBox checkBoxRememberMe,checkBoxRememberMeAdmin;
    String phone;
    String password;



    private TextView AdminLink, NotAdminLink,forgetPasswordLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginButton = (Button) findViewById(R.id.login_btn);
        InputPassword = (EditText) findViewById(R.id.login_password_input);
        InputPhoneNumber = (EditText) findViewById(R.id.login_phone_numer_input);
        checkBoxRememberMe = findViewById(R.id.remerber_me_checkbox);
        checkBoxRememberMeAdmin = findViewById(R.id.remerber_me_checkbox_admin);
        AdminLink = (TextView) findViewById(R.id.admin_pannel_link);
        NotAdminLink = (TextView) findViewById(R.id.not_admin_pannel_link);
        forgetPasswordLink=(TextView)findViewById(R.id.forget_password_link);

        Paper.init(this);


        forgetPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(LoginActivity.this,ResetPasswordActivity.class);
                intent.putExtra("check","login");
                startActivity(intent);


            }
        });

        checkBoxRememberMeAdmin.setVisibility(View.INVISIBLE);


        loadingBar = new ProgressDialog(this);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserIn();
            }
        });

        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginButton.setText("Admin Login");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                ParentDBname = "Admins";
                checkBoxRememberMe.setVisibility(View.INVISIBLE);
                checkBoxRememberMeAdmin.setVisibility(View.VISIBLE);


            }
        });

        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginButton.setText(" Login");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
                ParentDBname = "Users";
                checkBoxRememberMe.setVisibility(View.VISIBLE);
                checkBoxRememberMeAdmin.setVisibility(View.INVISIBLE);

            }
        });


    }


    public void SendUserIn() {

        phone = InputPhoneNumber.getText().toString();
        password = InputPassword.getText().toString();

        if (phone.equals("")) {
            Toast.makeText(this, "Please Enter Your Phone Number....", Toast.LENGTH_SHORT).show();
        } else if (password.equals("")) {
            Toast.makeText(this, "Please Enter Your Password....", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Logging in");
            loadingBar.setMessage("Please wait while we  are checking the Credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAcessToAccount(phone, password);
        }
    }

    private void AllowAcessToAccount(final String phone, final String password) {

        if (checkBoxRememberMe.isChecked()) {
            Paper.book().write(Prevelant.UserPhoneKey, phone);
            Paper.book().write(Prevelant.UserPasswordKey, password);
            Paper.book().write(Prevelant.UserParentDBname,ParentDBname);

        }

        if(checkBoxRememberMeAdmin.isChecked()){

            Paper.book().write(Prevelant.UserParentDBname,ParentDBname);
            Paper.book().write(Prevelant.UserPhoneKey, phone);
            Paper.book().write(Prevelant.UserPasswordKey, password);
        }

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(ParentDBname).child(phone).exists()) {

                   Users userData = dataSnapshot.child(ParentDBname).child(phone).getValue(Users.class);
                    if (userData.getPhone().equals(phone)) {
                        if (userData.getPassword().equals(password)) {


                            if (ParentDBname.equals("Admins")) {
                                Toast.makeText(getApplicationContext(), "Admin Login Scessfull!!", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                    finish();
                                Intent a = new Intent(getApplicationContext(), AdminCategoryActivity.class);
                                startActivity(a);
                            } else {
                                Toast.makeText(LoginActivity.this, "Logged in Scessfully!!", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                finish();
                                Prevelant.currentOnlineUser=userData;
                                Intent b = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(b);
                            }


                        } else {
                            Toast.makeText(LoginActivity.this, "Incorrect User ID or Password!!", Toast.LENGTH_SHORT).show();
                            InputPhoneNumber.setText("");
                            InputPassword.setText("");
                            loadingBar.dismiss();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Incorrect User ID or Password!!", Toast.LENGTH_SHORT).show();
                        InputPhoneNumber.setText("");
                        InputPassword.setText("");
                        loadingBar.dismiss();
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "Account with " + phone + "  do not exists!! Please Join to Create a New Account", Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
