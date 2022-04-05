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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassActivity extends AppCompatActivity {

    EditText etemail;
    String s_email;
    Button sendBtn;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);

        etemail = findViewById(R.id.etEmail);
        sendBtn = findViewById(R.id.sendBtn);
        firebaseAuth = FirebaseAuth.getInstance();


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s_email = etemail.getText().toString();

                if(s_email.length() == 0){
                    etemail.setError("Please Enter Email");
                    etemail.requestFocus();
                }else{
                    firebaseAuth.sendPasswordResetEmail(s_email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ResetPassActivity.this,"Check Email",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ResetPassActivity.this, LoginActivity.class));
                            }
                        }
                    });
                }
            }
        });

    }
}