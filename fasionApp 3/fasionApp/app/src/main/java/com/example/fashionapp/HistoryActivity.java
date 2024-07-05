package com.example.fashionapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fashionapp.Adapter.HistoryAdapter;
import com.example.fashionapp.Domain.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView historyRecyclerView;
    private HistoryAdapter historyAdapter;
    private ArrayList<Order> orderList;
    private ImageView imgback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyRecyclerView = findViewById(R.id.historyrev);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        imgback = findViewById(R.id.cardback);
        imgback.setOnClickListener(view -> finish());

        orderList = new ArrayList<>();
        historyAdapter = new HistoryAdapter(this, orderList); // Khởi tạo adapter
        historyRecyclerView.setAdapter(historyAdapter); // Gán adapter cho RecyclerView

        fetchOrders();
    }

    private void fetchOrders() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(userId);
            ordersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    orderList.clear();
                    for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                        Order order = orderSnapshot.getValue(Order.class);
                        if (order != null) {
                            orderList.add(order);
                        }
                    }
                    historyAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(HistoryActivity.this, "Error fetching orders", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
