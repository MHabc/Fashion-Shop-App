package com.example.fashionapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.fashionapp.databinding.ActivityChangeprofBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class changeprof extends AppCompatActivity {

    private ActivityChangeprofBinding binding;
    private ProgressDialog progressDialog;
     Uri uri2;

    // Request code for image selection
    private static final int PICK_IMAGE_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityChangeprofBinding.inflate(getLayoutInflater());
        progressDialog=new ProgressDialog(changeprof.this);
        setContentView(binding.getRoot());
        setifo();
        binding.avatarprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open gallery to select an image
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });
        binding.BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent=new Intent(changeprof.this,MainActivity.class);
               startActivity(intent);
            }
        });
        binding.btnthaydoi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onclickUpdateEmail();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                String name=binding.changeTen.getText().toString().trim();
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .setPhotoUri(uri2)
                        .build();
                progressDialog.show();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    progressDialog.dismiss();

                                    // Cập nhật photoUri vào Firebase Realtime Database
                                    updatePhotoUrl(user.getUid(), uri2.toString());

                                    // Cập nhật username vào Firebase Realtime Database
                                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
                                    userRef.child("username").setValue(name)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(changeprof.this,"Profile updated successfully",Toast.LENGTH_LONG).show();
                                                    } else {
                                                        Toast.makeText(changeprof.this,"Failed to update username",Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                } else {
                                    Toast.makeText(changeprof.this,"Failed to update profile",Toast.LENGTH_LONG).show();
                                }
                            }
                        });

            }
        });
    }

    private void onclickUpdateEmail() {
        String emial=binding.changeEmail.getText().toString().trim();
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();



        user.updateEmail(emial)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                           Toast.makeText(changeprof.this,"succccceesss",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    // Handle result of image selection
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the image URI
            Uri imageUri = data.getData();
            // Now you can use this imageUri to set the user's profile picture
            // For example:
            Glide.with(this).load(imageUri).into(binding.avatarprofile);
            uri2=imageUri;
        }
    }


    private void setifo() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        binding.changeTen.setText(user.getDisplayName());
        binding.changeEmail.setText(user.getEmail());
        Glide.with(changeprof.this).load(user.getPhotoUrl()).error(R.drawable.avar_img)
                .into(binding.avatarprofile);
    }
    public Uri setUri2(Uri uri)
    {
        return uri;
    }
    private void updatePhotoUrl(String userId, String photoUrl) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

        userRef.child("photoUri").setValue(photoUrl)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Photo URL updated successfully
                        } else {
                            // Failed to update photo URL
                        }
                    }
                });
    }

}
