package com.example.nitc_share;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Toast;

import com.example.nitc_share.adapters.ProductAdapter;
import com.example.nitc_share.adapters.WishlistAdapter;
import com.example.nitc_share.constructors.Products;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class WishlistActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    ArrayList<Products> list;
    ArrayList<String> wishlist;
    WishlistAdapter wishlistAdapter;
    RecyclerView recyclerView;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#047DF5"));
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle("Wishlist");
//        actionBar.hide();

        setContentView(R.layout.activity_wishlist);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Wishlist").child(user.getUid());
        databaseReference.keepSynced(true);
        recyclerView = findViewById(R.id.wishlistRV);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        list = new ArrayList<>();
        wishlist = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    wishlist.clear();
                    list.clear();
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        String s = dataSnapshot.getValue(String.class);
                        wishlist.add(0,s);
                    }

                    if(wishlist.size() == 0){
                        Toast.makeText(getApplicationContext(),"Wishlist is Empty",Toast.LENGTH_SHORT).show();
                        wishlistAdapter = new WishlistAdapter(getApplicationContext(),list);
                        recyclerView.setAdapter(wishlistAdapter);
                        wishlistAdapter.notifyDataSetChanged();
                    }

                    for(String s: wishlist){
//                        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
                        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference("Products");
                        mReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                                        Products products = dataSnapshot.getValue(Products.class);
                                        if(products.getPid().equals(s)){
                                            if(products.getSold().equals("No")){
                                                list.add(0,products);
                                            }
                                        }
                                    }
                                }
                                wishlistAdapter = new WishlistAdapter(getApplicationContext(),list);
                                recyclerView.setAdapter(wishlistAdapter);
                                wishlistAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}