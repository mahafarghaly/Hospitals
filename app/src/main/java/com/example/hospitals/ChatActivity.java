package com.example.hospitals;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private CircleImageView profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        profile = findViewById(R.id.profile_image);
        profile.setOnClickListener(v -> {

            showAlertImage();

        });

    }

    private void showAlertImage() {

        androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(this).create();

        View view = LayoutInflater.from(this).inflate(R.layout.alert_picture, null);
        alertDialog.setView(view);
        alertDialog.setCancelable(true);

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.transperant_bg);
            }
        });

        alertDialog.show();

    }

}