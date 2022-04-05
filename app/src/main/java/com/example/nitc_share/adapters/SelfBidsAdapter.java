package com.example.nitc_share.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nitc_share.BuyersActivity;
import com.example.nitc_share.R;
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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class SelfBidsAdapter extends RecyclerView.Adapter<SelfBidsAdapter.MyViewHolder> {

    Context context;
    ArrayList<Bids> list;
    ArrayList<Bids> nlist = new ArrayList<>();
    String pID;

    public SelfBidsAdapter(Context context, ArrayList<Bids> list, String pID) {
        this.context = context;
        this.list = list;
        this.pID = pID;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.selfbid,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Bids bids = list.get(position);
        int index = position;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(bids.getBidderid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    User user = snapshot.getValue(User.class);
                    holder.bidderCount.setText("#" + (index + 1));
                    holder.bidderCount.setTextSize(20);
                    holder.bidder.setText("" + user.getName());
                    holder.bidder.setTextSize(20);
                    String commaCurrency = convert(bids.getPrice());
                    commaCurrency = commaCurrency.substring(1,commaCurrency.length()-1);
                    holder.bidPrice.setText("₹ " + commaCurrency);
                    holder.bidder.setTextSize(20);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("Products").child(pID);
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Products p = snapshot.getValue(Products.class);
                    if(p.getSold().equals("Yes")){
                        if(holder.delete.getVisibility() != View.GONE){
                            holder.delete.setVisibility(View.GONE);
                        }
                    }else{
                        if(holder.delete.getVisibility() != View.VISIBLE){
                            holder.delete.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser curruser = FirebaseAuth.getInstance().getCurrentUser();

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Bids").child(pID);
                databaseReference.keepSynced(true);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    boolean progress = false;
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(!progress){
                            nlist = new ArrayList<>();
                            nlist.clear();
                            for (DataSnapshot ds: snapshot.getChildren()) {
                                Bids bids1 = ds.getValue(Bids.class);
                                if(!(curruser.getUid().equals(bids1.getBidderid()) && bids1.getPrice().equals(bids.getPrice()))) {
                                    nlist.add( bids1);
                                }
                            }
                            BuyersActivity.deleteBid(nlist, pID);
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

    public String convert(String money) {
        Locale locale = new Locale("en_IN");
        NumberFormat formatter= NumberFormat.getCurrencyInstance(locale);
        return formatter.format(Integer.parseInt(money));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView bidderCount, bidder, bidPrice;
        ImageButton delete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            bidPrice = itemView.findViewById(R.id.bidPrice);
            bidderCount = itemView.findViewById(R.id.bidderCount);
            bidder = itemView.findViewById(R.id.bidderName);
            delete = itemView.findViewById(R.id.deleteBid);



        }
    }
}
