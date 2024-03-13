package com.example.firebasedemo.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebasedemo.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class Fragment_View extends Fragment {

    RecyclerView recyclerView;
    RecyclerAdapter adapter; // Create Object of the Adapter class
    DatabaseReference mbase;
    FragmentManager fragmentManager ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment__view, container, false);
        recyclerView=view.findViewById(R.id.recyclerView);
        mbase= FirebaseDatabase.getInstance().getReference();
        fragmentManager=getParentFragmentManager();
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Products")
                .limitToLast(50);

        FirebaseRecyclerOptions<ProductData> options
                = new FirebaseRecyclerOptions.Builder<ProductData>()
                .setQuery(query, ProductData.class)
                .build();

        adapter=new RecyclerAdapter(options,fragmentManager, new OnFragmentItemClickListener() {
            @Override
            public void putBundle(String id, String pName, String pDes, String pPrice, String pImg) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment_Add fragment = new Fragment_Add();

                Bundle bundle=new Bundle();
                bundle.putString("id",id);
                bundle.putString("pName",pName);
                bundle.putString("pDes",pDes);
                bundle.putString("pPrice",pPrice);
                bundle.putString("pImage",pImg);

                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.nav_host_fragment_content_navigation, fragment); // Replace fragment_container with your container id
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}