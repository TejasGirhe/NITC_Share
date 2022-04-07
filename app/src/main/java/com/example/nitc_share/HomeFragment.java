package com.example.nitc_share;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.CompoundButtonCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.nitc_share.adapters.ProductAdapter;
import com.example.nitc_share.constructors.Products;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class HomeFragment extends Fragment{
    public HomeFragment() {
        // Required empty public constructor
    }
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    ArrayList<String> categories;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    ArrayList<Products> list;
    ProductAdapter productAdapter;
    androidx.appcompat.widget.SearchView searchView;
    androidx.appcompat.widget.AppCompatImageButton filter;
    LinearLayout horizontalScrollView, actionBar, productView, parent;
    ImageButton wishlist;
    CheckBox mobiles, electronics, fashion, stationary, books, furniture, appliances;
    LinearLayout bikes;
    boolean bike = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        recyclerView = view.findViewById(R.id.all_products);
        databaseReference = FirebaseDatabase.getInstance().getReference("Products");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchView = view.findViewById(R.id.search_bar);

        horizontalScrollView = view.findViewById(R.id.HScroll_View);
        actionBar = view.findViewById(R.id.actionBar);
        productView = view.findViewById(R.id.products);
        wishlist = view.findViewById(R.id.wishlistView);
        bikes = view.findViewById(R.id.bikes);
        mobiles = view.findViewById(R.id.mobiles);
        electronics = view.findViewById(R.id.electronics);
        fashion = view.findViewById(R.id.fashion);
        stationary = view.findViewById(R.id.stationary);
        books = view.findViewById(R.id.books);
        furniture = view.findViewById(R.id.furniture);
        appliances = view.findViewById(R.id.appliances);

        filter = view.findViewById(R.id.filter);
        horizontalScrollView.setVisibility(View.GONE);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(horizontalScrollView.getVisibility() == View.GONE){
                    horizontalScrollView.setVisibility(View.VISIBLE);
                } else if(horizontalScrollView.getVisibility() == View.VISIBLE) {
                    horizontalScrollView.setVisibility(View.GONE);
                }
            }
        });

        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),WishlistActivity.class));
            }
        });


        categories = new ArrayList<>();
        categories.clear();

        ColorStateList darkStateList = ContextCompat.getColorStateList(getContext(), R.color.white);
        CheckBox bikes1 = view.findViewById(R.id.bikes1);
        CompoundButtonCompat.setButtonTintList(bikes1, darkStateList);

//        bikes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                bike = !bike;
//                if(bike){
//                    categories.add(0,"Bikes");
//                    ColorStateList darkStateList = ContextCompat.getColorStateList(getContext(), R.color.teal_200);
//                    CompoundButtonCompat.setButtonTintList(bikes1, darkStateList);
//                    filter(categories);
//                }else{
//                    categories.remove("Bikes");
//                    ColorStateList darkStateList = ContextCompat.getColorStateList(getContext(), R.color.white);
//                    CompoundButtonCompat.setButtonTintList(bikes1, darkStateList);
//                    if(categories.size() == 0){
//                        viewAll();
//                    }else{
//                        filter(categories);
//                    }
//                }
//            }
//        });
        bikes1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(bikes1.isChecked()){
                    categories.add(0,"Bikes");
                    ColorStateList darkStateList = ContextCompat.getColorStateList(getContext(), R.color.teal_200);
                    CompoundButtonCompat.setButtonTintList(bikes1, darkStateList);
                    filter(categories);
                }else{
                    categories.remove("Bikes");
                    ColorStateList darkStateList = ContextCompat.getColorStateList(getContext(), R.color.white);
                    CompoundButtonCompat.setButtonTintList(bikes1, darkStateList);
                    if(categories.size() == 0){
//                        Toast.makeText(getContext(), "No such product in this category", Toast.LENGTH_SHORT).show();
                        viewAll();
                    }else{
                        filter(categories);
                    }
                }
            }
        });

        darkStateList = ContextCompat.getColorStateList(getContext(), R.color.white);
        CompoundButtonCompat.setButtonTintList(mobiles, darkStateList);
        mobiles.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(mobiles.isChecked()){
                    categories.add(0,"Mobile");
                    ColorStateList darkStateList = ContextCompat.getColorStateList(getContext(), R.color.teal_200);
                    CompoundButtonCompat.setButtonTintList(mobiles, darkStateList);
                    filter(categories);
                }else{
                    categories.remove("Mobile");
                    ColorStateList darkStateList = ContextCompat.getColorStateList(getContext(), R.color.white);
                    CompoundButtonCompat.setButtonTintList(mobiles, darkStateList);
                    if(categories.size() == 0){
//                        Toast.makeText(getContext(), "No such product in this category", Toast.LENGTH_SHORT).show();
                        viewAll();
                    }else{
                        filter(categories);
                    }
                }
            }
        });

        darkStateList = ContextCompat.getColorStateList(getContext(), R.color.white);
        CompoundButtonCompat.setButtonTintList(electronics, darkStateList);
        electronics.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(electronics.isChecked()){
                    categories.add(0,"Electronics");
                    ColorStateList darkStateList = ContextCompat.getColorStateList(getContext(), R.color.teal_200);
                    CompoundButtonCompat.setButtonTintList(electronics, darkStateList);
                    filter(categories);
                }else{
                    categories.remove("Electronics");
                    ColorStateList darkStateList = ContextCompat.getColorStateList(getContext(), R.color.white);
                    CompoundButtonCompat.setButtonTintList(electronics, darkStateList);
                    if(categories.size() == 0){
                        viewAll();
                    }else{
                        filter(categories);
                    }
                }
            }
        });
        darkStateList = ContextCompat.getColorStateList(getContext(), R.color.white);
        CompoundButtonCompat.setButtonTintList(fashion, darkStateList);
        fashion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(fashion.isChecked()){
                    categories.add(0,"Fashion");
                    ColorStateList darkStateList = ContextCompat.getColorStateList(getContext(), R.color.teal_200);
                    CompoundButtonCompat.setButtonTintList(fashion, darkStateList);
                    filter(categories);
                }else{
                    categories.remove("Fashion");
                    ColorStateList darkStateList = ContextCompat.getColorStateList(getContext(), R.color.white);
                    CompoundButtonCompat.setButtonTintList(fashion, darkStateList);
                    if(categories.size() == 0){
//                        Toast.makeText(getContext(), "No such product in this category", Toast.LENGTH_SHORT).show();
                        viewAll();
                    }else{
                        filter(categories);
                    }
                }
            }
        });
        //        "Mobile", "Bikes", "Electronics","Fashion", "Stationary","Books","Furniture", " Appliances"
        darkStateList = ContextCompat.getColorStateList(getContext(), R.color.white);
        CompoundButtonCompat.setButtonTintList(stationary, darkStateList);
        stationary.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(stationary.isChecked()){
                    categories.add(0,"Stationary");
                    ColorStateList darkStateList = ContextCompat.getColorStateList(getContext(), R.color.teal_200);
                    CompoundButtonCompat.setButtonTintList(stationary, darkStateList);
                    filter(categories);
                }else{
                    categories.remove("Stationary");
                    ColorStateList darkStateList = ContextCompat.getColorStateList(getContext(), R.color.white);
                    CompoundButtonCompat.setButtonTintList(stationary, darkStateList);
                    if(categories.size() == 0){
//                        Toast.makeText(getContext(), "No such product in this category", Toast.LENGTH_SHORT).show();
                        viewAll();
                    }else{
                        filter(categories);
                    }
                }
            }
        });
        darkStateList = ContextCompat.getColorStateList(getContext(), R.color.white);
        CompoundButtonCompat.setButtonTintList(books, darkStateList);
        books.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(books.isChecked()){
                    categories.add(0,"Books");
                    ColorStateList darkStateList = ContextCompat.getColorStateList(getContext(), R.color.teal_200);
                    CompoundButtonCompat.setButtonTintList(books, darkStateList);
                    filter(categories);
                }else{
                    categories.remove("Books");
                    ColorStateList darkStateList = ContextCompat.getColorStateList(getContext(), R.color.white);
                    CompoundButtonCompat.setButtonTintList(books, darkStateList);
                    if(categories.size() == 0){
//                        Toast.makeText(getContext(), "No such product in this category", Toast.LENGTH_SHORT).show();
                        viewAll();
                    }else{
                        filter(categories);
                    }
                }
            }
        });
        darkStateList = ContextCompat.getColorStateList(getContext(), R.color.white);
        CompoundButtonCompat.setButtonTintList(furniture, darkStateList);
        furniture.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(furniture.isChecked()){
                    categories.add(0,"Furniture");
                    ColorStateList darkStateList = ContextCompat.getColorStateList(getContext(), R.color.teal_200);
                    CompoundButtonCompat.setButtonTintList(furniture, darkStateList);
                    filter(categories);
                }else{
                    categories.remove("Furniture");
                    ColorStateList darkStateList = ContextCompat.getColorStateList(getContext(), R.color.white);
                    CompoundButtonCompat.setButtonTintList(furniture, darkStateList);
                    if(categories.size() == 0){
//                        Toast.makeText(getContext(), "No such product in this category", Toast.LENGTH_SHORT).show();
                        viewAll();
                    }else{
                        filter(categories);
                    }
                }
            }
        });
        darkStateList = ContextCompat.getColorStateList(getContext(), R.color.white);
        CompoundButtonCompat.setButtonTintList(appliances, darkStateList);
        appliances.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(appliances.isChecked()){
                    categories.add(0,"Appliances");
                    ColorStateList darkStateList = ContextCompat.getColorStateList(getContext(), R.color.teal_200);
                    CompoundButtonCompat.setButtonTintList(appliances, darkStateList);
                    filter(categories);
                }else{
                    categories.remove("Appliances");
                    ColorStateList darkStateList = ContextCompat.getColorStateList(getContext(), R.color.white);
                    CompoundButtonCompat.setButtonTintList(appliances, darkStateList);
                    if(categories.size() == 0){
//                        Toast.makeText(getContext(), "No such product in this category", Toast.LENGTH_SHORT).show();
                        viewAll();
                    }else{
                        filter(categories);
                    }
                }
            }
        });
        return view;
    }

    void filter(ArrayList<String> categories){
        list = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    list.clear();
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        Products products = dataSnapshot.getValue(Products.class);
                        if(products.getSold().equals("No")){
                            for(String c : categories){
                                if(products.getCategory().equals(c)){
                                    list.add(0,products);
                                }
                            }
                        }
                    }
                    productAdapter = new ProductAdapter(getContext(),list);
                    recyclerView.setAdapter(productAdapter);
                    productAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.keepSynced(true);
    }
    void viewAll(){
        list = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    list.clear();
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        Products products = dataSnapshot.getValue(Products.class);
                        if(products.getSold().equals("No")){
                            list.add(0,products);
                        }
                    }
                    productAdapter = new ProductAdapter(getContext(),list);
                    recyclerView.setAdapter(productAdapter);
                    productAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.keepSynced(true);
    }


    @Override
    public void onStart() {

        if(databaseReference!= null){
            list = new ArrayList<>();
            databaseReference = FirebaseDatabase.getInstance().getReference("Products");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        list.clear();
                        for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                            Products products = dataSnapshot.getValue(Products.class);
                            if(products.getSold().equals("No")){
                                list.add(0,products);
                            }
                        }

                        productAdapter = new ProductAdapter(getContext(),list);
                        recyclerView.setAdapter(productAdapter);
                        productAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            databaseReference.keepSynced(true);
        }
        if(searchView != null){
            searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText)
                {

                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                list.clear();
                                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                                    Products products = dataSnapshot.getValue(Products.class);
                                    if(products.getPname().toLowerCase().contains(newText.toLowerCase()) || products.getDescription().toLowerCase().contains(newText.toLowerCase()))
                                    {
                                        if(products.getSold().equals("No")){
                                            if(categories.size()!=0){
                                                for(String s : categories){
                                                    if(s.equals(products.getCategory())){
                                                        list.add(0,products);
                                                    }
                                                }
                                            }else{
                                                list.add(0,products);
                                            }
                                        }

                                    }
                                }
                                if(list.isEmpty() && !newText.equals("")){
                                    Toast.makeText(getContext(), "Product not Found", Toast.LENGTH_SHORT).show();
                                }
                                productAdapter = new ProductAdapter(getContext(),list);
                                recyclerView.setAdapter(productAdapter);
                                productAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    databaseReference.keepSynced(true);
                    return true;
                }
            });
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Products");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                        Products products = dataSnapshot.getValue(Products.class);

                        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                        Calendar c = Calendar.getInstance();
                        c.add(Calendar.DATE, 0);

                        if(products.getDeleteOn().equals(dateFormat.format(c.getTime()))){
                            FirebaseDatabase.getInstance().getReference("Products").child(products.getPid()).removeValue();
                            FirebaseDatabase.getInstance().getReference("Bids").child(products.getPid()).removeValue();
                            FirebaseStorage.getInstance().getReferenceFromUrl(products.getImage()).delete();
                        }

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.keepSynced(true);
        super.onStart();
    }

}