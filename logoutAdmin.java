package com.example.fashionapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class logoutAdmin extends AppCompatActivity {
    private Button btnlogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_logout_admin);
        btnlogout=(Button)findViewById(R.id.btnduyet);
         btnlogout.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 FirebaseAuth.getInstance().signOut();
                 Intent intent=new Intent(logoutAdmin.this,LoginActivity.class);
                 startActivity(intent);
                 finish();
             }
         });
    }
}