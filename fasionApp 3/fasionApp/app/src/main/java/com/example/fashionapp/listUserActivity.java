package com.example.fashionapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fashionapp.Adapter.UserListAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class listUserActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserListAdapter adapter;
    private List<Users> userList;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listuser);

        // Initialize RecyclerView and adapter
        recyclerView = findViewById(R.id.historyrev);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userList = new ArrayList<>();
        adapter = new UserListAdapter(this, userList);
        recyclerView.setAdapter(adapter);

        // Initialize Firebase Database reference
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        // Load users from Firebase Database
        loadUsers();
    }

    private void loadUsers() {
        usersRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Users user = snapshot.getValue(Users.class);
                if (user != null) {
                    user.setUid(snapshot.getKey()); // Set user ID from Firebase snapshot key
                    userList.add(user);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Handle updated user data
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                // Handle removed user
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Handle moved user (if applicable)
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error loading users
            }
        });
    }
}
