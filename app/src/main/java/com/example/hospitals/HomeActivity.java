package com.example.hospitals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;

import com.example.hospitals.databinding.ActivityHomeBinding;
import com.example.hospitals.fragments.HomeFragment;
import com.example.hospitals.fragments.ProfileFragment;
import com.example.hospitals.fragments.SettingsFragment;
import com.example.hospitals.fragments.StoreFragment;
import com.example.hospitals.R;
public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;
    private int STORAGE_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // For Check Internet
        if (!checkInternet()) {
            alertNoInternet();
        }
        new Handler().postDelayed(() -> {
            binding.bottomNavView.setSelectedItemId(R.id.home_item);
        }, 100);
        binding.bottomNavView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home_item) {
                replaceFragment(new HomeFragment());
                return true;
            } else if (item.getItemId() == R.id.profile_item) {
                replaceFragment(new ProfileFragment());
                return true;
            } else if (item.getItemId() == R.id.store_item) {
                replaceFragment(new StoreFragment());
                return true;
            } else if (item.getItemId() == R.id.settings_item) {
                replaceFragment(new SettingsFragment());
                return true;
            }
            return false;
        });

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
    private boolean checkInternet() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher_round);
        builder.setMessage(R.string.leave_msg)
                .setPositiveButton(R.string.btn_yes,
                        (dialog, which) -> finish())
                .setNegativeButton(R.string.btn_no,
                        (dialog, which) -> dialog.cancel());

        builder.show();
    }



    private void alertNoInternet() {

        androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(this).create();

        View view = LayoutInflater.from(this).inflate(R.layout.alert_no_internet, null);
        alertDialog.setView(view);
        alertDialog.setCancelable(false);

        AppCompatButton btnRetry = view.findViewById(R.id.btn_retry);
        btnRetry.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, SplashScreen.class)));

        alertDialog.show();

    }
}