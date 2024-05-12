package com.example.hospitals;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);
//        FacebookSdk.setClientToken("56789");
//
//        // Initialize the Facebook SDK
//        FacebookSdk.sdkInitialize(getApplicationContext());
    }
}
