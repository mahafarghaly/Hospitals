package com.example.hospitals.signup.presnter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.hospitals.signup.SignUpView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpPresenterImpl  implements SignUpPresenter {
    private SignUpView view;
    private FirebaseAuth auth;
    private GoogleSignInClient gsc;


    public SignUpPresenterImpl(SignUpView view) {
        this.view = view;
        auth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient((Context) view, gso);
    }

    @Override
    public void signUp(String email, String password, String confirmPassword) {
        if (password.equals(confirmPassword)) {
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            view.showSignUpSuccess();
                        } else {
                            view.showSignUpFailure(task.getException().getMessage());
                        }
                    });
        } else {
            view.showSignUpFailure("Passwords do not match");
        }
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
                view.showSignUpSuccess();
            } catch (ApiException e) {
                view.showSignUpFailure("Google SignIn failed");
            }
        }
    }
}