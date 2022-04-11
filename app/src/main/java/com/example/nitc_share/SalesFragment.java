package com.example.nitc_share;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nitc_share.adapters.ProductAdapter;
import com.example.nitc_share.adapters.SPAdapter;
import com.example.nitc_share.constructors.Products;
import com.example.nitc_share.constructors.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SalesFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public SalesFragment() {
        // Required empty public constructor
    }

    public static SalesFragment newInstance(String param1, String param2) {
        SalesFragment fragment = new SalesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    ArrayList<Products> list;
    ProductAdapter productAdapter;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    androidx.appcompat.widget.SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sales, container, false);
        recyclerView = view.findViewById(R.id.history);
        databaseReference = FirebaseDatabase.getInstance().getReference("Products");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        searchView = view.findViewById(R.id.search_history);

        return view;
    }

    @Override
    public void onStart() {
        if(databaseReference!= null){
            list = new ArrayList<>();
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        list.clear();
                        int cnt = 0;
                        for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                            Products products = dataSnapshot.getValue(Products.class);
                            if(products.getSellerid().equals(user.getUid())){
                                list.add(0,products);
                                if(products.getSold().equals("Yes")){
                                    cnt = cnt + 1;
                                }
                            }
                        }
                        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Users");
                        ref1 = ref1.child(user.getUid());
                        int finalCnt = cnt;
                        ref1.addValueEventListener(new ValueEventListener() {
                            boolean flag = false;
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(!flag){
                                    User user1 = snapshot.getValue(User.class);
                                    if(user1 != null){
                                        FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("sold").setValue(finalCnt);
                                        flag = true;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        SPAdapter spAdapter = new SPAdapter(getContext(),list);
                        recyclerView.setAdapter(spAdapter);
                        spAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
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
                                    if(products.getSellerid().equals(user.getUid())){

                                        if(products.getPname().toLowerCase().contains(newText.toLowerCase()) || products.getDescription().toLowerCase().contains(newText.toLowerCase()))
                                        {
                                            list.add(0,products);
                                        }
                                    }
                                }

                                SPAdapter spAdapter = new SPAdapter(getContext(),list);
                                recyclerView.setAdapter(spAdapter);
                                spAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    return true;
                }
            });
        }
        super.onStart();
    }
}