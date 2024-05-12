package com.example.hospitals.signup.presnter;

import android.content.Intent;

public interface SignUpPresenter {
    void signUp(String email, String password, String confirmPassword);
    void handleGoogleSignIn();
    void onActivityResult(int requestCode, int resultCode, Intent data);
}
