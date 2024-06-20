package com.example.fashionapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView historyRecyclerView;
    private HistoryAdapter historyAdapter;
    private List<Order> orderList;
    private ImageView imgback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        historyRecyclerView = findViewById(R.id.historyrev);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        imgback=findViewById(R.id.cardback);
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        orderList = new ArrayList<>();
        historyAdapter = new HistoryAdapter(orderList);
        historyRecyclerView.setAdapter(historyAdapter);

        fetchOrders();
    }

    private void fetchOrders() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            FirebaseDatabase.getInstance().getReference().child("Orders").child(userId)
                    .addValueEventListener(new ValueEventListener() {
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
                            Log.e("HistoryActivity", "Error fetching order data", error.toException());
                        }
                    });
        }
    }
}
