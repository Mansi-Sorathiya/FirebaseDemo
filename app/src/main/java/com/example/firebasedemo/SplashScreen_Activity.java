package com.example.firebasedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen_Activity extends AppCompatActivity {
    public static SharedPreferences preferences;
    public static SharedPreferences.Editor editor;
    Boolean Login=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        preferences = getSharedPreferences("myPref",MODE_PRIVATE);
        editor = preferences.edit();
        Login=preferences.getBoolean("Login",false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(Login){
                    Intent intent=new Intent(SplashScreen_Activity.this, Navigation_Activity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent=new Intent(SplashScreen_Activity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },1000);

    }
}