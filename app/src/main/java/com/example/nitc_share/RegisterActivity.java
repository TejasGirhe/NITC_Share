package com.example.nitc_share;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nitc_share.constructors.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    EditText etName,etPhone,etEmail,etPassword,etCPassword;
    TextView tvLogin;
    Button registerBtn;

    FirebaseAuth firebaseAuth;

    String sName, sEmail, sPhone, sPassword, sCpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etCPassword = findViewById(R.id.etC_Password);
        etPhone = findViewById(R.id.etPhone);
        registerBtn = findViewById(R.id.registerBtn);
        tvLogin = findViewById(R.id.tvlogin);

        firebaseAuth = FirebaseAuth.getInstance();

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sName = etName.getText().toString();
                sEmail = etEmail.getText().toString();
                sPhone = etPhone.getText().toString();
                sPassword = etPassword.getText().toString();
                sCpassword = etCPassword.getText().toString();


                if(sName.length() == 0){
                    etName.setError("Please Enter Name");
                    etName.requestFocus();
                }
                else if(!sEmail.endsWith("@nitc.ac.in")){
                    etEmail.setError("Please Enter Valid NITC Email");
                    etEmail.requestFocus();
                }
                else if(sPhone.length() != 10){
                    etPhone.setError("Please Enter Correct Number");
                    etPhone.requestFocus();
                }
                else if(sPassword.length() < 8){
                    etPassword.setError("Please Enter password with more than 8 letters");
                    etPassword.requestFocus();
                }
                else if(!sCpassword.equals(sPassword)){
                    etCPassword.setError("Password not matching");
                    etCPassword.requestFocus();
                } else {
                    User user = new User();
                    user.setEmail(sEmail);
                    user.setName(sName);
                    user.setPassword(sPassword);
                    user.setPhone(sPhone);
                    user.setRating(Float.parseFloat("5"));
                    user.setRatecount(1);
                    user.setBought(0);
                    user.setSold(0);

                    if(sEmail.endsWith("@nitc.ac.in")){
                        firebaseAuth.createUserWithEmailAndPassword(sEmail,sPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(getApplicationContext(),"Registration Success",Toast.LENGTH_SHORT).show();
                                                firebaseAuth.getCurrentUser().sendEmailVerification();
                                                startActivity(new Intent(getApplicationContext(),VerifyEmailActivity.class));
                                            }else{
                                                Toast.makeText(getApplicationContext(),"Registration Failed",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Please Enter NITC Email",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }
}