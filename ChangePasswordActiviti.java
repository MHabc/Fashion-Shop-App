package com.example.fashionapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.fashionapp.databinding.ActivityChangePasswordActivitiBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActiviti extends AppCompatActivity {
    private ActivityChangePasswordActivitiBinding binding;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding=ActivityChangePasswordActivitiBinding.inflate(getLayoutInflater());
        progressDialog=new ProgressDialog(ChangePasswordActiviti.this);
        setContentView(binding.getRoot());
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Glide.with(ChangePasswordActiviti.this)
                .load(user.getPhotoUrl())
                .error(R.drawable.avar_img).into(binding.avatarprofile);
        binding.BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.btnthaydoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickChangePassword();
            }
        });
    }

    private void clickChangePassword() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String oldpassword=binding.changeTen.getText().toString().trim();
        String newPassword=binding.changeEmail.getText().toString().trim();
        String xacnhan=binding.changexacnhan.getText().toString().trim();
        if (oldpassword.isEmpty())
        {
            Toast.makeText(ChangePasswordActiviti.this,"mật khẩu hiện tại chưa chính xac",Toast.LENGTH_LONG).show();
            return;
        }
        if (newPassword.isEmpty()||xacnhan.isEmpty())
        {
            Toast.makeText(ChangePasswordActiviti.this,"mat khau empty",Toast.LENGTH_LONG).show();
            return;
        }
        if (!newPassword.equals(xacnhan))
        {
            Toast.makeText(ChangePasswordActiviti.this,"hãy xác nhận lại mật khẩu",Toast.LENGTH_LONG).show();
            return;
        }
        if (user != null) {
            progressDialog.show();

            // Người dùng đã đăng nhập, tiếp tục với quá trình đổi mật khẩu
            // Yêu cầu người dùng nhập mật khẩu cũ để xác thực
            String oldPassword = binding.changeTen.getText().toString().trim(); // Thay đổi thành input từ người dùng

            // Xác thực mật khẩu cũ
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);
            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                // Xác thực thành công, cho phép người dùng nhập mật khẩu mới
                                String newPassword = binding.changeEmail.getText().toString().trim(); // Thay đổi thành input từ người dùng

                                // Cập nhật mật khẩu mới
                                user.updatePassword(newPassword)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(ChangePasswordActiviti.this,"success",Toast.LENGTH_LONG).show();
                                                } else {
                                                   Toast.makeText(ChangePasswordActiviti.this,"error",Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                            } else {
                                // Xác thực thất bại, thông báo cho người dùng
                               Toast.makeText(ChangePasswordActiviti.this,"mat khau cu khong dung ",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        } else {
            // Người dùng chưa đăng nhập, yêu cầu họ đăng nhập trước khi đổi mật khẩu
            // Thực hiện điều này bằng cách chuyển người dùng đến màn hình đăng nhập hoặc yêu cầu họ đăng nhập lại
           Toast.makeText(ChangePasswordActiviti.this,"ban chua dang nhap",Toast.LENGTH_LONG).show();
        }
    }
}