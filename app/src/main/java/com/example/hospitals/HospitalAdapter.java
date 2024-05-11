package com.example.hospitals;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.HospitalViewHolder> {
    private ArrayList<HospitalItem> hospitalList;
    private Context context;
    private static final int PERMISSION_CODE_CALL = 100;
    private static final int PERMISSION_CODE_LOCATION = 101;

    public static class HospitalViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageHospital;
        public TextView textName;
        public TextView textDescription;
        public Button btnLocation;
        public Button btnCall;

        public HospitalViewHolder(View itemView) {
            super(itemView);
            imageHospital = itemView.findViewById(R.id.imageHospital);
            textName = itemView.findViewById(R.id.textName);
            textDescription = itemView.findViewById(R.id.textDescription);
            btnLocation = itemView.findViewById(R.id.btnLocation);
            btnCall = itemView.findViewById(R.id.btnCall);
        }
    }

    public HospitalAdapter(ArrayList<HospitalItem> hospitalList, Context context) {
        this.hospitalList = hospitalList;
        this.context = context;
    }

    @NonNull
    @Override
    public HospitalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hospital, parent, false);
        return new HospitalViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HospitalViewHolder holder, int position) {
        HospitalItem currentItem = hospitalList.get(position);

        holder.imageHospital.setImageResource(currentItem.getImageResource());
        holder.textName.setText(currentItem.getName());
        holder.textDescription.setText(currentItem.getDescription());

        holder.btnLocation.setOnClickListener(v -> {
            double latitude = currentItem.getLatitude();
            double longitude = currentItem.getLongitude();
            requestLocationPermission(latitude, longitude);
        });

        holder.btnCall.setOnClickListener(v -> {
            String phoneNumber = currentItem.getPhoneNumber();
            requestCallPermission(phoneNumber);
        });
    }

    @Override
    public int getItemCount() {
        return hospitalList.size();
    }

    private void requestLocationPermission(double latitude, double longitude) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_CODE_LOCATION);
        } else {
            openLocationInMap(latitude, longitude);
        }
    }

    private void openLocationInMap(double latitude, double longitude) {
        Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude + "?q=" + latitude + "," + longitude);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(mapIntent);
        } else {
            Toast.makeText(context, "Google Maps app is not installed or not supported", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestCallPermission(String phoneNumber) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_CODE_CALL);
        } else {
            makeCall(phoneNumber);
        }
    }

    private void makeCall(String phoneNumber) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        context.startActivity(callIntent);
    }
}