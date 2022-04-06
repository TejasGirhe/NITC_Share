package com.example.nitc_share;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    String name, email, phone;
    TextView nametv, emailtv, phonetv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        phone = getIntent().getStringExtra("phone");
        nametv = findViewById(R.id.tvName);
        emailtv = findViewById(R.id.tvEmail);
        phonetv = findViewById(R.id.tvPhone);

        nametv.setText(name);
        emailtv.setText(email);
        phonetv.setText(phone);

//        getIntent().getStringExtra("rating");
    }
}