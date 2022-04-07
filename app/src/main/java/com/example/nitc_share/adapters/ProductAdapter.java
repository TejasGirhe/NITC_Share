package com.example.nitc_share.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nitc_share.AdvertActivity;
import com.example.nitc_share.BuyersActivity;
import com.example.nitc_share.R;
import com.example.nitc_share.constructors.Bids;
import com.example.nitc_share.constructors.Products;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.myViewHolder >{

    Context context;
    ArrayList<Products> list;
    ArrayList<String> wishlist = new ArrayList<>();

    public ProductAdapter(Context context, ArrayList<Products> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

        Products products = list.get(position);
        holder.PName.setText(products.getPname());
        holder.PDescription.setText(products.getDescription());
        holder.PPrice.setText(products.getPrice());
        holder.BPrice.setText(products.getBaseprice());

        String sDate1 = products.getDeleteOn();
        Date date1 = null;
        try {
            date1 = new SimpleDateFormat("yyyyMMdd").parse(sDate1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat dateFormat = new SimpleDateFormat("MMM dd");
        if(date1!=null){
            String strDate = dateFormat.format(date1);
            holder.time.setText(strDate);
        }

        Glide.with(holder.imageView.getContext()).load(products.getImage()).into(holder.imageView);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Wishlist").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.keepSynced(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            Boolean progress = false;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds: snapshot.getChildren()) {
                        if(ds.getValue(String.class).equals(products.getPid())){
                            holder.wishlist.setChecked(true);
                        }
                    }
                    progress = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if(products.getSellerid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            holder.wishlist.setVisibility(View.INVISIBLE);
        }

        holder.wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.wishlist.isChecked()){
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Wishlist").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    databaseReference.keepSynced(true);

                    databaseReference.addValueEventListener(new ValueEventListener() {
                            Boolean progress = false;
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (!progress) {
                                    wishlist = new ArrayList<>();
                                    wishlist.clear();
                                    for (DataSnapshot ds: snapshot.getChildren()) {
                                        wishlist.add(0,ds.getValue(String.class));
                                    }
                                    wishlist.add(0,products.getPid());

                                    databaseReference.setValue(wishlist).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(context.getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
                                            }else{
                                                Toast.makeText(context.getApplicationContext(),"Fail",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                    list.clear();
                                    progress = true;
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    Toast.makeText(context.getApplicationContext(), "Added to wishlist",Toast.LENGTH_SHORT).show();
                } else{
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Wishlist").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    databaseReference.keepSynced(true);

                    databaseReference.addValueEventListener(new ValueEventListener() {
                        Boolean progress = false;
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!progress) {
                                wishlist = new ArrayList<>();
                                wishlist.clear();
                                for (DataSnapshot ds: snapshot.getChildren()) {
                                    if(!ds.getValue(String.class).equals(products.getPid())){
                                        wishlist.add(0,ds.getValue(String.class));
                                    }
                                }
                                databaseReference.setValue(wishlist).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(context.getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toast.makeText(context.getApplicationContext(),"Fail",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                                list.clear();
                                progress = true;
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(products.getSellerid())){
                    intent = new Intent(context, AdvertActivity.class);
                }else{
                    intent = new Intent(context, BuyersActivity.class);
                }

//                pname, description, price, image, category, pid, date, time, sellerid, buyerid;

                intent.putExtra("Name",products.getPname());
                intent.putExtra("Price",products.getPrice());
                intent.putExtra("Description",products.getDescription());
                intent.putExtra("Image",products.getImage());
                intent.putExtra("Seller",products.getSellerid());
                intent.putExtra("Pid",products.getPid());
                intent.putExtra("Date",products.getDate());
                intent.putExtra("Time",products.getTime());
                intent.putExtra("Buyerid",products.getBuyerid());

                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder{

        com.google.android.material.imageview.ShapeableImageView imageView;
        TextView PName, PDescription, PPrice, BPrice, time;
        CheckBox wishlist;
        LinearLayout card;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = (ShapeableImageView) itemView.findViewById(R.id.tvProductImage);
            PName = itemView.findViewById(R.id.tvPName);
            PDescription = itemView.findViewById(R.id.tvPDescription);
            PPrice = itemView.findViewById(R.id.tvProductPrice);
            wishlist = itemView.findViewById(R.id.wishlist);
            BPrice = itemView.findViewById(R.id.tvbasePrice);
            card = itemView.findViewById(R.id.card);
            time = itemView.findViewById(R.id.time);
        }
    }



}
