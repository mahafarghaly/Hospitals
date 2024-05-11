package com.example.hospitals.adminpanel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hospitals.R;
import com.example.hospitals.model.ProductModel;
import com.example.hospitals.model.UserModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class AddProductActivity extends AppCompatActivity {

    private TextView currentAdmin;
    private TextInputEditText edID, edName, edImage, edDesc, edPrice;
    private AppCompatButton btnAdd;
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private LinearLayout progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        // Firebase
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        //
        progress = findViewById(R.id.linear_progress_admin_panel);
        //
        currentAdmin = findViewById(R.id.tv_current_admin);
        //
        edID = findViewById(R.id.ed_id_product);
        //
        edName = findViewById(R.id.ed_name_product);
        //
        edImage = findViewById(R.id.ed_image_product);
        //
        edDesc = findViewById(R.id.ed_description_product);
        //
        edPrice = findViewById(R.id.ed_price_product);
        //
        btnAdd = findViewById(R.id.btn_add_product_admin_panel);
        btnAdd.setOnClickListener(v -> {

            Validation();

        });

        //
        getUserData();

    }

    private void Validation() {
        // Get ID
        String productId = edID.getText().toString().trim();
        // Get Name
        String productName = edName.getText().toString().trim();
        // Get Image URL
        String productImage = edImage.getText().toString().trim();
        // Get Description
        String productDes = edDesc.getText().toString().trim();
        // Get Price
        String productPrice = edPrice.getText().toString().trim();
        // If Statement For Empty Ed
        if (productId.isEmpty()) {
            Toast.makeText(this, "Required Field", Toast.LENGTH_SHORT).show();
            edID.setError("Enter Product ID");
            return;
        }
        if (productName.isEmpty()) {
            Toast.makeText(this, "Required Field", Toast.LENGTH_SHORT).show();
            edName.setError("Enter Product Name");
            return;
        }
        if (productImage.isEmpty()) {
            Toast.makeText(this, "Required Field", Toast.LENGTH_SHORT).show();
            edImage.setError("Enter Product ImageUrl");
            return;
        }
        if (productDes.isEmpty()) {
            Toast.makeText(this, "Required Field", Toast.LENGTH_SHORT).show();
            edDesc.setError("Enter Product Description");
            return;
        }
        if (productPrice.isEmpty()) {
            Toast.makeText(this, "Required Field", Toast.LENGTH_SHORT).show();
            edPrice.setError("Enter Product Price");
            return;
        }

        AddProduct(productId, productName, productImage, productDes, productPrice);

    }

    private void AddProduct(String productId, String productName, String productImage, String productDes, String productPrice) {
        progress.setVisibility(View.VISIBLE);
        ProductModel model = new ProductModel(productId, productName, productImage, productDes, productPrice);
        // Add In Firestore Database
        firestore.collection("product")
                .document(productId)
                .set(model)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Data Added Successfully", Toast.LENGTH_SHORT).show();
                        progress.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        progress.setVisibility(View.GONE);
                    }

                });

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

                            currentAdmin.setText("Admin : " + model.getEmail());

                        }

                    } else {
                        Toast.makeText(AddProductActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }

                });
    }

}