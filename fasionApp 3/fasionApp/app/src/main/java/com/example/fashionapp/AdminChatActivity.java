package com.example.fashionapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fashionapp.Adapter.ChatAdapter;
import com.example.fashionapp.Domain.Message;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import io.reactivex.rxjava3.annotations.NonNull;

public class AdminChatActivity extends AppCompatActivity {
    private DatabaseReference adminMessagesRef;
    private EditText messageInput;
    private Button sendMessageButton;
    private RecyclerView recyclerView;
    private ChatAdapter adapter;

    private String userId = "user1"; // Replace with actual user ID
    private String adminId = "admin1"; // Replace with actual admin ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Initialize Views
        messageInput = findViewById(R.id.message_input);
        sendMessageButton = findViewById(R.id.send_message_button);
        recyclerView = findViewById(R.id.recycler_view);

        // Initialize Firebase Database references
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        adminMessagesRef = database.getReference().child("messages").child("adminMessages").child(adminId);

        // Setup RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ChatAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // Send message button click listener
        sendMessageButton.setOnClickListener(v -> sendMessage());

        // Listen for new messages
        adminMessagesRef.addChildEventListener(new ChildEventListener() {
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
                Toast.makeText(AdminChatActivity.this, "Failed to load messages.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessage() {
        String messageText = messageInput.getText().toString().trim();
        if (!messageText.isEmpty()) {
            long timestamp = System.currentTimeMillis();
            Message message = new Message(messageText, timestamp, "admin");
            adminMessagesRef.push().setValue(message)
                    .addOnSuccessListener(aVoid -> {
                        messageInput.setText("");
                        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
                    })
                    .addOnFailureListener(e -> Toast.makeText(AdminChatActivity.this, "Failed to send message.", Toast.LENGTH_SHORT).show());
        }
    }
}
