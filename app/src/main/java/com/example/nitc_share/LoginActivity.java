package com.example.nitc_share;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth fAuth;
    EditText etemail, etpass;
    String s_email,s_password;
    TextView tvregister, resetPass;
    Button loginBtn;
    SharedPreferences sharedPreferences;
    private  static  final String SHARED_PREF_NAME = "myPref";
    private  static  final String KEY_EMAIL = "email";
    private  static  final String KEY_PASSWORD= "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        fAuth = FirebaseAuth.getInstance();


        String email = sharedPreferences.getString(KEY_EMAIL,null);
        String password = sharedPreferences.getString(KEY_PASSWORD,null);
        if(email != null && password != null && password != "" && email.endsWith("@nitc.ac.in") && email!=""){
//            ProgressDialog progressDialog = new ProgressDialog(this);
//            progressDialog.setMessage("Logging In...");
//            progressDialog.show();

            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"Invalid Login",Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }

        etemail = findViewById(R.id.etEmail);
        etpass = findViewById(R.id.etPassword);
        loginBtn = findViewById(R.id.loginBtn);
        tvregister = findViewById(R.id.tvregister);
        resetPass = findViewById(R.id.resetPass);

        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPassActivity.class));
            }
        });

        tvregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                s_email = etemail.getText().toString();
                s_password = etpass.getText().toString();

                if( s_email != null && !s_email.endsWith("@nitc.ac.in"))
                {
                    Toast.makeText(getApplicationContext(),"not NITC email",Toast.LENGTH_SHORT).show();
                }

                SharedPreferences.Editor editor= sharedPreferences.edit();
                editor.putString(KEY_EMAIL,s_email);
                editor.putString(KEY_PASSWORD,s_password);
                editor.apply();

                if(s_email.length() == 0){
                    etemail.setError("Please Enter Email");
                    etemail.requestFocus();
                }
                if(s_password.length() == 0){
                    etpass.setError("Please Enter Password");
                    etpass.requestFocus();
                }

                if(s_email != null && !s_email.equals("") && s_email.endsWith("@nitc.ac.in") && s_password != null && !s_password.equals("")){
                    fAuth.signInWithEmailAndPassword(s_email,s_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                if(fAuth.getCurrentUser().isEmailVerified()){
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                }
                                else {
                                    startActivity(new Intent(getApplicationContext(), VerifyEmailActivity.class));
                                }

                            }else{
                                SharedPreferences.Editor editor= sharedPreferences.edit();
                                editor.clear();
                                editor.commit();
                                Toast.makeText(getApplicationContext(),"Invalid Login",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity(); // or finish();
    }
}