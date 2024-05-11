package com.example.hospitals.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.hospitals.PrivacyPolicyActivity;
import com.example.hospitals.R;
import com.example.hospitals.SplashScreen;

import java.util.Locale;


public class SettingsFragment extends Fragment {

    LinearLayout linearLanguage, linearCustomerService, linearRateUs, linearPrivacyPolicy, linearLocation;
    String lang;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        String currentLang = Locale.getDefault().getDisplayLanguage();
        lang = getActivity().getSharedPreferences("language", MODE_PRIVATE)
                .getString("lang", currentLang);

        linearLanguage = view.findViewById(R.id.linear_language);
        linearLanguage.setOnClickListener(v -> {

            showAlertLanguage();

        });

        linearRateUs = view.findViewById(R.id.linear_rate_app);
        linearRateUs.setOnClickListener(v -> {

            rateLink();

        });

        linearPrivacyPolicy = view.findViewById(R.id.linear_privacy_policy);
        linearPrivacyPolicy.setOnClickListener(v -> {

            startActivity(new Intent(getActivity(), PrivacyPolicyActivity.class));

        });

        linearLocation = view.findViewById(R.id.linear_location);
        linearLocation.setOnClickListener(v -> {

            showAlertLocation();

        });

        return view;
    }


    // ShowAlertLanguage Is Alert Dialog Appear When Click On Change Language Linear For Change Language
    private void showAlertLanguage() {
// Alert Dialog
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
// Get The Layout That Will Appear In The Alert
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.alert_language, null);
        alertDialog.setView(view);
        alertDialog.setCancelable(true);
// The Button In Alert
        RadioButton btnEnglish = view.findViewById(R.id.radio_english);
        RadioButton btnArab = view.findViewById(R.id.radio_arab);
// If Statement For Check The Button By Shared Preferences
        if (lang.equals("ar")) {
            btnEnglish.setChecked(false);
            btnArab.setChecked(true);
        } else if (lang.equals("en")) {
            btnEnglish.setChecked(true);
            btnArab.setChecked(false);
        }
// btn Save Language
        AppCompatButton btnSave = view.findViewById(R.id.btn_save_language);
        btnSave.setOnClickListener(v -> {

            if (btnEnglish.isChecked()) {
                saveLanguage("en");
            } else if (btnArab.isChecked()) {
                saveLanguage("ar");
                btnEnglish.setChecked(false);
                btnArab.setChecked(true);
            }

        });

        alertDialog.show();

    }

    private void showAlertLocation() {
// Alert Dialog
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
// Get The Layout That Will Appear In The Alert
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.alert_location, null);
        alertDialog.setView(view);
        alertDialog.setCancelable(true);

        // Radio Group btn
        // The Button In Alert
        RadioButton btnMaadi = view.findViewById(R.id.radio_elmaadi);
        RadioButton btnMohadseen = view.findViewById(R.id.radio_mohandseen);
        RadioButton btnnasr = view.findViewById(R.id.radio_madent_naser);
        // Button Open
        Button btnOpen = view.findViewById(R.id.btn_open_location);
        btnOpen.setOnClickListener(v -> {

            if (btnMaadi.isChecked()) {
                MaadiLocation();
            } else if (btnMohadseen.isChecked()) {
                MohandseenLocation();
            }else if (btnnasr.isChecked()){
                NasrLocation();
            }else
                Toast.makeText(getActivity(), "Please Choose One", Toast.LENGTH_SHORT).show();

        });

        alertDialog.show();

    }

    // Method SaveLanguage For Save The Chosen Language
    void saveLanguage(String lang) {
// Shared Preferences
        getActivity().getSharedPreferences("language", MODE_PRIVATE)
                .edit()
                .putString("lang", lang)
                .apply();
// Local Code For Set The Language
        Locale l = new Locale(lang);
        Locale.setDefault(l);
        Configuration configuration = new Configuration();
        configuration.locale = l;
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
        Intent intentLang = new Intent(getActivity(), SplashScreen.class);
        intentLang.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentLang);

    }

    // This Method rateLink For Intent The cs From The App To App Dashboard On Google Play To Rate It
    private void rateLink() {

        // Intent Uri For Move From Main Activity.This To Play Store App For Rate The Application
        Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + getActivity().getApplicationContext().getPackageName());
        Intent intentRate = new Intent(Intent.ACTION_VIEW, uri);
        try {
            // Try And Catch More Safely For Any Errors Will Happen
            startActivity(intentRate);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Unable to open\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
    private void MaadiLocation()
    {
        Intent intentMaadi = new Intent(Intent.ACTION_VIEW , Uri.parse("https://maps.app.goo.gl/wz7VGZXZQ3edPPbW8"));
        startActivity(intentMaadi);
    }

    private void MohandseenLocation()
    {
        Intent intentMohandseen = new Intent(Intent.ACTION_VIEW , Uri.parse("https://maps.app.goo.gl/LJhpT9kyVdphpvPk7"));
        startActivity(intentMohandseen);
    }

    private void NasrLocation()
    {
        Intent intentNasr = new Intent(Intent.ACTION_VIEW , Uri.parse("https://maps.app.goo.gl/VEyGSfBGQiLLT2746"));
        startActivity(intentNasr);
    }


}