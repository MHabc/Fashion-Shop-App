package com.example.fashionapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fashionapp.Adapter.ReviewAdapter;
import com.example.fashionapp.Domain.ReviewDomain;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ReportActivity extends AppCompatActivity {
     private RecyclerView desctxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Review");
        ArrayList<ReviewDomain> list = new ArrayList<>();
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Được gọi khi một child mới được thêm vào
                ReviewDomain review = snapshot.getValue(ReviewDomain.class);
                if (review != null) {
                    list.add(review);
                    // Cập nhật RecyclerView khi có dữ liệu mới
                     desctxt =findViewById(R.id.historyrev);
                    desctxt.setAdapter(new ReviewAdapter(list));
                    desctxt.setLayoutManager(new LinearLayoutManager(ReportActivity.this, LinearLayoutManager.VERTICAL, false));
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Được gọi khi một child được thay đổi
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                // Được gọi khi một child bị xóa
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Được gọi khi một child được di chuyển
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý khi có lỗi xảy ra
            }
        });
    }
}