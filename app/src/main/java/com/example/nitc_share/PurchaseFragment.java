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
import android.widget.Toast;

import com.example.nitc_share.adapters.ProductAdapter;
import com.example.nitc_share.adapters.SPAdapter;
import com.example.nitc_share.constructors.Bids;
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

public class PurchaseFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public PurchaseFragment() {
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
        View view = inflater.inflate(R.layout.fragment_purchase, container, false);

        recyclerView = view.findViewById(R.id.history);
        databaseReference = FirebaseDatabase.getInstance().getReference("Products");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),1);
//        recyclerView.setLayoutManager(layoutManager);
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
                boolean progress = false;
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists() && !progress){
                        list.clear();
                        for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                            Products products = dataSnapshot.getValue(Products.class);
                            int count = 0;

                            if(products.getSold().equals("Yes") && products.getBuyerid().equals(user.getUid())){
                                count = count + 1;
                            }
                            DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Users");
                            ref2 = ref2.child(products.getBuyerid());
                            int finalCount = count;
                            ref2.addValueEventListener(new ValueEventListener() {
                                boolean flag = false;
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(!flag){
                                        User user = snapshot.getValue(User.class);
                                        if(user != null){
                                            FirebaseDatabase.getInstance().getReference("Users").child(products.getBuyerid()).child("bought").setValue(finalCount);
                                            flag = true;
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Bids").child(products.getPid());
                            ref.addValueEventListener(new ValueEventListener() {
                                boolean flag = true;
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(flag){
                                        Bids bids;
                                        for(DataSnapshot ds: snapshot.getChildren()){
                                            bids = ds.getValue(Bids.class);
                                            if(bids.getBidderid().equals(user.getUid()) && products.getSold().equals("No")){
                                                list.remove(products);
                                                list.add(0, products);
                                            }
                                            else if(products.getSold().equals("Yes") && products.getBuyerid().equals(user.getUid())){
                                                list.remove(products);
                                                list.add(0,products);
                                            }

                                        }

                                        SPAdapter spAdapter = new SPAdapter(getContext(),list);
                                        recyclerView.setAdapter(spAdapter);
                                        spAdapter.notifyDataSetChanged();
                                        flag = false;
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

//                            if(products.getBuyerid().equals(user.getUid()) || products.getBuyerid().equals(null)){
//                                list.add(0,products);
//                            }
                        }
                        progress = true;
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
                                    if(products.getBuyerid().equals(user.getUid()) || products.getBuyerid().equals(null)){

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