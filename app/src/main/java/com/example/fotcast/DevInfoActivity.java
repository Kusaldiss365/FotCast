package com.example.fotcast;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class DevInfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_info);

        ImageButton btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(v -> finish());

        Button btnExit = findViewById(R.id.btnExit);
        btnExit.setOnClickListener(v -> {
            // Go back to NewsFeedActivity
            Intent intent = new Intent(DevInfoActivity.this, NewsFeedActivity.class);
            startActivity(intent);
            finish(); // optional, closes the current screen
        });
    }
}
