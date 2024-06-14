package com.example.fashionapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fashionapp.Adapter.PopularAdapter;
import com.example.fashionapp.Domain.ItemDomain;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import android.util.Log;
import android.view.View;

import com.example.fashionapp.databinding.ActivityFavoriteBinding;

import java.util.ArrayList;

import io.reactivex.rxjava3.annotations.NonNull;

public class FavoriteActivity extends AppCompatActivity {
    private ActivityFavoriteBinding binding;
    private FirebaseAuth auth;
    private DatabaseReference mdatabase;
    private ArrayList<ItemDomain> favoriteItemList;
    private PopularAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityFavoriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        mdatabase = FirebaseDatabase.getInstance().getReference();
        favoriteItemList = new ArrayList<>();
        adapter = new PopularAdapter(favoriteItemList);

        binding.recyl.setAdapter(adapter);
        binding.recyl.setLayoutManager((new GridLayoutManager(FavoriteActivity.this, 2)));
        binding.cardback.setOnClickListener(view -> finish());
        Initlisternt();
    }

    private void Initlisternt() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference favoriteItemsRef = mdatabase.child("Favorites").child(userId).child("favoriteItems");

            favoriteItemsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        favoriteItemList.clear();
                        for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                            ItemDomain favoriteItem = itemSnapshot.getValue(ItemDomain.class);
                            if (favoriteItem != null) {
                                favoriteItemList.add(favoriteItem);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("FavoriteActivity", "Failed to read favorite items.", error.toException());
                }
            });
        }
    }
}
