package com.example.firebasedemo.Fragments;


import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.firebasedemo.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class RecyclerAdapter extends FirebaseRecyclerAdapter<ProductData, RecyclerAdapter.MyHolder> {

    public RecyclerAdapter(@NonNull FirebaseRecyclerOptions<ProductData> options) {
        super(options);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull MyHolder holder, int position, @NonNull ProductData model) {
        holder.txtPname.setText(""+model.getpName());
        holder.txtPDes.setText(""+model.getpDes());
        holder.txtPPrice.setText(""+model.getpPrice());
        Log.d("PPP", "onBindViewHolder: "+model.getpName());

        Glide.with(holder.imageView.getContext())
                .load(model.getpImg())
                .diskCacheStrategy(DiskCacheStrategy.NONE) // Disable disk cache
                .skipMemoryCache(true) // Skip memory cache
                .placeholder(R.drawable.rotate)
                .into(holder.imageView);

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item,parent,false);
        MyHolder holder=new MyHolder(view);

        return holder;
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView txtPname,txtPDes,txtPPrice;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            txtPname=itemView.findViewById(R.id.pName);
            txtPDes=itemView.findViewById(R.id.pDes);
            txtPPrice=itemView.findViewById(R.id.pprice);
            imageView=itemView.findViewById(R.id.imgView);
        }

    }
}
