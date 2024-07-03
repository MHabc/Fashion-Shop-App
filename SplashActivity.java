package com.example.fashionapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.reactivex.rxjava3.annotations.NonNull;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash2);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkUserStatus();
            }
        }, 3000);
    }

    private void checkUserStatus() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Nếu người dùng đã đăng nhập, kiểm tra vai trò của họ
            checkUserRole(user.getUid());
        } else {
            // Nếu người dùng chưa đăng nhập, chuyển hướng đến màn hình đăng nhập
            redirectToLoginActivity();
        }
    }

    private void checkUserRole(String userId) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String role = dataSnapshot.child("strole").getValue(String.class);
                if ("admin".equals(role)) {
                    redirectToAdminActivity();
                } else {
                    redirectToMainActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi khi kiểm tra vai trò người dùng
                redirectToLoginActivity();
            }
        });
    }

    private void redirectToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void redirectToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void redirectToAdminActivity() {
        Intent intent = new Intent(this, AdminActivity.class); // Thay thế FavoriteActivity bằng tên Activity của màn hình admin
        startActivity(intent);
        finish();
    }
}
