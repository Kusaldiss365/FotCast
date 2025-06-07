package com.example.fotcast;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreatePostActivity extends AppCompatActivity {

    private EditText etTitle, etContent;
    private Spinner spinnerCategory;
    private Button btnSubmit;
    private DatabaseReference postsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        etTitle = findViewById(R.id.etTitle);
        etContent = findViewById(R.id.etContent);
        spinnerCategory = findViewById(R.id.categorySpinner);
        btnSubmit = findViewById(R.id.btnSubmit);

        postsRef = FirebaseDatabase.getInstance().getReference("posts");

        // Spinner setup
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.categories_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        btnSubmit.setOnClickListener(v -> submitPost());
    }

    private void submitPost() {
        String title = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        NewsItem newsItem = new NewsItem(title, category, content);
        postsRef.push().setValue(newsItem).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(CreatePostActivity.this, "Post submitted", Toast.LENGTH_SHORT).show();
                finish(); // close the activity
            } else {
                Toast.makeText(CreatePostActivity.this, "Failed to submit", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
