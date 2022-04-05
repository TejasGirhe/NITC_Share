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
                if(sName.length() == 0){
                    etName.setError("Please Enter Name");
                    etName.requestFocus();
                }
                sEmail = etEmail.getText().toString();
                if(!sEmail.endsWith("@nitc.ac.in")){
                    etEmail.setError("Please Enter Valid NITC Email");
                    etEmail.requestFocus();
                }
                sPhone = etPhone.getText().toString();
                if(sPhone.length() != 10){
                    etPhone.setError("Please Enter Correct Number");
                    etPhone.requestFocus();
                }
                sPassword = etPassword.getText().toString();
                if(sPassword.length() < 8){
                    etPassword.setError("Please Enter password with more than 8 letters");
                    etPassword.requestFocus();
                }
                sCpassword = etCPassword.getText().toString();
                if(!sCpassword.equals(sPassword)){
                    etCPassword.setError("Password not matching");
                    etCPassword.requestFocus();
                }


                User user = new User();
                user.setEmail(sEmail);
                user.setName(sName);
                user.setPassword(sPassword);
                user.setPhone(sPhone);
                user.setRating(Float.parseFloat("5"));
                user.setRatecount(1);


                if(sEmail.endsWith("@nitc.ac.in")){
                    firebaseAuth.createUserWithEmailAndPassword(sEmail,sPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"Registration Success",Toast.LENGTH_SHORT).show();
                                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(getApplicationContext(),"Registration Success",Toast.LENGTH_SHORT).show();
                                            firebaseAuth.getCurrentUser().sendEmailVerification();
                                            startActivity(new Intent(getApplicationContext(),VerifyEmailActivity.class));
                                        }else{
                                            Toast.makeText(getApplicationContext(),"Registration failed",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Registration failed",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"not NITC",Toast.LENGTH_SHORT).show();
                }


            }
        });



    }
}