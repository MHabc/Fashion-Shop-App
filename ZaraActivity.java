package com.example.fashionapp;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.fashionapp.Adapter.PopularAdapter;
import com.example.fashionapp.Domain.ItemDomain;
import com.example.fashionapp.Domain.SliderItem;
import com.example.fashionapp.databinding.ActivityPumaAcitivityBinding;
import com.example.fashionapp.databinding.ActivityZaraBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ZaraActivity extends AppCompatActivity {

    private ActivityZaraBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityZaraBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initAdidas();
        binding.cardback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void initAdidas() {

        DatabaseReference myref = FirebaseDatabase.getInstance().getReference("Items");
        ArrayList<SliderItem> items = new ArrayList<>();
        ArrayList<ItemDomain> itemDomains = new ArrayList<>();
        myref.orderByChild("rating").startAt(2).endAt(3).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        itemDomains.add(issue.getValue(ItemDomain.class));
                    }
                    if (!itemDomains.isEmpty()) {
                        binding.recyl.setLayoutManager(new GridLayoutManager(ZaraActivity.this, 2));
                        binding.recyl.setAdapter(new PopularAdapter(itemDomains));
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}