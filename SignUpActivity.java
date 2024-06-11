package com.example.fashionapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fashionapp.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;
    private String stremail;
    private String strpassword;
    private String strconfirmpassword;
    private String strRole;
    private FirebaseAuth auth;
    private DatabaseReference mdatabase;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressDialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        mdatabase = FirebaseDatabase.getInstance().getReference();
        initlisterner();
    }

    private void initlisterner() {
        binding.btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.btndangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stremail = binding.edtTaiKhoan.getText().toString().trim();
                strpassword = binding.edtmatkhau.getText().toString().trim();
                strconfirmpassword = binding.edtxacnhanmk.getText().toString().trim();


                strRole = "User";
                if (stremail.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Bạn chưa nhập Email", Toast.LENGTH_LONG).show();
                    return;
                }
                if (strpassword.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Bạn chưa nhập Mật Khẩu", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!strpassword.equals(strconfirmpassword)) {
                    Toast.makeText(SignUpActivity.this, "Vui lòng xác nhận lại mật khẩu của bạn", Toast.LENGTH_LONG).show();
                    return;
                }
                OnclickSignup();
            }
        });
    }

    private void OnclickSignup() {
        progressDialog.show();
        auth.createUserWithEmailAndPassword(stremail, strpassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            if (user != null) {
                                String strUsername = "";
                                String photoUriString = "";

                                Users newUser = new Users(strUsername, stremail, user.getUid(), photoUriString, strRole);
                                mdatabase.child("users").child(user.getUid()).setValue(newUser)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(SignUpActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                                    startActivity(intent);
                                                    finishAffinity();
                                                } else {
                                                    Toast.makeText(SignUpActivity.this, "Không thể lưu dữ liệu người dùng", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                Toast.makeText(SignUpActivity.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SignUpActivity.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
