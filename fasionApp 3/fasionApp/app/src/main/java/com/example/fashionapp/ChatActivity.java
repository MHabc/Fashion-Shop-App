package com.example.fashionapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fashionapp.Adapter.ChatAdapter;
import com.example.fashionapp.Domain.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {
    private DatabaseReference userMessagesRef;
    private EditText messageInput;
    private Button sendMessageButton;
    private RecyclerView recyclerView;
    private ChatAdapter adapter;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String userId; // User ID based on current logged in user

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            // Handle the case where no user is logged in
            Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show();
            finish(); // Close activity if no user is logged in
            return;
        }

        userId = currentUser.getUid();

        // Initialize Views
        messageInput = findViewById(R.id.message_input);
        sendMessageButton = findViewById(R.id.send_message_button);
        recyclerView = findViewById(R.id.recycler_view);

        // Initialize Firebase Database references
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        userMessagesRef = database.getReference().child("messages").child("userMessages").child(userId);

        // Setup RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ChatAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // Send message button click listener
        sendMessageButton.setOnClickListener(v -> sendMessage());

        // Listen for new messages
        userMessagesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message message = snapshot.getValue(Message.class);
                if (message != null) {
                    adapter.addMessage(message);
                    recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChatActivity.this, "Failed to load messages.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessage() {
        String messageText = messageInput.getText().toString().trim();
        if (!messageText.isEmpty()) {
            long timestamp = System.currentTimeMillis();
            Message message = new Message(messageText, timestamp, userId); // Include userId in message
            DatabaseReference newMessageRef = userMessagesRef.push();
            newMessageRef.setValue(message)
                    .addOnSuccessListener(aVoid -> {
                        messageInput.setText("");
                        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
                    })
                    .addOnFailureListener(e -> Toast.makeText(ChatActivity.this, "Failed to send message.", Toast.LENGTH_SHORT).show());
        }
    }
}
