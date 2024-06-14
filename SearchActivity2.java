package com.example.fashionapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fashionapp.Adapter.PopularAdapter;
import com.example.fashionapp.Domain.ItemDomain;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity2 extends AppCompatActivity {

    private EditText searchEditText;
    private RecyclerView recyclerView;
    private PopularAdapter adapter;
    private ArrayList<ItemDomain> itemDomains;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search2);

        searchEditText = findViewById(R.id.edit1);
        recyclerView = findViewById(R.id.recyitem);
        imageView=(ImageView) findViewById(R.id.btnback) ;
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        recyclerView.setLayoutManager(new GridLayoutManager(SearchActivity2.this,2));
        itemDomains = new ArrayList<>();
        adapter = new PopularAdapter(itemDomains);
        recyclerView.setAdapter(adapter);

// Thêm DividerItemDecoration để tạo khoảng cách giữa các item trong GridLayoutManager
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.HORIZONTAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.set_draable));
        recyclerView.addItemDecoration(dividerItemDecoration);


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Items");

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String searchText = editable.toString().trim();
                if (!searchText.isEmpty()) {
                    searchItems(searchText, databaseReference);
                } else {
                    itemDomains.clear();
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void searchItems(String searchText, DatabaseReference databaseReference) {
        Query query = databaseReference.orderByChild("title").startAt(searchText).endAt(searchText + "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemDomains.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ItemDomain itemDomain = snapshot.getValue(ItemDomain.class);
                        if (itemDomain != null) {
                            itemDomains.add(itemDomain);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });
    }
}
