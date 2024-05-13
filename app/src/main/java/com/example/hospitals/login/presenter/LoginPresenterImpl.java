package com.example.hospitals.login.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.hospitals.login.LoginView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPresenterImpl implements LoginPresenter {
    private LoginView view;
    private FirebaseAuth auth;
    private GoogleSignInClient gsc;

    public LoginPresenterImpl(LoginView view) {
        this.view = view;
        auth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient((Context) view, gso);
    }

    @Override
    public void login(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> view.showLoginSuccess())
                .addOnFailureListener(e -> view.showLoginFailure(e.getMessage()));
    }

    @Override
    public void handleGoogleSignIn() {
        Intent signInIntent = gsc.getSignInIntent();
        ((Activity) view).startActivityForResult(signInIntent, 1000);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1000) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                view.showLoginSuccess();
            } catch (ApiException e) {
                view.showLoginFailure("Google SignIn failed");
            }
        }
    }
}

