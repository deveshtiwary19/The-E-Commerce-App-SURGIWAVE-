package com.example.surgiwave;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button createAccountButton;
    private EditText InputName,InputPhoneNumber,InputPassword;
    private ProgressDialog loadingBar;

    private void ValidatePhoneNumber(final String name, final String phone, final String password) {
        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(!(dataSnapshot.child("Users").child(phone).exists())){

                    HashMap<String,Object> userDataMap= new HashMap<>();
                    userDataMap.put("Phone",phone);
                    userDataMap.put("Password",password);
                    userDataMap.put("Name",name);



                    RootRef.child("Users").child(phone).updateChildren(userDataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "Congratulations!! Your Account has been created", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                finish();
                                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(intent);


                            }
                            else
                            {
                                loadingBar.dismiss();
                                Toast.makeText(RegisterActivity.this, "Some Error Occured!! Try Again After Some Time...", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });



                } else
                {
                    Toast.makeText(RegisterActivity.this,phone+" already exists", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "Please try Using Another Phone Number", Toast.LENGTH_SHORT).show();

                    Intent intent= new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        createAccountButton=findViewById(R.id.register_btn);
        InputName=findViewById(R.id.register_username_input);
        InputPhoneNumber=findViewById(R.id.register_phone_numer_input);
        InputPassword=findViewById(R.id.register_password_input);
        loadingBar=new ProgressDialog(this);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });
    }

    private void CreateAccount(){
        String name=InputName.getText().toString();
        String phone=InputPhoneNumber.getText().toString();
        String password=InputPassword.getText().toString();

        if(TextUtils.isEmpty(name)){
            Toast.makeText(this,"Please Enter Your Name....", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phone)){
            Toast.makeText(this,"Please Enter Your Phone Number....", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please Create Your Password....", Toast.LENGTH_SHORT).show();
        }
        else{

            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait while we  are checking the Credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidatePhoneNumber(name,phone,password);
        }
    }


}
