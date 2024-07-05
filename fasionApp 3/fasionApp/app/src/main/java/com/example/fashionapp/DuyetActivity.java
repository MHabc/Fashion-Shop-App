package com.example.fashionapp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fashionapp.Adapter.DuyetAdapter;
import com.example.fashionapp.Adapter.HistoryAdapter;
import com.example.fashionapp.Domain.Order;
import com.example.fashionapp.databinding.ActivityDuyetBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import io.reactivex.rxjava3.annotations.NonNull;

public class DuyetActivity extends AppCompatActivity {

   private ActivityDuyetBinding bingding;
    private DuyetAdapter adminOrderAdapter;
    private ArrayList<Order> allOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       bingding=ActivityDuyetBinding.inflate(getLayoutInflater());
        setContentView(bingding.getRoot());
        bingding.historyrev.setLayoutManager(new LinearLayoutManager(this));

        allOrders = new ArrayList<>();
        adminOrderAdapter = new DuyetAdapter(this, allOrders);
        bingding.historyrev.setAdapter(adminOrderAdapter);

        fetchAllOrders();
    }

    private void fetchAllOrders() {
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allOrders.clear();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot orderSnapshot : userSnapshot.getChildren()) {
                        Order order = orderSnapshot.getValue(Order.class);
                        if (order != null && "chờ xác nhận".equals(order.getTimestamp())) {
                            order.setTimestamp2(orderSnapshot.getKey()); // Set orderId
                            order.setUserId(userSnapshot.getKey()); // Set userId
                            allOrders.add(order);
                        }
                    }
                }
                adminOrderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DuyetActivity.this, "Error fetching orders", Toast.LENGTH_SHORT).show();
            }
        });
    }
}