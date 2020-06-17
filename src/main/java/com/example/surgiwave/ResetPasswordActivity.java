package com.example.surgiwave;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.prefs.PreferenceChangeEvent;

import Prevelant.Prevelant;

public class ResetPasswordActivity extends AppCompatActivity {

     private String check="";

     private TextView pageTitle,titleQuestions;
     private EditText phoneNumber,Question1,Question2;
     private Button veriftButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        check=getIntent().getStringExtra("check");

        pageTitle=findViewById(R.id.pg_title);
        titleQuestions=findViewById(R.id.title_question);
        phoneNumber=findViewById(R.id.find_phone_number);
       Question1=findViewById(R.id.question_1);
        Question2=findViewById(R.id.question_2);
        veriftButton=findViewById(R.id.verify);







    }

    @Override
    protected void onStart() {
        super.onStart();




        phoneNumber.setVisibility(View.INVISIBLE);
        if(check.equals("settings")){
            DisplayPreviousAnswers();

            pageTitle.setText("Set Security Questions");

            titleQuestions.setText("Please set Answer for the Security Questions");
            veriftButton.setText("Update");



            veriftButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   setAnswers();
                }
            });



        }
         if(check.equals("login"))
        {

            phoneNumber.setVisibility(View.VISIBLE);
            pageTitle.setText("Answer The Security Questions");
            Question2.setHint("Your Favourite Superhero?");
            Question1.setHint("Where Your Parents met first?");
            veriftButton.setText("Verify");


            veriftButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    VerifyUser();


                }
            });



        }
    }

    private void VerifyUser() {
        final String phone = phoneNumber.getText().toString();
        final String answer1 = Question1.getText().toString().toLowerCase();
        final String answer2 = Question2.getText().toString().toLowerCase();


        if (!phone.equals("") && !answer1.equals("") && !answer2.equals("")) {


            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(phone);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {

                        String mPhone = dataSnapshot.child("Phone").getValue().toString();


                        if (dataSnapshot.hasChild("Security Questions")) {

                            String answet1 = dataSnapshot.child("Security Questions").child("answer1").getValue().toString();
                            String answet2 = dataSnapshot.child("Security Questions").child("answer2").getValue().toString();

                            if (!answet1.equals(answer1)) {

                                Toast.makeText(ResetPasswordActivity.this, "Answer 1 incorrect", Toast.LENGTH_SHORT).show();


                            } else if (!answet2.equals(answer2)) {
                                Toast.makeText(ResetPasswordActivity.this, "Answer 2 incorrect", Toast.LENGTH_SHORT).show();
                            } else {

                                AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
                                builder.setTitle("Enter the new Password");

                                final EditText newPassword = new EditText(ResetPasswordActivity.this);
                                newPassword.setHint("Create Password");
                                builder.setView(newPassword);

                                builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(final DialogInterface dialog, int which) {

                                        if (!newPassword.getText().toString().equals("")) {

                                            ref.child("Password").setValue(newPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()) {

                                                        Toast.makeText(ResetPasswordActivity.this, "Password Changed Sucessfully", Toast.LENGTH_SHORT).show();
                                                        dialog.dismiss();
                                                        finish();


                                                    }


                                                }
                                            });


                                        } else {
                                            Toast.makeText(ResetPasswordActivity.this, "Password can not be Empty", Toast.LENGTH_SHORT).show();
                                        }


                                    }
                                });

                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.cancel();

                                    }
                                });
                                builder.show();

                            }


                        } else {
                            Toast.makeText(ResetPasswordActivity.this, "You Have Not Set Security Questions in Your Account", Toast.LENGTH_SHORT).show();


                        }
                    }
                    else
                    {
                        Toast.makeText(ResetPasswordActivity.this, "Account do not Exist", Toast.LENGTH_SHORT).show();

                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }
        else
        {
            Toast.makeText(this, "All fields are mandatory here,none of those can be empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void setAnswers() {
        String answer1=Question1.getText().toString().toLowerCase();
        String answer2=Question2.getText().toString().toLowerCase();



        if(answer1.equals("") && answer2.equals("")){

            Toast.makeText(ResetPasswordActivity.this, "Answer Both The Questions", Toast.LENGTH_SHORT).show();
        }
        else
        {
            DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Users").child(Prevelant.currentOnlineUser.getPhone());

            HashMap<String,Object> userDataMap= new HashMap<>();
            userDataMap.put("answer1",answer1);
            userDataMap.put("answer2",answer2);
            ref.child("Security Questions").updateChildren(userDataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful()){
                        Toast.makeText(ResetPasswordActivity.this, "Security Questions set Sucessfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }


                }
            });


        }
    }

    private void DisplayPreviousAnswers(){

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Users").child(Prevelant.currentOnlineUser.getPhone());

        ref.child("Security Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    String answet1=dataSnapshot.child("answer1").getValue().toString();
                    String answet2=dataSnapshot.child("answer2").getValue().toString();

                    Question1.setText(answet1);
                    Question2.setText(answet2);
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}
