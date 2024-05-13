package com.example.hospitals.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hospitals.HomeActivity;
import com.example.hospitals.R;
import com.example.hospitals.login.presenter.LoginPresenterImpl;
import com.example.hospitals.sgnup.SignUpActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity implements LoginView {
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
        signupRedirectText = findViewById(R.id.signupRedirectText);
        signGoogleBtn = findViewById(R.id.btn_google);

        loginButton.setOnClickListener(v -> {
            String email = loginEmail.getText().toString().trim();
            String password = loginPassword.getText().toString().trim();
            presenter.login(email, password);
        });

        signupRedirectText.setOnClickListener(v -> startActivity(new Intent(SignInActivity.this, SignUpActivity.class)));

        // Google SignIn initialization
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);
        acct = GoogleSignIn.getLastSignedInAccount(this);
//        if (acct != null) {
//            navigateToSecondActivity();
//        }
        signGoogleBtn.setOnClickListener(v -> presenter.handleGoogleSignIn());
        if (currentUser != null||acct!=null) {
            navigateToSecondActivity();
        }
    }

    @Override
    public void showLoginSuccess() {

        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(SignInActivity.this, HomeActivity.class));
        finish();
    }

    @Override
    public void showLoginFailure(String message) {
        Toast.makeText(this, "Login Failed: " + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode, resultCode, data);
    }
    void navigateToSecondActivity() {
        finish();
        Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}
