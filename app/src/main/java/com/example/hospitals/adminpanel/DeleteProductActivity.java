package com.example.hospitals.adminpanel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hospitals.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DeleteProductActivity extends AppCompatActivity {

    private AppCompatButton btnDelete;
    private TextView tvAdmin;
    private TextInputEditText tvID;
    private FirebaseFirestore firestore;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_product);

        firestore = FirebaseFirestore.getInstance();
        tvAdmin = findViewById(R.id.tv_current_admin_delete);
        tvID = findViewById(R.id.ed_id_delete);
        btnDelete = findViewById(R.id.btn_delete_admin_panel);
        btnDelete.setOnClickListener(v -> {

            Validation();

        });


    }

    private void Validation()
    {

        id = tvID.getText().toString().trim();
        //
        if (id.isEmpty())
        {
            Toast.makeText(this, "Enter Product ID", Toast.LENGTH_SHORT).show();
            tvID.setError("Enter Product ID");
            tvID.requestFocus();
        }
        else
        {
            DeleteData();
        }

    }

    private void DeleteData()
    {

        firestore.collection("product")
                .whereEqualTo("id", id)
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful() && !task.getResult().isEmpty())
                    {

                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        String documentID = documentSnapshot.getId();;
                        firestore.collection("product")
                                .document(documentID)
                                .delete()
                                .addOnSuccessListener(unused -> {

                                    Toast.makeText(this, "Product Deleted Success", Toast.LENGTH_SHORT).show();

                                }).addOnFailureListener(e -> {

                                    Toast.makeText(this, "Failed To Delete Product", Toast.LENGTH_SHORT).show();

                                });

                    }
                    else
                    {
                        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                    }

                });

    }

}