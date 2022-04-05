package com.example.nitc_share.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nitc_share.R;
import com.example.nitc_share.constructors.Bids;
import com.example.nitc_share.constructors.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class BidsAdapter extends RecyclerView.Adapter<BidsAdapter.MyViewHolder> {

    Context context;
    ArrayList<Bids> list;

    public BidsAdapter(Context context, ArrayList<Bids> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bid,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Bids bids = list.get(position);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(bids.getBidderid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    User user = snapshot.getValue(User.class);
                    holder.bidderCount.setText("#" + (position + 1));
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
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            bidPrice = itemView.findViewById(R.id.bidPrice);
            bidderCount = itemView.findViewById(R.id.bidderCount);
            bidder = itemView.findViewById(R.id.bidderName);

        }
    }
}
