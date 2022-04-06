package com.example.nitc_share;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nitc_share.adapters.BidsAdapter;
import com.example.nitc_share.constructors.Bids;
import com.example.nitc_share.constructors.Products;
import com.example.nitc_share.constructors.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class AdvertActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_advert);

        LinearLayout layout = findViewById(R.id.linearLayout3);

        ActionBar actionBar = getSupportActionBar();

        ImageView tvP_Image = findViewById(R.id.tvP_Image);
//        TextView tvP_Name =findViewById(R.id.tvP_Name);
        TextView tvP_Base = findViewById(R.id.tvP_Base);
        TextView tvP_Description =findViewById(R.id.tvP_Description);
        TextView tvP_Price =findViewById(R.id.tvP_Price);
        TextView tvP_Seller = findViewById(R.id.tvP_Seller);
//        TextView tvP_Buyer = findViewById(R.id.tvP_Buyer);
        Button deleteAd = findViewById(R.id.deleteAd);
        Button finaliseAd = findViewById(R.id.finaliseAd);
        Button rate = findViewById(R.id.rate);
//        TextView buyerRating = findViewById(R.id.tvP_BuyerRating);
        RatingBar sellerRating = findViewById(R.id.tvP_SellerRating);
        ratingLayout = findViewById(R.id.rating_layout);
        ratingBar = findViewById(R.id.ratingbar);
        recyclerView = findViewById(R.id.bid_history);
        LinearLayout bids = findViewById(R.id.bids);
        LinearLayout details = findViewById(R.id.linearLayout3);
        LinearLayout buttonslayout = findViewById(R.id.buttons_layout);

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

        // soldornot
        // sold no p,f,d show rate if rated hide
        // not sold hide rate c=s show f,d or c=t show p

        productRef = FirebaseDatabase.getInstance().getReference("Products").child(Pid);
        productRef.keepSynced(true);
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Products products = snapshot.getValue(Products.class);

                if(!products.getSold().equals(null)){
                    PSold = products.getSold();//might be null
                }else{
                    PSold = "No";//might be null
                }
                tvP_Base.setText(products.getBaseprice());
                tvP_Price.setText(products.getPrice());

                Toast.makeText(getApplicationContext(),String.valueOf(finaliseAd.getVisibility()),Toast.LENGTH_SHORT).show();

                if(PSold.equals("Yes")){
                    if(buttonslayout.getVisibility() != View.GONE){
                        buttonslayout.setVisibility(View.GONE);
                    }
                    if(products.getSellerrated().equals("Yes")){
                        if(ratingLayout.getVisibility() != View.GONE){
                            ratingLayout.setVisibility(View.GONE);
                        }
                    }else{
                        if(finaliseAd.getVisibility() != View.GONE){
                            finaliseAd.setVisibility(View.GONE);
                        }
                    }
                }

                if(PSold.equals("No")){

                    if(finaliseAd.getVisibility() != View.VISIBLE){
                        finaliseAd.setVisibility(View.VISIBLE);
                    }
                    if(currentUser.getUid().equals(products.getSellerid())){
                        if(finaliseAd.getVisibility() != View.VISIBLE){
                            finaliseAd.setVisibility(View.VISIBLE);
                        }
                        if(deleteAd.getVisibility() != View.VISIBLE){
                            deleteAd.setVisibility(View.VISIBLE);
                        }
//                        if(placebid.getVisibility() != View.GONE){
//                            placebid.setVisibility(View.GONE);
//                        }
                    }

                    if(!(currentUser.getUid().equals(products.getSellerid()))){
//                        if(placebid.getVisibility() != View.GONE){
//                            placebid.setVisibility(View.GONE);
//                        }
                        if(finaliseAd.getVisibility() != View.VISIBLE){
                            finaliseAd.setVisibility(View.VISIBLE);
                        }
                        if(deleteAd.getVisibility() != View.VISIBLE){
                            deleteAd.setVisibility(View.VISIBLE);
                        }
                    }else
                    {
                        if(finaliseAd.getVisibility() != View.GONE){
                            finaliseAd.setVisibility(View.GONE);
                        }
                    }

                    if(ratingLayout.getVisibility() != View.GONE){
                        ratingLayout.setVisibility(View.GONE);
                    }
                }
                if(currentUser.getUid().equals(products.getBuyerid())){
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
                        if(finaliseAd.getVisibility() != View.GONE){
                            finaliseAd.setVisibility(View.GONE);
                        }
                        if(deleteAd.getVisibility() != View.VISIBLE){
                            deleteAd.setVisibility(View.VISIBLE);
                        }
                    }else{
                        if(details.getVisibility() != View.VISIBLE){
                            details.setVisibility(View.VISIBLE);
                        }
                        if(bids.getVisibility() != View.VISIBLE){
                            bids.setVisibility(View.VISIBLE);
                        }
                        if(finaliseAd.getVisibility() != View.VISIBLE){
                            finaliseAd.setVisibility(View.VISIBLE);
                        }
                        if(deleteAd.getVisibility() != View.VISIBLE){
                            deleteAd.setVisibility(View.VISIBLE);
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


        deleteAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder =  new AlertDialog.Builder(AdvertActivity.this);
                builder.setTitle("Delete Advertisement").setMessage("Are you sure?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                FirebaseDatabase.getInstance().getReference("Products").child(Pid).removeValue();
                                FirebaseDatabase.getInstance().getReference("Bids").child(Pid).removeValue();
//                                FirebaseStorage.getInstance().getReference("Product Images").child("products").child(PImage).delete();
                                FirebaseStorage.getInstance().getReferenceFromUrl(PImage).delete();
                                startActivity(new Intent(AdvertActivity.this,MainActivity.class));
                            }
                        }).setNegativeButton("No", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();


            }
        });

        finaliseAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder =  new AlertDialog.Builder(AdvertActivity.this);
                builder.setTitle("Finalise Deal").setMessage("Are you sure ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                userReference = FirebaseDatabase.getInstance().getReference().child("Products");
                                userReference.child(Pid).child("sold").setValue("Yes");
                                userReference.child(Pid).child("deleteOn").setValue("");

                                if(finaliseAd.getVisibility() != View.GONE){
                                    finaliseAd.setVisibility(View.GONE);
                                }
                                if(deleteAd.getVisibility() != View.GONE){
                                    deleteAd.setVisibility(View.GONE);
                                }
                                if(ratingLayout.getVisibility() != View.VISIBLE){
                                    ratingLayout.setVisibility(View.VISIBLE);
                                }
                            }
                        }).setNegativeButton("No", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();


            }
        });

//        placebid.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if(bids.getVisibility() != View.VISIBLE){
//                    bids.setVisibility(View.VISIBLE);
//                }
//
//                userReference = FirebaseDatabase.getInstance().getReference().child("Products");
//                userReference.keepSynced(true);
//                int i=Integer.parseInt(PPrice) + 100;
//                PPrice = Integer.toString(i);
//                tvP_Price.setText(PPrice);
//
//                userReference.child(Pid).child("price").setValue(PPrice);
//                userReference.child(Pid).child("buyerid").setValue(currentUser.getUid());
//
//                userReference = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());
//                userReference.keepSynced(true);
//                userReference.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        User user = snapshot.getValue(User.class);
////                        tvP_Buyer.setText(user.getName());
//
//                        //BIDDER'S LIST
//
//                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Bids").child(Pid);
//                        databaseReference.keepSynced(true);
//                        list = new ArrayList<>();
//                        databaseReference.addValueEventListener(new ValueEventListener() {
//
//                            Boolean progress = false;
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                if (!progress) {
//                                    list = new ArrayList<>();
//                                    list.clear();
//                                    for (DataSnapshot ds: snapshot.getChildren()) {
//                                        list.add(ds.getValue(Bids.class));
//                                    }
//
//                                    Bids bids = new Bids();
//                                    if(list.isEmpty()){
//                                        bids.setBidCount(1);
//                                    }else{
//                                        Bids lastBid = list.get(list.size()-1);
//                                        bids.setBidCount(lastBid.getBidCount()+1);
//                                    }
//
//                                    bids.setBidderid(currentUser.getUid());
//                                    bids.setPrice(PPrice);
//                                    list.add(bids);
//
//                                    databaseReference.setValue(list).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//                                            if(task.isSuccessful()){
//                                                Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
//                                            }else{
//                                                Toast.makeText(getApplicationContext(),"Fail",Toast.LENGTH_SHORT).show();
//                                            }
//                                        }
//                                    });
//
//                                    list.clear();
//                                    progress = true;
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//
////                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Bids").child(Pid);
////                databaseReference.addValueEventListener(new ValueEventListener() {
////                    Boolean progress = false;
////                    @Override
////                    public void onDataChange(@NonNull DataSnapshot snapshot) {
////                        if (!progress && snapshot.exists()) {
////                            list = new ArrayList<>();
////                            list.clear();
////                            for (DataSnapshot ds: snapshot.getChildren()) {
////                                list.add(0,ds.getValue(Bids.class));
////                            }
////
////                            String s = "";
////                            for(Bids bids1 : list){
////                                s =  s + "S : " + bids1.getBidCount()+ " ";
////                            }
////                            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
////
////                            RecyclerView recyclerView1 = findViewById(R.id.bid_history);
////                            BidsAdapter bidsAdapter = new BidsAdapter(AdvertActivity.this,list);
////                            recyclerView1.setAdapter(bidsAdapter);
////                            bidsAdapter.notifyDataSetChanged();
////
////                        }
////                    }
////
////                    @Override
////                    public void onCancelled(@NonNull DatabaseError error) {
////
////                    }
////                });
//
//            }
//        });

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
                                //seller rates to buyer
                                userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(buyer);
                                userReference.addValueEventListener(new ValueEventListener() {

                                    boolean progressdone = false;
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(!progressdone){
                                            user = snapshot.getValue(User.class);
                                            float currRating = user.getRating();
                                            int count = user.getRatecount();

                                            FirebaseDatabase.getInstance().getReference("Users").child(buyer).child("rating").setValue(((currRating * count)+ratingBar.getRating())/(count +1));
                                            FirebaseDatabase.getInstance().getReference("Users").child(buyer).child("ratecount").setValue(count + 1);
                                            FirebaseDatabase.getInstance().getReference("Products").child(Pid).child("sellerrated").setValue("Yes");

                                            progressdone = true;
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            progress = true;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

//        rate.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//
//
//            String currUid = currentUser.getUid();
//            if(currUid.equals(PBuyerId))
//            {
//                final float[] currRating = {0};
//                final float[] ratecount = {0};
//                final boolean[] processDone = {false};
//                userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(PSellerId);
//                userReference.addValueEventListener(new ValueEventListener()
//                {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot)
//                    {
//                        if (!processDone[0]) {
//                            User user = snapshot.getValue(User.class);
//                            currRating[0] = user.getRating();
//                            ratecount[0] = user.getRatecount();
//                            processDone[0] = true;
//
//                            userReference.child("rating").setValue(((currRating[0] * ratecount[0]) + givenRating) / (ratecount[0] + 1));
//                            userReference.child("ratecount").setValue(ratecount[0] + 1);
//
//                            FirebaseDatabase.getInstance().getReference().child("Products").child(Pid).child("buyerrated").setValue("Yes");
//                            rate.setVisibility(View.INVISIBLE);
//                            Toast.makeText(getApplicationContext(),"Who got rated ? " + user.getName(),Toast.LENGTH_SHORT).show();
//
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//            }
//            else
//            {
//                final float[] currRating = {0};
//                final float[] ratecount = {0};
//                final boolean[] processDone = {false};
//                userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(PBuyerId);
//                userReference.addValueEventListener(new ValueEventListener()
//                    {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot)
//                        {
//                            if (!processDone[0]) {
//                                User user = snapshot.getValue(User.class);
//                                currRating[0] = user.getRating();
//                                ratecount[0] = user.getRatecount();
//                                processDone[0] = true;
//
//                                userReference.child("rating").setValue(((currRating[0] * ratecount[0]) + givenRating) / (ratecount[0] + 1));
//                                userReference.child("ratecount").setValue(ratecount[0] + 1);
//                                rate.setVisibility(View.INVISIBLE);
//                                FirebaseDatabase.getInstance().getReference().child("Products").child(Pid).child("sellerrated").setValue("Yes");
//
//                                Toast.makeText(getApplicationContext(),"Who got rated ? " + user.getName(),Toast.LENGTH_SHORT).show();
//                            }
//
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
//                }
//            }
//        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Bids").child(Pid);
        databaseReference.keepSynced(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list = new ArrayList<>();
                    list.clear();
                    int curr = 0;
                    int prev = 0;
                    for (DataSnapshot ds: snapshot.getChildren()) {
                        Bids bids = ds.getValue(Bids.class);
                        list.add(0,bids);
                        if(bids.getBidCount()==1) {
                            curr = Integer.parseInt(bids.getPrice());
                        }else {
                            prev = curr;
                            curr = Integer.parseInt(bids.getPrice());
                            if(prev == curr){
                                list.remove(bids);
                            }
                        }
                    }
                    RecyclerView recyclerView1 = findViewById(R.id.bid_history);
                    BidsAdapter bidsAdapter = new BidsAdapter(AdvertActivity.this,list);
                    recyclerView1.setAdapter(bidsAdapter);
                    bidsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}