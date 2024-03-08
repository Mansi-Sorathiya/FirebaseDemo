package com.example.firebasedemo.ui.home;



import com.example.firebasedemo.R;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.firebasedemo.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    EditText pname, pdescription, pprize;
    ImageView imageView;
    private DatabaseReference userRef;
    FirebaseDatabase database;
    DatabaseReference myRef;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);


        database =  FirebaseDatabase.getInstance();

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        View root = binding.getRoot();
        Button btnAdd = root.findViewById(R.id.btnAdd);
        pname = root.findViewById(R.id.pname);
        pdescription = root.findViewById(R.id.pdes);
        pprize = root.findViewById(R.id.pprize);
        imageView = root.findViewById(R.id.imageView);

//        getUserData();
//
//        btnAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                myRef = database.getReference("Products").push(); // Creating main (parent) reference
//                Log.d("TTT", "testCode2: "+myRef);
//                String id = myRef.getKey();
//                Log.d("TTT", "onClick: Key="+id);
//                ProductData data=new ProductData(id,"Mouse","Input Device","1250");
//
//                myRef.setValue(data);
//            }
//        });

        return root;
    }

//    private void getUserData()
//    {
//        userRef = database.getReference("Users").push();
//        FirebaseAuth.getInstance().getCurrentUser();
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        String userId = user.getUid();
//
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}