package com.example.fashionapp;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.fashionapp.Adapter.PopularAdapter;
import com.example.fashionapp.Domain.ItemDomain;
import com.example.fashionapp.databinding.ActivityAllActivytiBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllActivyti extends AppCompatActivity {
    private FirebaseDatabase database;
    private ActivityAllActivytiBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityAllActivytiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance(); // Properly initialize the database reference

        binding.btnback.setOnClickListener(view -> finish());

        initializeItems();
    }

    private void initializeItems() {
        DatabaseReference myRef = database.getReference("Items");
        ArrayList<ItemDomain> itemDomains = new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        ItemDomain item = issue.getValue(ItemDomain.class);
                        if (item != null) {
                            itemDomains.add(item);
                        }
                    }
                    if (!itemDomains.isEmpty()) {
                        binding.recyall.setLayoutManager(new GridLayoutManager(AllActivyti.this, 2));
                        binding.recyall.setAdapter(new PopularAdapter(itemDomains));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors.
            }
        });
    }
}
