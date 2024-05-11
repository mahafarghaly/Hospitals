package com.example.hospitals.fragments;

import static android.content.Context.MODE_PRIVATE;

import static com.example.hospitals.LoginActivity.SHARED_PREF;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hospitals.LoginActivity;
import com.example.hospitals.R;
import com.example.hospitals.adminpanel.AddProductActivity;
import com.example.hospitals.adminpanel.DeleteProductActivity;
import com.example.hospitals.adminpanel.UpdateProductActivity;
import com.example.hospitals.model.UserModel;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


import java.util.HashMap;
import java.util.Map;


public class ProfileFragment extends Fragment {

    private static final int RESULT_OK = -1;
    private LinearLayout linearChangePassword, linearLogOut;
    private SwitchMaterial btnSwitch;
    private CardView imageCard;
    private ImageView imageViewPerson;
    private TextView tvEmail, tvPhone, tvAdminPanel;
    private CardView loading, admin;


    // For Image
    private Uri imageUri = null;
    private static String ImageURL = null;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore fireStore;
    private StorageReference storageReference;
    private String userID;
    private ProgressDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        imageViewPerson = view.findViewById(R.id.iv_change_picture);
        tvEmail = view.findViewById(R.id.tv_email_view);
        tvPhone = view.findViewById(R.id.tv_phone_view);
        admin = view.findViewById(R.id.card_view_admin_panel);
        tvAdminPanel = view.findViewById(R.id.tv_admin_panel_hidden);
        // Fire Base
        firebaseAuth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();


        linearChangePassword = view.findViewById(R.id.linear_change_password);
        linearChangePassword.setOnClickListener(v -> {

            showAlertPassword();

        });

        linearLogOut = view.findViewById(R.id.linear_logout);
        linearLogOut.setOnClickListener(v -> {
            firebaseAuth.signOut();
            getActivity().getSharedPreferences("login", MODE_PRIVATE)
                    .edit()
                    .clear()
                    .apply();
            /*
            Intent intentLogOut = new Intent(getActivity(), LoginActivity.class);
            startActivity(intentLogOut);
            getActivity().finish();
            */


        });

        linearLogOut = view.findViewById(R.id.linear_logout);
        linearLogOut.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.app_name)
                    .setIcon(R.mipmap.ic_launcher_round)
                    .setMessage("Do You Want To Log Out?")
                    .setPositiveButton(R.string.btn_yes, (dialog, which) -> {
                        // Sign out from Firebase
                        firebaseAuth.signOut();

                        // Clear the 'login' shared preferences
                        getActivity().getSharedPreferences("login", Context.MODE_PRIVATE)
                                .edit()
                                .clear()
                                .apply();

                        // Clear 'name' shared preference
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("name", "");
                        editor.apply();

                        // Start LoginActivity and finish current activity
                        Intent intentLogOut = new Intent(getActivity(), LoginActivity.class);
                        intentLogOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intentLogOut);
                        getActivity().finish();
                    })
                    .setNegativeButton(R.string.btn_no, (dialog, which) -> dialog.cancel());

            builder.show();
        });

        btnSwitch = view.findViewById(R.id.switch_mode);
        btnSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                saveTheme(true);
                btnSwitch.setClickable(true);
            } else {
                saveTheme(false);
                btnSwitch.setClickable(false);
            }

        });

        imageCard = view.findViewById(R.id.image_card);
        imageCard.setOnClickListener(v -> {

            checkPermission();

        });
        getUserData();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (tvAdminPanel.getText().toString().equals("admin"))
                {
                    admin.setVisibility(View.VISIBLE);
                }
                else
                {
                    admin.setVisibility(View.GONE);
                }
            }
        },2000);

        admin.setOnClickListener(v -> {

            showAlertAdminPanel();

        });

        return view;
    }

    // Method SaveTheme For Save The Chosen Theme In Shared Preferences
    private void saveTheme(boolean b) {

        getActivity().getSharedPreferences("theme", MODE_PRIVATE)
                .edit()
                .putBoolean("themeSelected", b)
                .apply();

        if (b) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

    }

    private void checkPermission() {

        //use permission to READ_EXTERNAL_STORAGE For Device >= Marshmallow
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                // Ask User For Read External Storage
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            }

            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                // Ask User For Read External Storage
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);

            } else {
                OpenGalleryImagePicker();
            }

            //implement code for device < Marshmallow
        } else {

            OpenGalleryImagePicker();
        }
    }

    private void OpenGalleryImagePicker() {
        // start picker to get image for cropping and then use the image in cropping activity
        /*Intent intent = CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .getIntent(getActivity());
        someActivityResultLauncher.launch(intent);*/
    }

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                       /* CropImage.ActivityResult d = CropImage.getActivityResult(data);

                        imageUri = d.getUri();*/

                        // set image user in ImageView ;
                        imageViewPerson.setImageURI(imageUri);

                        uploadImage();
                    }
                }
            });

    private void uploadImage() {

        if (firebaseAuth.getCurrentUser() != null) {

            // chick if user image is null or not
            if (imageUri != null) {

                userID = firebaseAuth.getCurrentUser().getUid();

                // mack progress bar dialog
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setTitle("Uploading....");
                progressDialog.setCancelable(false);
                progressDialog.show();

                // mack collection in fireStorage
                final StorageReference ref = storageReference.child("user_profile_image").child(userID + ".jpg");

                // get image user and give to imageUserPath
                ref.putFile(imageUri).addOnProgressListener(taskSnapshot -> {

                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage("upload " + (int) progress + "%");

                }).continueWithTask(task -> {
                    if (!task.isSuccessful()) {

                        throw task.getException();

                    }
                    return ref.getDownloadUrl();

                }).addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        progressDialog.dismiss();
                        Uri downloadUri = task.getResult();

                        assert downloadUri != null;
                        ImageURL = downloadUri.toString();
                        saveChange();

                    } else {

                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), " Error in  " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }
    }

    private void saveChange() {
        Map<String, Object> userPicture = new HashMap<>();
        userPicture.put("image", ImageURL);
        fireStore.collection("users")
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        String documentID = documentSnapshot.getId();
                        fireStore.collection("users")
                                .document(documentID)
                                .update(userPicture)
                                .addOnSuccessListener(unused ->
                                        Toast.makeText(getActivity(), "Uploaded Successfully", Toast.LENGTH_SHORT).show()
                                ).addOnFailureListener(e -> Toast.makeText(getActivity(), "Failed Load Image", Toast.LENGTH_SHORT).show());
                    } else {
                        Toast.makeText(getActivity(), "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getUserData() {
        String id = firebaseAuth.getUid();
        fireStore.collection("users")
                .whereEqualTo("id", id)
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            UserModel model = new UserModel(
                                    document.getData().get("email").toString(),
                                    document.getData().get("password").toString(),
                                    document.getData().get("phone").toString(),
                                    document.getData().get("image").toString(),
                                    document.getData().get("kind").toString(),
                                    id
                            );
                            tvEmail.setText(model.getEmail());
                            tvPhone.setText(model.getPhone());
                            tvAdminPanel.setText(model.getKind());
                            if (model.getImage() != null) {
                                Picasso.get()
                                        .load(model.getImage())
                                        .placeholder(R.drawable.place_holder)
                                        .into(imageViewPerson);
                            }

                        }

                    } else {
                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                    }

                });
    }

    // Alert Dialog Password
    private void showAlertPassword() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setCancelable(true);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.alert_update_password, null);
        alertDialog.setView(view);
        EditText currentPassword = view.findViewById(R.id.et_current_password);
        EditText newPassword = view.findViewById(R.id.et_new_password);
        EditText reNewPassword = view.findViewById(R.id.et_re_new_password);
        loading = view.findViewById(R.id.include_loading);

// Button Code
        Button save = view.findViewById(R.id.btn_save_password);
        save.setOnClickListener(v -> {

            String cPassword = currentPassword.getText().toString().trim();
            String nPassword = newPassword.getText().toString().trim();

            if (currentPassword.getText().toString().isEmpty()) {
                currentPassword.setError("Required Field");
                return;
            }
            if (newPassword.getText().toString().isEmpty()) {
                newPassword.setError("Required Filed");
                return;
            }
            if (reNewPassword.getText().toString().isEmpty()) {
                reNewPassword.setError("Required Filed");
                return;
            }
            if (!newPassword.getText().toString().equals(reNewPassword.getText().toString())) {
                reNewPassword.setError("Password Do not Match");
                return;
            }
            if (newPassword.length() < 6) {
                newPassword.setError("Password Must Be More Than 6 Characters");
                return;
            }
            updatePassword(cPassword, nPassword);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    alertDialog.dismiss();
                }
            }, 1000);
        });

        alertDialog.show();

    }

    private void updatePassword(String cPassword, String nPassword) {
        loading.setVisibility(View.VISIBLE);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        // re-authenticate the user before changing password
        assert user != null;
        AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), cPassword);
        user.reauthenticate(authCredential)
                .addOnSuccessListener(unused -> {
                    // successfully authenticated begin update
                    user.updatePassword(nPassword)
                            .addOnSuccessListener(unused1 -> {
                                // password updated
                                loading.setVisibility(View.VISIBLE);
                                Toast.makeText(getActivity(), "Password Updated Successfully", Toast.LENGTH_SHORT).show();

                            })
                            .addOnFailureListener(e -> {
                                // failed updating password
                                loading.setVisibility(View.VISIBLE);
                                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show());

    }
    // Shwoing Alert Of Admin Panel
    private void showAlertAdminPanel() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setCancelable(true);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.alert_admin, null);
        // btn Adding New Product To Database
        AppCompatButton btnAdd = view.findViewById(R.id.btn_add_product);
        btnAdd.setOnClickListener(v -> {

            startActivity(new Intent(getActivity(), AddProductActivity.class));

        });
        // btn Delete Current Product In Database
        AppCompatButton btnDelete = view.findViewById(R.id.btn_delete_product);
        btnDelete.setOnClickListener(v -> {

            startActivity(new Intent(getActivity(), DeleteProductActivity.class));

        });
        // btn Update Current Product In Database
        AppCompatButton btnUpdate = view.findViewById(R.id.btn_update_product);
        btnUpdate.setOnClickListener(v -> {

            startActivity(new Intent(getActivity(), UpdateProductActivity.class));

        });

        alertDialog.setView(view);
        alertDialog.show();

    }



}