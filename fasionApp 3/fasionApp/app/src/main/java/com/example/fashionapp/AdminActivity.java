package com.example.fashionapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.fashionapp.Adapter.AdminItemAdapter;
import com.example.fashionapp.Domain.ItemDomain;
import com.example.fashionapp.databinding.ActivityAdminBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;

public class AdminActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private ActivityAdminBinding binding;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private Uri selectedImageUri; // To store the selected image URI
    private DatabaseReference itemsRef;
    private ItemDomain object;
   private String firebasePosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebasePosition = getIntent().getStringExtra("firebasePosition");
        // Initialize Firebase Storage
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference().child("images");
        itemsRef = FirebaseDatabase.getInstance().getReference("Items");

        // Setup bottom navigation
        setupBottomNavigation();

        // Load item details
        getBundles();
        initPopular();

        // Set click listener for uploading image
        binding.itemImageView.setOnClickListener(view -> selectImage());
        binding.btnAddItem.setOnClickListener(view -> uploadAndAddItem());
        binding.btndelete.setOnClickListener(view -> deleteItem());
        binding.btnupdate.setOnClickListener(view -> updateItem());
    }

    private void setupBottomNavigation() {
        binding.cardfavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminActivity.this, DuyetActivity.class);
                startActivity(intent);
            }
        });
        binding.cardfabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminActivity.this, ReportActivity.class);
                startActivity(intent);
            }
        });
        binding.listUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminActivity.this, listUserActivity.class);
                startActivity(intent);
            }
        });
        binding.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminActivity.this, logoutAdmin.class);
                startActivity(intent);
            }
        });
    }

    private void initPopular() {
        FirebaseDatabase database = FirebaseDatabase.getInstance(); // Initialize Firebase instance
        DatabaseReference myRef = database.getReference("Items");

        ArrayList<ItemDomain> itemDomains = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        itemDomains.add(snapshot.getValue(ItemDomain.class));
                    }
                    if (!itemDomains.isEmpty()) {
                        binding.adminrev.setLayoutManager(new GridLayoutManager(AdminActivity.this, 2));
                        binding.adminrev.setAdapter(new AdminItemAdapter(itemDomains));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
            }
        });
    }

    private void getBundles() {
        object = (ItemDomain) getIntent().getSerializableExtra("itemDomain");
        if (object != null) {
            binding.itemName.setText(object.getTitle());
            binding.itemPrice.setText("$" + object.getPrice());
            binding.itemRating.setText(object.getRating() + " Rating");
            binding.itemDescription.setText(object.getDescription());
            binding.itemoldprice.setText("$" + object.getOldPrice());

            RequestOptions requestOptions = new RequestOptions().centerCrop();
            Glide.with(this)
                    .load(object.getPicUrl().get(0)) // Assuming picUrl is a list and we load the first image
                    .apply(requestOptions)
                    .into(binding.itemImageView);
        }
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            // Display the selected image in itemImageView
            Glide.with(this)
                    .load(selectedImageUri)
                    .apply(new RequestOptions().centerCrop())
                    .into(binding.itemImageView);
        }
    }

    private void uploadAndAddItem() {
        if (selectedImageUri != null) {
            StorageReference imageRef = storageRef.child("image_" + System.currentTimeMillis() + ".jpg"); // Unique filename

            UploadTask uploadTask = imageRef.putFile(selectedImageUri);
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                // Get the download URL
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    addItemWithNextKey(imageUrl);
                });
            }).addOnFailureListener(exception -> {
                Toast.makeText(AdminActivity.this, "Upload failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }
    private void addItemWithNextKey(String imageUrl) {
        itemsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Retrieve list of existing keys
                    List<String> keys = new ArrayList<>();
                    for (DataSnapshot child : snapshot.getChildren()) {
                        keys.add(child.getKey());
                    }

                    // Generate the next sequential key
                    String nextItemKey = generateNextKey(keys);

                    // Create a new item with the next key
                    ItemDomain newItem = new ItemDomain(nextItemKey,
                            binding.itemName.getText().toString(),
                            binding.itemDescription.getText().toString(),
                            new ArrayList<String>() {{
                                add(imageUrl);
                            }},
                            Double.parseDouble(binding.itemPrice.getText().toString()),
                            Double.parseDouble(binding.itemoldprice.getText().toString()),
                            6,
                            Double.parseDouble(binding.itemRating.getText().toString()),0

                    );

                    // Add the new item to Firebase with the generated key
                    itemsRef.child(nextItemKey).setValue(newItem)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(AdminActivity.this, "Item added successfully", Toast.LENGTH_SHORT).show();
                                // Clear input fields or perform any necessary actions
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(AdminActivity.this, "Failed to add item: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });

                } else {
                    // If no items exist yet, start with key "0"
                    addItemWithNextKey("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
   /* private void addItemToDatabase(String imageUrl) {
         // Generate a unique key for the item
        ItemDomain newItem = new ItemDomain(
                binding.itemName.getText().toString(),
                binding.itemDescription.getText().toString(),
                new ArrayList<String>() {{
                    add(imageUrl);
                }},
                Double.parseDouble(binding.itemPrice.getText().toString()),
                Double.parseDouble(binding.itemoldprice.getText().toString()),
                6,
                Double.parseDouble(binding.itemRating.getText().toString()),0

        );

        itemsRef.child("").setValue(newItem)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Item added successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to add item: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }*/



    private String generateNextKey(List<String> keys) {
        int max = 0;
        for (String key : keys) {
            try {
                int keyInt = Integer.parseInt(key);
                if (keyInt > max) {
                    max = keyInt;
                }
            } catch (NumberFormatException ignored) {
                // Ignore non-numeric keys
            }
        }
        // Increment the highest numeric key found
        return String.valueOf(max + 1);
    }
    private String generateNextKey2(List<String> keys) {
        int max = 0;
        for (String key : keys) {
            try {
                int keyInt = Integer.parseInt(key);
                if (keyInt > max) {
                    max = keyInt;
                }
            } catch (NumberFormatException ignored) {
                // Ignore non-numeric keys
            }
        }
        // Increment the highest numeric key found
        return String.valueOf(max );
    }

    private void updateItem() {
        if (selectedImageUri != null) {
            StorageReference imageRef = storageRef.child("image_" + System.currentTimeMillis() + ".jpg"); // Unique filename

            UploadTask uploadTask = imageRef.putFile(selectedImageUri);
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                // Get the download URL
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    updateItemInDatabase(imageUrl);
                });
            }).addOnFailureListener(exception -> {
                Toast.makeText(AdminActivity.this, "Upload failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else {
            updateItemInDatabase(null);
        }
    }

    private void updateItemInDatabase(String imageUrl) {
        String itemTitleToUpdate = object.getTitle();

        DatabaseReference itemsRef = FirebaseDatabase.getInstance().getReference("Items");

        itemsRef.orderByChild("title").equalTo(itemTitleToUpdate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String itemIdToUpdate = snapshot.getKey();

                        // Update fields in the existing item
                        snapshot.getRef().child("description").setValue(binding.itemDescription.getText().toString());
                        snapshot.getRef().child("imageUrl").setValue(imageUrl);
                        snapshot.getRef().child("price").setValue(Double.parseDouble(binding.itemPrice.getText().toString()));
                        snapshot.getRef().child("oldPrice").setValue(Double.parseDouble(binding.itemoldprice.getText().toString()));
                        snapshot.getRef().child("rating").setValue(Double.parseDouble(binding.itemRating.getText().toString()));

                        Toast.makeText(AdminActivity.this, "Updated item with title: " + itemTitleToUpdate, Toast.LENGTH_SHORT).show();
                        // Refresh the RecyclerView or update UI as needed
                    }
                } else {
                    Toast.makeText(AdminActivity.this, "Item with title '" + itemTitleToUpdate + "' not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }







    private void deleteItem() {
        if (object != null) {
            String itemTitleToDelete = object.getTitle(); // Get the title of the item to delete from object

            // DatabaseReference to the node where items are stored
            DatabaseReference itemsRef = FirebaseDatabase.getInstance().getReference("Items");

            // Query to find the item with the specified title
            itemsRef.orderByChild("title").equalTo(itemTitleToDelete).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            snapshot.getRef().removeValue().addOnSuccessListener(aVoid -> {
                                Toast.makeText(AdminActivity.this, "Deleted item with title: " + itemTitleToDelete, Toast.LENGTH_SHORT).show();
                                // Refresh the RecyclerView or update UI as needed
                            }).addOnFailureListener(e -> {
                                Toast.makeText(AdminActivity.this, "Failed to delete item: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                        }
                    } else {
                        Toast.makeText(AdminActivity.this, "Item with title '" + itemTitleToDelete + "' not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(AdminActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
