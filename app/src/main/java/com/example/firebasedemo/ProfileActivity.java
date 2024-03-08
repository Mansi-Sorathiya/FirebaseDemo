package com.example.firebasedemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {

    ImageView profileimage;
    EditText name,username,dob,email,phone;
    Button savechanges;
    private Bitmap bitmap;
    final CharSequence[] items = {"Camera", "Gallery"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileimage=findViewById(R.id.profileimage);
        name=findViewById(R.id.name);
        username=findViewById(R.id.username);
        dob=findViewById(R.id.dob);
        email=findViewById(R.id.email);
        phone=findViewById(R.id.phone);


        if (ContextCompat.checkSelfPermission(ProfileActivity.this, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.CAMERA}, 500);

        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImgOther();
            }
        });


    }

    private void selectImgOther() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Choose Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals("Camera")) {
                    launchCamera();

                } else if (items[which].equals("Gallery")) {

                    Intent GalleryIntent = null;
                    GalleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    GalleryIntent.setType("image/*");
                    GalleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(GalleryIntent, 0);
                }
            }
        });
        builder.show();

        if (ContextCompat.checkSelfPermission(ProfileActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // If not granted, request the CAMERA permission
            ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{android.Manifest.permission.CAMERA}, 100);
        }
    }

    private void launchCamera() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, 100);

        }
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with launching the camera
                launchCamera();
            } else {
                // Permission denied, handle accordingly (e.g., show a message to the user)
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //from gallery
        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {

            Log.d("LLL", "CODE in Gallery=" + requestCode);
            Bundle extras = data.getExtras();
            Uri selectedImageUri = data.getData();
            Log.d("UUU", "onActivityResult: extras=" + selectedImageUri);

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                Log.d("XXX", "onActivityResult:Bitmap= " + bitmap);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            profileimage.setImageBitmap(bitmap);
            //Bitmap bitmap = (Bitmap) extras.get("data");;
//            ByteArrayOutputStream bytes=new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG,100,bytes);
//            imageView.setImageBitmap(bitmap);
//            SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMDD_HHmmss", Locale.getDefault());
//            String currentDateandTime=sdf.format(new Date());
//            File downloadedFile;
//            downloadedFile = new File(file.getAbsolutePath() + "/IMG_" + currentDateandTime + ".jpg");
//            try {
//                downloadedFile.createNewFile();
//                FileOutputStream fo=new FileOutputStream(downloadedFile);
//                fo.write(bytes.toByteArray());
//                Toast.makeText(MainActivity.this, "File Downloaded", Toast.LENGTH_SHORT).show();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            ImageURI= Uri.parse(downloadedFile.getAbsolutePath());


        } else {
            // Handle the case where the URI is null
            Log.e("LLL", "Gallery: URI is null");
        }

        // for take image from camera
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            // Get the captured image from the intent's data
            Bundle extras = data.getExtras();
            if (extras != null) {
                bitmap = (Bitmap) extras.get("data");
                profileimage.setImageBitmap(bitmap);
//                if (bitmap != null) {
//                    // The image is in the extras as a bitmap
//                    imageView.setImageBitmap(bitmap);
//
//                    // Save the bitmap to internal storage and get the URI
//                    ImageURI = Uri.parse(saveToInternalStorage(bitmap));
//                    Log.d("MMM", "Main: URI in Main Camera==" + ImageURI);
//                    loadImageFromStorage(savedImgPath);
//                } else {
//                    // Handle the case where the bitmap is null
//                    Log.e("LLL", "Camera: Bitmap is null");
//                }
//            } else {
//                // Handle the case where extras is null
//                Log.e("LLL", "Camera: Extras is null");
            }
        }
    }
}