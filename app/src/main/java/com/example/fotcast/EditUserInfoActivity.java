package com.example.fotcast;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class EditUserInfoActivity extends AppCompatActivity {

    private EditText etUsername, etRegNo;
    private Button btnSave, btnCancel;

    private FirebaseUser currentUser;
    private DatabaseReference dbRef;

    private static final String TAG = "EditUserInfoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);

        etUsername = findViewById(R.id.etUsername);
        etRegNo = findViewById(R.id.etRegNo);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference("Users");

        if (getIntent() != null) {
            etUsername.setText(getIntent().getStringExtra("username"));
            etRegNo.setText(getIntent().getStringExtra("regNo"));
        }

        btnSave.setOnClickListener(v -> {
            Log.d(TAG, "Save button clicked");
            saveUserInfo();
        });

        btnCancel.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }


    private void saveUserInfo() {
        String newUsername = etUsername.getText().toString().trim();
        String newRegNo = etRegNo.getText().toString().trim();

        if (TextUtils.isEmpty(newUsername) || TextUtils.isEmpty(newRegNo)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentUser == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = currentUser.getUid();
        Map<String, Object> updates = new HashMap<>();
        updates.put("displayName", newUsername);
        updates.put("registrationNumber", newRegNo);

        dbRef.child(uid).updateChildren(updates)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(EditUserInfoActivity.this, "User info updated", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(EditUserInfoActivity.this, "Failed to update: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

}
