package com.example.fashionapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fashionapp.databinding.ActivityChangePhoneAndAdressBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class changePhoneAndAdressActivity extends AppCompatActivity {
    ActivityChangePhoneAndAdressBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding=ActivityChangePhoneAndAdressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnthaydoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Onlickthaydroiid();
            }
        });
        binding.BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent=new Intent(changePhoneAndAdressActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void Onlickthaydroiid() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);

            // Update phone number
            String newPhoneNumber = binding.changephone.getText().toString().trim();
            userRef.child("phone").setValue(newPhoneNumber);

            // Update address
            String newAddress = binding.addressUser.getText().toString().trim();
            userRef.child("address").setValue(newAddress);

            // Update both phone number and address simultaneously
            Map<String, Object> updates = new HashMap<>();
            updates.put("phone", newPhoneNumber);
            updates.put("address", newAddress);
            userRef.updateChildren(updates);
            Toast.makeText(changePhoneAndAdressActivity.this, "succcess", Toast.LENGTH_SHORT).show();
        }

    }
}