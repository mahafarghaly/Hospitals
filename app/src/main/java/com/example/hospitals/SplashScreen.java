package com.example.hospitals;

import static com.example.hospitals.LoginActivity.SHARED_PREF;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.airbnb.lottie.LottieAnimationView;


public class SplashScreen extends AppCompatActivity {

    static final String TAG = "Login";

    SharedPreferences sharedPreferences;

    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        sharedPreferences = SplashScreen.this.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        userName = sharedPreferences.getString("name", "");

        Log.i(TAG, "splash userName: "+userName);

        LottieAnimationView lottie = findViewById(R.id.lottie);
        lottie.animate().translationX(2000).setDuration(2000).setStartDelay(2900);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(userName.equals("")){
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, 5000);
    }
}
