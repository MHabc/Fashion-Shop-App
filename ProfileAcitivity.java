package com.example.fashionapp;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.fashionapp.databinding.ActivityProfileAcitivityBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileAcitivity extends AppCompatActivity {
    private ActivityProfileAcitivityBinding bingding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        bingding=ActivityProfileAcitivityBinding.inflate(getLayoutInflater());
        setContentView(bingding.getRoot());
        ShowUserInformation();
       bingding.BackBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               finish();
           }
       });
    }
    public void ShowUserInformation()
    {
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(user==null)
        {
            return;
        }

        String name = user.getDisplayName();
        String email=user.getEmail();
        Uri uri=user.getPhotoUrl();
        bingding.changeTen.setText(name);
        if (name.isEmpty())
        {
            bingding.changeTen.setText(" ban chua thiet lao ten tai khoan ");
        }

        bingding.changeEmail.setText(email);
        Glide.with(this)
                .load(uri)
                .error(R.drawable.avar_img)
                .into(bingding.avatarprofile);

    }



}