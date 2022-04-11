package com.example.nitc_share.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.example.nitc_share.AdvertActivity;
import com.example.nitc_share.BuyersActivity;
import com.example.nitc_share.R;
import com.example.nitc_share.constructors.Products;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SPAdapter extends RecyclerView.Adapter<SPAdapter.myViewHolder >{

    Context context;
    ArrayList<Products> list;

    public SPAdapter(Context context, ArrayList<Products> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemsp,parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

        Products products = list.get(position);
        holder.PName.setText(products.getPname());
        holder.PDescription.setText(products.getDescription());
        holder.PPrice.setText(products.getPrice());
//        Glide.with(holder.imageView.getContext()).load(products.getImage()).into(holder.imageView);
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();

        Glide.with(holder.imageView.getContext())
                .load(products.getImage())
                .placeholder(circularProgressDrawable)
                .into(holder.imageView);

        holder.BPrice.setText(products.getBaseprice());
        if(products.getSold().equals("Yes")){
            if(products.getSellerid().equals(holder.user.getUid())){
                holder.Status.setText("Sold");
                holder.Status.setTextSize(13);
                holder.statusbg.setBackgroundResource(R.drawable.solidblueborder);
                holder.Status.setTextColor(Color.WHITE);
            }else{
                holder.Status.setText("Bought");
                holder.Status.setTextSize(13);
                holder.statusbg.setBackgroundResource(R.drawable.solidblueborder);
                holder.Status.setTextColor(Color.WHITE);
            }
        }else{
            holder.Status.setText("Live");
            holder.Status.setTextSize(13);
            holder.Status.setTextColor(Color.parseColor("#047DF5"));
            holder.statusbg.setBackgroundResource(R.drawable.blueborder);
        }

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

        ShapeableImageView imageView;
        TextView PName, PDescription, PPrice, Status,BPrice ;
        LinearLayout statusbg, card;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = (ShapeableImageView) itemView.findViewById(R.id.tvProductImage);
            PName = itemView.findViewById(R.id.tvPName);
            PDescription = itemView.findViewById(R.id.tvPDescription);
            PPrice = itemView.findViewById(R.id.tvProductPrice);
            Status = itemView.findViewById(R.id.status);
            statusbg = itemView.findViewById(R.id.status_bg);
            BPrice = itemView.findViewById(R.id.tvbasePrice);
            card = itemView.findViewById(R.id.card);
        }
    }



}
