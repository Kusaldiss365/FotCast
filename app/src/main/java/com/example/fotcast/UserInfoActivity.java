package com.example.fotcast;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserInfoActivity extends AppCompatActivity {

    private TextView tvUsername, tvEmail, tvRegNo;
    private Button btnEdit, btnSignOut;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        mAuth = FirebaseAuth.getInstance();

        tvUsername = findViewById(R.id.tvUsername);
        tvEmail = findViewById(R.id.tvEmail);
        tvRegNo = findViewById(R.id.tvRegNo);
        btnEdit = findViewById(R.id.btnEdit);
        btnSignOut = findViewById(R.id.btnSignOut);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            tvUsername.setText(currentUser.getDisplayName() != null ? currentUser.getDisplayName() : "-");
            tvEmail.setText(currentUser.getEmail() != null ? currentUser.getEmail() : "-");

            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());
            userRef.child("displayName").get().addOnSuccessListener(snapshot -> {
                String name = snapshot.getValue(String.class);
                tvUsername.setText(name != null ? name : "-");
            });
            userRef.child("registrationNumber").get().addOnSuccessListener(snapshot -> {
                String regNo = snapshot.getValue(String.class);
                tvRegNo.setText(regNo != null && !regNo.isEmpty() ? regNo : "-");
            });


        }

        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(UserInfoActivity.this, EditUserInfoActivity.class);
            intent.putExtra("username", tvUsername.getText().toString());
            intent.putExtra("regNo", tvRegNo.getText().toString());
            startActivityForResult(intent, 1);
        });


        btnSignOut.setOnClickListener(v -> {
            AlertDialog dialog = new AlertDialog.Builder(UserInfoActivity.this)
                    .setTitle("Are you sure you want to sign out?")
                    .setPositiveButton("Yes", (dialogInterface, which) -> {
                        mAuth.signOut();
                        startActivity(new Intent(UserInfoActivity.this, SignInActivity.class));
                        finish();
                    })
                    .setNegativeButton("Cancel", null)
                    .create();

            dialog.setOnShowListener(d -> {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setTextColor(getResources().getColor(R.color.defaultPrimary));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                        .setTextColor(Color.RED);
            });

            dialog.show();
        });


        ImageButton btnExit = findViewById(R.id.btnClose);
        btnExit.setOnClickListener(v -> {
            startActivity(new Intent(UserInfoActivity.this, NewsFeedActivity.class));
            finish();
        });

        Button btnDelete = findViewById(R.id.btnDeleteUser);
        btnDelete.setOnClickListener(view -> {
            AlertDialog dialog = new AlertDialog.Builder(UserInfoActivity.this)
                    .setTitle("Delete Account")
                    .setMessage("Are you sure you want to permanently delete your account?")
                    .setPositiveButton("Yes", null)
                    .setNegativeButton("Cancel", null)
                    .create();

            dialog.setOnShowListener(dialogInterface -> {
                // Style Positive Button
                Button yesButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                yesButton.setTextColor(ContextCompat.getColor(this, R.color.defaultPrimary)); // App's primary color
                yesButton.setOnClickListener(v1 -> {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        user.delete()
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(this, "Account deleted", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(this, SignInActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                    dialog.dismiss();
                });

                // Style Negative Button
                Button cancelButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                cancelButton.setTextColor(Color.RED);
                cancelButton.setOnClickListener(v2 -> dialog.dismiss());
            });

            dialog.show();
        });


    }


    // 🔥 FIXED: onActivityResult placed outside onCreate()
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());

                userRef.child("displayName").get().addOnSuccessListener(snapshot -> {
                    String name = snapshot.getValue(String.class);
                    tvUsername.setText(name != null ? name : "-");
                });

                userRef.child("registrationNumber").get().addOnSuccessListener(snapshot -> {
                    String regNo = snapshot.getValue(String.class);
                    tvRegNo.setText(regNo != null && !regNo.isEmpty() ? regNo : "-");
                });
            }
        }
    }


}

