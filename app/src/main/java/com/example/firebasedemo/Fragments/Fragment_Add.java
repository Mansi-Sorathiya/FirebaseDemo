package com.example.firebasedemo.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.firebasedemo.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Random;

public class Fragment_Add extends Fragment {


    EditText pname, pdescription, pprize;
    ImageView imageView;
    Button btnAdd;
    private DatabaseReference userRef;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseStorage storage;
    FirebaseStorage productsImage;
    StorageReference storageRef;
    StorageReference imgRef;
    private int requestCode = 150;
    private static final int PICK_IMAGE_CAMERA = 100;
    private static final int PICK_IMAGE_GALLERY = 200;
    private static final int MY_CAMERA_REQUEST_CODE = 150;
    private Bitmap bitmap;
    private String imgUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        database = FirebaseDatabase.getInstance();

        View view = inflater.inflate(R.layout.fragment__add, container, false);
        btnAdd = view.findViewById(R.id.btnAdd);
        pname = view.findViewById(R.id.pname);
        pdescription = view.findViewById(R.id.pdes);
        pprize = view.findViewById(R.id.pprize);
        imageView = view.findViewById(R.id.imageView);

        getUserData();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                database = FirebaseDatabase.getInstance(); // initializing object of database
                myRef = database.getReference("Products").push(); // Creating main (parent) reference
                addImage();

            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCameraPermission();
                selectImgOther();
            }
        });
        return view;
    }

    private void addImage() {
        storage = FirebaseStorage.getInstance();
        productsImage = FirebaseStorage.getInstance("gs://ProductsImages");
        Log.d("UUU", "onCreateView: Storage="+productsImage.getReference());
        storageRef = storage.getReference();
        Log.d("UUU", "onCreateView: storageRef="+storageRef);
        String imageName = "Img" + new Random().nextInt(10000) + ".jpg";
        imgRef=storageRef.child("ProductsImages/"+imageName);
        Log.d("UUU", "onCreateView: imgRef="+imgRef.getPath());

        imgRef.getName().equals(imgRef.getName());    // true
        imgRef.getPath().equals(imgRef.getPath());    // false

        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        //Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imgRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.d("UUU", "onFailure:");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Log.d("UUU", "onSuccess: ");

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return imgRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            imgUrl= String.valueOf(downloadUri);

                            String id = myRef.getKey();
                            Log.d("TTT", "testCode2: "+id);
                            Log.d("TTT", "testCode2: "+imgUrl);
                            ProductData data = new ProductData(id,pname.getText().toString(),pdescription.getText().toString(), pprize.getText().toString(),imgUrl);
                            myRef.setValue(data);



                        } else {
                            // Handle failures
                            // ...
                        }
                    }
                });



            }
        });



    }

    private void getUserData() {

        userRef = database.getReference("Users").push();
        FirebaseAuth.getInstance().getCurrentUser();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
    }

    private void convertImage() {


    }

    private void checkCameraPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CAMERA}, requestCode);
    }

    private void selectImgOther() {
        try {
            PackageManager pm = getActivity().getPackageManager();
            int hasPerm = pm.checkPermission(android.Manifest.permission.CAMERA, getActivity().getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Select Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {
                            dialog.dismiss();
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, PICK_IMAGE_CAMERA);
                        } else if (options[item].equals("Choose From Gallery")) {
                            dialog.dismiss();
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            } else
                Toast.makeText(getActivity(), "Camera Permission error", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_CAMERA) {
            try {
                Uri selectedImage = data.getData();
                bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);


            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_GALLERY) {
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                Log.e("Activity", "Pick from Gallery::>>> ");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "camera permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }
}