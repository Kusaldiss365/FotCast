package com.example.fotcast;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NewsFeedActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private List<NewsItem> newsList = new ArrayList<>();
    private List<NewsItem> filteredList = new ArrayList<>();
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_create_post) {
                startActivity(new Intent(this, CreatePostActivity.class));
            } else if (id == R.id.nav_user_info) {
                startActivity(new Intent(this, UserInfoActivity.class));
            } else if (id == R.id.nav_dev_info) {
                startActivity(new Intent(this, DevInfoActivity.class));
            } else if (id == R.id.nav_sign_out) {
                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setTitle("Are you sure you want to sign out?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(this, SignInActivity.class));
                            finish();
                        })
                        .setNegativeButton("Cancel", null)
                        .create();

                alertDialog.setOnShowListener(dialogInterface -> {
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                            .setTextColor(getResources().getColor(R.color.defaultPrimary));
                    alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                            .setTextColor(Color.RED);
                });

                alertDialog.show();
            }

            drawerLayout.closeDrawers();
            return true;
        });

        recyclerView = findViewById(R.id.newsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_all);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_sports) {
                filterNews("Sports");
                return true;
            } else if (id == R.id.nav_education) {
                filterNews("Education");
                return true;
            } else if (id == R.id.nav_all) {
                filterNews("All");
                return true;
            }
            return false;
        });

        // Get current user ID for delete permission check
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        fetchNewsFromFirebase(); // also calls filterNews("All")
    }

    private void fetchNewsFromFirebase() {
        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("posts");

        postsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                newsList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    NewsItem post = postSnapshot.getValue(NewsItem.class);
                    if (post != null) {
                        post.setKey(postSnapshot.getKey()); // Needed for deletion
                        newsList.add(post);
                    }
                }
                filterNews("All");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(NewsFeedActivity.this, "Failed to load posts", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterNews(String category) {
        filteredList.clear();
        if (category.equals("All")) {
            filteredList.addAll(newsList);
        } else {
            for (NewsItem item : newsList) {
                if (item.getCategory().equalsIgnoreCase(category)) {
                    filteredList.add(item);
                }
            }
        }

        adapter = new NewsAdapter(filteredList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logo) {
            Toast.makeText(this, "Logo clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
