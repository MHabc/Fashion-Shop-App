package com.example.fashionapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fashionapp.databinding.ActivityChangePasswordActivitiBinding;
import com.example.fashionapp.databinding.ActivityForgotpasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotpasswordActivity extends AppCompatActivity {
    private ActivityForgotpasswordBinding binding;
    private ProgressDialog progesss;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
       binding=ActivityForgotpasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progesss=new ProgressDialog(ForgotpasswordActivity.this);
        binding.btnxacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onclickxacnhan();
            }
        });
        binding.btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void onclickxacnhan() {
        progesss.show();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = binding.edtTaiKhoan.getText().toString().trim();
        binding.btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progesss.dismiss();
                            Toast.makeText(ForgotpasswordActivity.this, "email send", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(ForgotpasswordActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }
}