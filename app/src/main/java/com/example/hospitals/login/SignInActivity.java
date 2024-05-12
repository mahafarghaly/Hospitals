package com.example.hospitals.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hospitals.HomeActivity;
import com.example.hospitals.R;
import com.example.hospitals.login.presenter.LoginPresenterImpl;
import com.example.hospitals.signup.SignUpActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class SignInActivity extends AppCompatActivity implements LoginView {


    public static final String SHARED_PREF = "sharedPre";

    static final String TAG = "Login";

    String userName;


    GoogleSignInClient googleSignInClient;

    FirebaseAuth mAuth;

    private LoginPresenterImpl presenter;
    private FirebaseAuth auth;
    private EditText loginEmail, loginPassword;
    private Button loginButton;
    private ImageView signGoogleBtn;
    private TextView signupRedirectText;
    private GoogleSignInClient gsc;
    private GoogleSignInOptions gso;
    GoogleSignInAccount acct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        auth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = auth.getCurrentUser();


        presenter = new LoginPresenterImpl(this);
        auth = FirebaseAuth.getInstance();

        loginEmail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        signupRedirectText = findViewById(R.id.signUpRedirectText);
        signGoogleBtn = findViewById(R.id.btn_google);

        loginButton.setOnClickListener(v -> {
            String email = loginEmail.getText().toString().trim();
            String password = loginPassword.getText().toString().trim();
            presenter.login(email, password);
        });

        signupRedirectText.setOnClickListener(v ->
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class)));

        // Google SignIn initialization
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);
        acct = GoogleSignIn.getLastSignedInAccount(this);

        signGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser != null||acct!=null) {
                    navigateToSecondActivity();
                }
                Intent intent = googleSignInClient.getSignInIntent();
                startActivityForResult(intent, 100);
            }
        });
    }

    protected void onStart() {
        super.onStart();

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("320698811787-4fjo3jndj7dqgk4b8a17nsnm0n1eb0qj.apps.googleusercontent.com")
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

    }

    @Override
    public void showLoginSuccess() {
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        SharedPreferences sharedPreferences = SignInActivity.this.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", userEmail);
        editor.apply();
        Log.i("testEmail", "showLoginSuccess: " + userEmail);
        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(SignInActivity.this, HomeActivity.class));
        finish();
    }

    @Override
    public void showLoginFailure(String message) {
        Toast.makeText(this, "Login Failed: " + message, Toast.LENGTH_SHORT).show();
    }

    void navigateToSecondActivity() {
//        finish();
        Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode, resultCode, data);

//        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            if (signInAccountTask.isSuccessful()) {
                String s = "Google sign in successful";
                Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
                try {
                    GoogleSignInAccount googleSignInAccount = signInAccountTask.getResult(ApiException.class);
                    if (googleSignInAccount != null) {
                        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
                        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                                    SharedPreferences sharedPreferences = SignInActivity.this.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("name", userEmail);
                                    editor.putBoolean("isLogin", true);
                                    editor.apply();
                                    Log.i(TAG, "gmail: " + userName);
                                    startActivity(new Intent(SignInActivity.this, HomeActivity.class));
                                    Toast.makeText(SignInActivity.this, s, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SignInActivity.this, "Login fail, please try again", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        }

        }
    }
