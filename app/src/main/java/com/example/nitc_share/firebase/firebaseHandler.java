package com.example.nitc_share.firebase;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class firebaseHandler  extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
