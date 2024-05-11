package com.example.hospitals.adminpanel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hospitals.R;
import com.example.hospitals.model.UserModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class UpdateProductActivity extends AppCompatActivity {

    private TextView currentAdmin;
    private TextInputEditText edID, edName, edImage, edDesc, edPrice;
    private AppCompatButton btnAdd;
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private LinearLayout progress;
    private String productId, productName, productImage, productDes, productPrice;
    private ConstraintLayout con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        // Firebase
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        //
        progress = findViewById(R.id.linear_progress_admin_panel_update);
        //
        con = findViewById(R.id.con_update);
        //
        currentAdmin = findViewById(R.id.tv_current_admin_update);
        //
        edID = findViewById(R.id.ed_id_product_update);
        //
        edName = findViewById(R.id.ed_name_product_update);
        //
        edImage = findViewById(R.id.ed_image_product_update);
        //
        edDesc = findViewById(R.id.ed_description_product_update);
        //
        edPrice = findViewById(R.id.ed_price_product_update);
        //
        btnAdd = findViewById(R.id.btn_add_product_admin_panel_update);
        btnAdd.setOnClickListener(v -> {

            Validation();

        });

        //
        getUserData();

    }

    private void Validation()
    {
        // Get ID
        productId = edID.getText().toString().trim();
        // Get Name
        productName = edName.getText().toString().trim();
        // Get Image URL
        productImage = edImage.getText().toString().trim();
        // Get Description
        productDes = edDesc.getText().toString().trim();
        // Get Price
        productPrice = edPrice.getText().toString().trim();
        // If Statement For Empty Ed
        if (productId.isEmpty())
        {
            Toast.makeText(this, "Required Field", Toast.LENGTH_SHORT).show();
            edID.setError("Enter Product ID");
            return;
        }

        if (! productName.isEmpty() && ! productImage.isEmpty() && ! productDes.isEmpty() && ! productPrice.isEmpty())
        {
            updateProductAll();
        }
        else if (! productName.isEmpty())
        {
            updateProductName();
        }
        else if (! productImage.isEmpty())
        {
            updateProductImage();
        }
        else if (! productDes.isEmpty())
        {
            updateProductDesc();
        }
        else if (! productPrice.isEmpty())
        {
            updateProductPrice();
        }
        else
        {
            Toast.makeText(this, "Please Enter One Filed Min", Toast.LENGTH_SHORT).show();
        }


    }


    private void getUserData() {
        String id = firebaseAuth.getUid();
        firestore.collection("users")
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

                            currentAdmin.setText("Admin : "+model.getEmail());

                        }

                    } else {
                        Toast.makeText(UpdateProductActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }

                });
    }

    private void updateProductName() {
        progress.setVisibility(View.VISIBLE);
        Map<String, Object> productUpdateName = new HashMap<>();
        productUpdateName.put("name", productName);
        firestore.collection("product")
                .whereEqualTo("id", productId)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        progress.setVisibility(View.GONE);
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        String documentID = documentSnapshot.getId();
                        firestore.collection("product")
                                .document(documentID)
                                .update(productUpdateName)
                                .addOnSuccessListener(unused ->
                                        Snackbar.make(con, "Product Name Updated Successfully", Snackbar.LENGTH_SHORT)
                                                .setTextColor(Color.WHITE)
                                                .setBackgroundTint(Color.BLACK)
                                                .show()).addOnFailureListener(e ->
                                        Snackbar.make(con, "Error" + task.getException().getMessage(), Snackbar.LENGTH_SHORT)
                                                .setTextColor(Color.RED)
                                                .setBackgroundTint(Color.BLACK)
                                                .show());
                    } else {
                        Snackbar.make(con, "Error" + task.getException().getMessage(), Snackbar.LENGTH_SHORT)
                                .setTextColor(Color.RED)
                                .setBackgroundTint(Color.BLACK)
                                .show();
                        progress.setVisibility(View.GONE);

                    }

                });
    }

    private void updateProductImage() {
        progress.setVisibility(View.VISIBLE);
        Map<String, Object> productUpdateImage = new HashMap<>();
        productUpdateImage.put("image", productImage);
        firestore.collection("product")
                .whereEqualTo("id", productId)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        progress.setVisibility(View.GONE);
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        String documentID = documentSnapshot.getId();
                        firestore.collection("product")
                                .document(documentID)
                                .update(productUpdateImage)
                                .addOnSuccessListener(unused ->
                                        Snackbar.make(con, "Product ImageUrl Updated Successfully", Snackbar.LENGTH_SHORT)
                                                .setTextColor(Color.WHITE)
                                                .setBackgroundTint(Color.BLACK)
                                                .show()).addOnFailureListener(e ->
                                        Snackbar.make(con, "Error" + task.getException().getMessage(), Snackbar.LENGTH_SHORT)
                                                .setTextColor(Color.RED)
                                                .setBackgroundTint(Color.BLACK)
                                                .show());
                    } else {
                        Snackbar.make(con, "Error" + task.getException().getMessage(), Snackbar.LENGTH_SHORT)
                                .setTextColor(Color.RED)
                                .setBackgroundTint(Color.BLACK)
                                .show();
                        progress.setVisibility(View.GONE);

                    }

                });
    }

    private void updateProductDesc() {
        progress.setVisibility(View.VISIBLE);
        Map<String, Object> productUpdateDesc = new HashMap<>();
        productUpdateDesc.put("description", productDes);
        firestore.collection("product")
                .whereEqualTo("id", productId)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        progress.setVisibility(View.GONE);
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        String documentID = documentSnapshot.getId();
                        firestore.collection("product")
                                .document(documentID)
                                .update(productUpdateDesc)
                                .addOnSuccessListener(unused ->
                                        Snackbar.make(con, "Product Description Updated Successfully", Snackbar.LENGTH_SHORT)
                                                .setTextColor(Color.WHITE)
                                                .setBackgroundTint(Color.BLACK)
                                                .show()).addOnFailureListener(e ->
                                        Snackbar.make(con, "Error" + task.getException().getMessage(), Snackbar.LENGTH_SHORT)
                                                .setTextColor(Color.RED)
                                                .setBackgroundTint(Color.BLACK)
                                                .show());
                    } else {
                        Snackbar.make(con, "Error" + task.getException().getMessage(), Snackbar.LENGTH_SHORT)
                                .setTextColor(Color.RED)
                                .setBackgroundTint(Color.BLACK)
                                .show();
                        progress.setVisibility(View.GONE);

                    }

                });
    }

    private void updateProductPrice() {
        progress.setVisibility(View.VISIBLE);
        Map<String, Object> productUpdatePrice = new HashMap<>();
        productUpdatePrice.put("price", productPrice);
        firestore.collection("product")
                .whereEqualTo("id", productId)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        progress.setVisibility(View.GONE);
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        String documentID = documentSnapshot.getId();
                        firestore.collection("product")
                                .document(documentID)
                                .update(productUpdatePrice)
                                .addOnSuccessListener(unused ->
                                        Snackbar.make(con, "Product Price Updated Successfully", Snackbar.LENGTH_SHORT)
                                                .setTextColor(Color.WHITE)
                                                .setBackgroundTint(Color.BLACK)
                                                .show()).addOnFailureListener(e ->
                                        Snackbar.make(con, "Error" + task.getException().getMessage(), Snackbar.LENGTH_SHORT)
                                                .setTextColor(Color.RED)
                                                .setBackgroundTint(Color.BLACK)
                                                .show());
                    } else {
                        Snackbar.make(con, "Error" + task.getException().getMessage(), Snackbar.LENGTH_SHORT)
                                .setTextColor(Color.RED)
                                .setBackgroundTint(Color.BLACK)
                                .show();
                        progress.setVisibility(View.GONE);

                    }

                });
    }

    private void updateProductAll() {
        progress.setVisibility(View.VISIBLE);
        Map<String, Object> productUpdateAll = new HashMap<>();
        productUpdateAll.put("name", productName);
        productUpdateAll.put("image", productImage);
        productUpdateAll.put("description", productDes);
        productUpdateAll.put("price", productPrice);
        firestore.collection("product")
                .whereEqualTo("id", productId)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        progress.setVisibility(View.GONE);
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        String documentID = documentSnapshot.getId();
                        firestore.collection("product")
                                .document(documentID)
                                .update(productUpdateAll)
                                .addOnSuccessListener(unused ->
                                        Snackbar.make(con, "Product Data Updated Successfully", Snackbar.LENGTH_SHORT)
                                                .setTextColor(Color.WHITE)
                                                .setBackgroundTint(Color.BLACK)
                                                .show()).addOnFailureListener(e ->
                                        Snackbar.make(con, "Error" + task.getException().getMessage(), Snackbar.LENGTH_SHORT)
                                                .setTextColor(Color.RED)
                                                .setBackgroundTint(Color.BLACK)
                                                .show());
                    } else {
                        Snackbar.make(con, "Error" + task.getException().getMessage(), Snackbar.LENGTH_SHORT)
                                .setTextColor(Color.RED)
                                .setBackgroundTint(Color.BLACK)
                                .show();
                        progress.setVisibility(View.GONE);

                    }

                });
    }


}