package com.example.firebasedemo.Fragments;


import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.firebasedemo.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RecyclerAdapter extends FirebaseRecyclerAdapter<ProductData, RecyclerAdapter.MyHolder> {

    FragmentManager fragment_manager;
    OnFragmentItemClickListener onFragmentItemClickListener;
    public RecyclerAdapter(@NonNull FirebaseRecyclerOptions<ProductData> options, FragmentManager fragmentManager, OnFragmentItemClickListener onFragmentItemClickListener) {
        super(options);
        this.fragment_manager=fragmentManager;
        this.onFragmentItemClickListener=onFragmentItemClickListener;

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

        holder.optionMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(view.getContext(), holder.optionMenu);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.update) {

                            onFragmentItemClickListener.putBundle(model.getId(), model.getpName(), model.getpDes(), model.getpPrice(), model.getpImg());

                        }
                        else if (menuItem.getItemId() == R.id.delete) {
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                            Query applesQuery = ref.child("Products").orderByChild("id").equalTo(model.getId());

                            applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                        appleSnapshot.getRef().removeValue();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.e("UUU", "onCancelled", databaseError.toException());
                                }
                            });
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

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
        ImageView optionMenu;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            txtPname=itemView.findViewById(R.id.pName);
            txtPDes=itemView.findViewById(R.id.pDes);
            txtPPrice=itemView.findViewById(R.id.pprice);
            imageView=itemView.findViewById(R.id.imgView);
            optionMenu=itemView.findViewById(R.id.optionMenu);
        }

    }
}
