package com.example.nitc_share;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nitc_share.adapters.BidsAdapter;
import com.example.nitc_share.adapters.SelfBidsAdapter;
import com.example.nitc_share.constructors.Bids;
import com.example.nitc_share.constructors.Products;
import com.example.nitc_share.constructors.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class BuyersActivity extends AppCompatActivity {
    String PName,  PDescription,  PPrice,  PImage, PSellerId, Pid, PDate, PTime, PBuyerId, PSold;
    LinearLayout ratingLayout;
    DatabaseReference userReference,productRef;
    User user;
    ArrayList<Bids> list;
    RecyclerView recyclerView;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyers);


        ActionBar actionBar = getSupportActionBar();
        EditText amount  =  findViewById(R.id.amount);
        ImageView tvP_Image = findViewById(R.id.tvP_Image);
//        TextView tvP_Name =findViewById(R.id.tvP_Name);
        TextView tvP_Base = findViewById(R.id.tvP_Base);
        TextView tvP_Description =findViewById(R.id.tvP_Description);
        TextView tvP_Price =findViewById(R.id.tvP_Price);
        TextView tvP_Seller = findViewById(R.id.tvP_Seller);
//        TextView tvP_Buyer = findViewById(R.id.tvP_Buyer);
        Button placeBid = findViewById(R.id.placeBid);
        Button rate = findViewById(R.id.rate);
//        TextView buyerRating = findViewById(R.id.tvP_BuyerRating);
        RatingBar sellerRating = findViewById(R.id.tvP_SellerRating);
        ratingLayout = findViewById(R.id.rating_layout);
        ratingBar = findViewById(R.id.ratingbar);
        recyclerView = findViewById(R.id.bid_history);
        LinearLayout bids = findViewById(R.id.bids);
        LinearLayout details = findViewById(R.id.linearLayout3);
        LinearLayout sellerLayout = findViewById(R.id.sellerLayout);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        pname, description, price, image, category, pid, date, time, sellerid, buyerid;

        Pid = getIntent().getStringExtra("Pid");
        PName = getIntent().getStringExtra("Name");
        PPrice = getIntent().getStringExtra("Price");
        PDescription = getIntent().getStringExtra("Description");
        PImage = getIntent().getStringExtra("Image");
        PSellerId = getIntent().getStringExtra("Seller");
        PBuyerId = getIntent().getStringExtra("Buyerid");
        PDate = getIntent().getStringExtra("Date");
        PTime = getIntent().getStringExtra("Time");
        actionBar.setTitle(PName);
//        PSold = "No";

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        tvP_Price.setText(PPrice);
        tvP_Description.setText(PDescription);
        Glide.with(getApplicationContext()).load(PImage).into(tvP_Image);

        productRef = FirebaseDatabase.getInstance().getReference("Products").child(Pid);
        productRef.keepSynced(true);
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Products products = snapshot.getValue(Products.class);

                PSold = products.getSold();
                tvP_Base.setText(products.getBaseprice());

                userReference = FirebaseDatabase.getInstance().getReference("Users").child(products.getSellerid());
                userReference.keepSynced(true);
                userReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        tvP_Seller.setText(user.getName());
                        sellerRating.setRating(user.getRating());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                if(PSold.equals("Yes")){
                    if(placeBid.getVisibility() != View.GONE){
                        placeBid.setVisibility(View.GONE);
                    }

                    if(amount.getVisibility() != View.GONE){
                        amount.setVisibility(View.GONE);
                    }

                    if(currentUser.getUid().equals(products.getBuyerid())){
                        if(products.getBuyerrated().equals("Yes")){
                            if(ratingLayout.getVisibility() != View.GONE){
                                ratingLayout.setVisibility(View.GONE);
                            }
                        }else{
                            if(ratingLayout.getVisibility() != View.VISIBLE){
                                ratingLayout.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    if(currentUser.getUid().equals(products.getSellerid())){
                        if(products.getSellerrated().equals("Yes")){
                            if(ratingLayout.getVisibility() != View.GONE){
                                ratingLayout.setVisibility(View.GONE);
                            }
                        }
                    }
                }

                if(PSold.equals("No")){
                    if(ratingLayout.getVisibility() != View.GONE){
                        ratingLayout.setVisibility(View.GONE);
                    }
                }




                if(currentUser.getUid().equals(products.getBuyerid()) || products.getBuyerid().equals("null")){
                    if(details.getVisibility() != View.VISIBLE){
                        details.setVisibility(View.VISIBLE);
                    }
                    if(products.getBuyerid().equals("null")){
                        if(bids.getVisibility() != View.GONE){
                            bids.setVisibility(View.GONE);
                        }
                    }
                    userReference = FirebaseDatabase.getInstance().getReference("Users").child(products.getSellerid());
                    userReference.keepSynced(true);
                    userReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user = snapshot.getValue(User.class);
                            tvP_Seller.setText(user.getName());
                            sellerRating.setRating(user.getRating());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                if(currentUser.getUid().equals(products.getSellerid())){
                    if(products.getBuyerid().equals("null")){
                        if(details.getVisibility() != View.GONE){
                            details.setVisibility(View.GONE);
                        }
                        if(bids.getVisibility() != View.GONE){
                            bids.setVisibility(View.GONE);
                        }
                    }else{
                        if(details.getVisibility() != View.VISIBLE){
                            details.setVisibility(View.VISIBLE);
                        }
                        if(bids.getVisibility() != View.VISIBLE){
                            bids.setVisibility(View.VISIBLE);
                        }
                        userReference = FirebaseDatabase.getInstance().getReference("Users").child(products.getBuyerid());
                        userReference.keepSynced(true);
                        userReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                User user = snapshot.getValue(User.class);
                                tvP_Seller.setText(user.getName());
                                sellerRating.setRating(user.getRating());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }


                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sellerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productRef.addValueEventListener(new ValueEventListener() {
                    Intent intent =  new Intent(getApplicationContext(), ProfileActivity.class);
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Products products = snapshot.getValue(Products.class);
                        if(products.getSold().equals("Yes")){
                            if(products.getBuyerid().equals(currentUser.getUid())){
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(products.getSellerid());
                                ref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        User user = snapshot.getValue(User.class);
                                        intent.putExtra("name", user.getName());
                                        intent.putExtra("email", user.getEmail());
                                        intent.putExtra("rating", user.getRating());
                                        intent.putExtra("phone", user.getPhone());
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        placeBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                Toast.makeText(BuyersActivity.this, amount.getText(), Toast.LENGTH_SHORT).show();

                if(bids.getVisibility() != View.VISIBLE){
                    bids.setVisibility(View.VISIBLE);
                }

//                if(!amount.getText().toString().equals("") && amount.getText()!=null) {
//                    int amt = Integer.parseInt(amount.getText().toString());
////                    Toast.makeText(BuyersActivity.this, "Base Price : " + p.getPrice(), Toast.LENGTH_SHORT).show();
//                    Toast.makeText(BuyersActivity.this, "Input Price : " + amt, Toast.LENGTH_SHORT).show();
//                }

                userReference = FirebaseDatabase.getInstance().getReference().child("Products");
                userReference.keepSynced(true);
                userReference.child(Pid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Products p = snapshot.getValue(Products.class);

                            int i=Integer.parseInt(p.getPrice());

                            if(!amount.getText().toString().equals("") && amount.getText()!=null){
                                int amt = Integer.parseInt(amount.getText().toString());
//                                Toast.makeText(BuyersActivity.this, "Base Price : " + p.getPrice(), Toast.LENGTH_SHORT).show();
//                                Toast.makeText(BuyersActivity.this, "Input Price : " + amt, Toast.LENGTH_SHORT).show();
                                if(amt>i){

                                    PPrice = Integer.toString(amt);
                                    tvP_Price.setText(PPrice);

                                    userReference.child(Pid).child("price").setValue(PPrice);
                                    userReference.child(Pid).child("buyerid").setValue(currentUser.getUid());

                                    userReference = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());
                                    userReference.keepSynced(true);
                                    userReference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            User user = snapshot.getValue(User.class);
//                        tvP_Buyer.setText(user.getName());

                                            //BIDDER'S LIST

                                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Bids").child(Pid);
                                            databaseReference.keepSynced(true);
                                            list = new ArrayList<>();
                                            databaseReference.addValueEventListener(new ValueEventListener() {

                                                Boolean progress = false;
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (!progress) {
                                                        list = new ArrayList<>();
                                                        list.clear();
                                                        for (DataSnapshot ds: snapshot.getChildren()) {
                                                            list.add(ds.getValue(Bids.class));
                                                        }

                                                        Bids bids = new Bids();
                                                        if(list.isEmpty()){
                                                            bids.setBidCount(1);
                                                        }else{
                                                            Bids lastBid = list.get(list.size()-1);
                                                            bids.setBidCount(lastBid.getBidCount()+1);
                                                        }

                                                        bids.setBidderid(currentUser.getUid());
                                                        bids.setPrice(PPrice);
                                                        list.add(bids);

                                                        databaseReference.setValue(list).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
                                                                }else{
                                                                    Toast.makeText(getApplicationContext(),"Fail",Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });

                                                        RecyclerView recyclerView1 = findViewById(R.id.bid_history);
                                                        SelfBidsAdapter bidsAdapter = new SelfBidsAdapter(BuyersActivity.this, list, Pid);
                                                        recyclerView1.setAdapter(bidsAdapter);
                                                        bidsAdapter.notifyDataSetChanged();

                                                        list.clear();
                                                        progress = true;
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    amount.setText("");
                                }
                                else
                                {
                                    Toast.makeText(BuyersActivity.this, "Please Enter Price Greater than Current Price!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currUid = currentUser.getUid();
                productRef = FirebaseDatabase.getInstance().getReference("Products").child(Pid);
                productRef.keepSynced(true);
                productRef.addValueEventListener(new ValueEventListener() {
                    boolean progress = false;
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(!progress && snapshot.exists()){

                            Products products = snapshot.getValue(Products.class);
                            String seller = products.getSellerid();
                            String buyer = products.getBuyerid();
                            if(currUid.equals(buyer)){
                                //buyer rates to seller
                                userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(seller);
                                userReference.keepSynced(true);
                                userReference.addValueEventListener(new ValueEventListener() {

                                    boolean progressdone = false;
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(!progressdone){
                                            user = snapshot.getValue(User.class);
                                            float currRating = user.getRating();
                                            int count = user.getRatecount();

                                            FirebaseDatabase.getInstance().getReference("Users").child(seller).child("rating").setValue(((currRating * count)+ratingBar.getRating())/(count +1));
                                            FirebaseDatabase.getInstance().getReference("Users").child(seller).child("ratecount").setValue(count + 1);
                                            FirebaseDatabase.getInstance().getReference("Products").child(Pid).child("buyerrated").setValue("Yes");

                                            progressdone = true;
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                            progress = true;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }



    public static void deleteBid(ArrayList<Bids> list1, String Pid){
        FirebaseUser curruser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Bids").child(Pid);
        databaseReference.keepSynced(true);
        databaseReference.setValue(list1);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Bids").child(Pid);
        reference.addValueEventListener(new ValueEventListener() {
            boolean progress = false;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Products").child(Pid);
                    ref.addValueEventListener(new ValueEventListener() {
                        boolean progress = false;
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists() && !progress){
                                Products products = snapshot.getValue(Products.class);
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Products").child(Pid);
                                databaseReference.child("price").setValue(products.getBaseprice());
                                databaseReference.child("buyerid").setValue("null");
                                progress = true;
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
                for(DataSnapshot ds: snapshot.getChildren()){
                    Bids bids = ds.getValue(Bids.class);
                    if(!progress){
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Products").child(Pid);
                        databaseReference.child("price").setValue(bids.getPrice());
                    }
                    progress = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return;
    }
    @Override
    protected void onStart() {
        super.onStart();

        LinearLayout BidsLL = findViewById(R.id.bids);

        FirebaseUser curruser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Bids").child(Pid);
        databaseReference.keepSynced(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                list.clear();
                int curr = 0;
                int prev = 0;
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Bids bids = ds.getValue(Bids.class);
                    if (curruser.getUid().equals(bids.getBidderid())) {
                        list.add(0, bids);
                        if(bids.getBidCount()==1) {
                            curr = Integer.parseInt(bids.getPrice());
                        }else {
                            prev = curr;
                            curr = Integer.parseInt(bids.getPrice());
                            if(prev == curr){
                                list.remove(bids);
                            }
                        }
                        FirebaseDatabase.getInstance().getReference("Products").child(Pid).child("price").setValue(bids.getPrice());
                    }
                }
                RecyclerView recyclerView1 = findViewById(R.id.bid_history);
                if(list.size() == 0){
                    if(BidsLL.getVisibility() != View.INVISIBLE){
                        BidsLL.setVisibility(View.INVISIBLE);
                    }
                }else{
                    if(BidsLL.getVisibility() != View.VISIBLE){
                        BidsLL.setVisibility(View.VISIBLE);
                    }
                }
                Set<Bids> set = new HashSet<>(list);
                list.clear();
                list.addAll(set);
                for(Bids b: list){
                    Toast.makeText(BuyersActivity.this, b.getPrice(), Toast.LENGTH_SHORT).show();
                }
                SelfBidsAdapter bidsAdapter = new SelfBidsAdapter(BuyersActivity.this, list, Pid);
                recyclerView1.setAdapter(bidsAdapter);
                bidsAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}