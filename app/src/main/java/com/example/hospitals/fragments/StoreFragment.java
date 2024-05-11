package com.example.hospitals.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hospitals.ChatActivity;
import com.example.hospitals.R;

public class StoreFragment extends Fragment {

    AppCompatButton btnChat, btnCall;

    private int STORAGE_PERMISSION_CODE = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_store, container, false);

        // btn Chat Customer Service
//        btnCall = view.findViewById(R.id.btn_call);
//        btnCall.setOnClickListener(v -> {
//
//            onDialButton();
//
//        });
        // btn Chat Customer Service
        btnChat = view.findViewById(R.id.btn_chat);
        btnChat.setOnClickListener(v -> {

            startActivity(new Intent(getActivity(), ChatActivity.class));

        });

        return view;
    }

    public void onDialButton() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:01550374461"));
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CALL_PHONE}, STORAGE_PERMISSION_CODE);
            return;
        } else
            startActivity(intent);
    }

}