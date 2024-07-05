package com.example.fashionapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fashionapp.Adapter.OnSizeSelectedListener;
import com.example.fashionapp.Adapter.SizeAdapter;
import com.example.fashionapp.Adapter.SliderAdapter;
import com.example.fashionapp.Domain.ItemDomain;
import com.example.fashionapp.Domain.SliderItem;
import com.example.fashionapp.Fragment.DescriptionFragment;
import com.example.fashionapp.Fragment.ReviewFragment;
import com.example.fashionapp.Fragment.SoldFragment;
import com.example.fashionapp.Helper.ManagmentCart;
import com.example.fashionapp.databinding.ActivityDetailActivitiBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailActiviti extends AppCompatActivity {
    private ActivityDetailActivitiBinding binding;
    private ItemDomain ojbect;
    public int numberoder = 1;
    private ManagmentCart managmentCart;
    private FirebaseAuth auth;
    private DatabaseReference mdatabase;
    private boolean isclicked = false;
    private Handler sliderHandle = new Handler();
    private FirebaseUser user;
    private static final String PREFS_NAME = "FavoritePrefs";
    private static final String FAVORITE_KEY_PREFIX = "favorite_";
    private String selectedSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityDetailActivitiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        managmentCart = new ManagmentCart(this);
        getBundles();
        banners();
        initSize();
        setupViewPager();
        mdatabase = FirebaseDatabase.getInstance().getReference();
        FavoriteManager();

        // Load the favorite state
        isclicked = loadFavoriteState();
        updateFavoriteButtonState();
    }

    private void initSize() {
        ArrayList<String> list = new ArrayList<>();
        list.add("S");
        list.add("M");
        list.add("L");
        list.add("XL");
        list.add("XXL");
        SizeAdapter sizeAdapter=new SizeAdapter(list, new OnSizeSelectedListener() {
            @Override
            public void onSizeSelected(String size) {
                selectedSize = size;
            }
        });
        binding.RecyclerSize.setAdapter(sizeAdapter);
        binding.RecyclerSize.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void banners() {
        ArrayList<SliderItem> sliderItems = new ArrayList<>();
        for (int i = 0; i < ojbect.getPicUrl().size(); i++) {
            sliderItems.add(new SliderItem(ojbect.getPicUrl().get(i)));
        }
        binding.SliderViewpager2.setAdapter(new SliderAdapter(sliderItems, binding.SliderViewpager2));
        binding.SliderViewpager2.setClipToPadding(false);
        binding.SliderViewpager2.setClipChildren(false);
        binding.SliderViewpager2.setOffscreenPageLimit(3);
        binding.SliderViewpager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
    }

    public void FavoriteManager() {
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Favorites");
    }

    private void addFavorite(ItemDomain item) {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference favoriteItemsRef = mdatabase.child(userId).child("favoriteItems");
            favoriteItemsRef.push().setValue(item)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(DetailActiviti.this, "Item added to favorites", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(DetailActiviti.this, "Failed to add item to favorites", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void removeFavorite(ItemDomain object) {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference favoriteItemRef = mdatabase.child(userId).child("favoriteItems");
            favoriteItemRef.orderByChild("title").equalTo(object.getTitle()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        snapshot.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(DetailActiviti.this, "Item removed from favorites", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(DetailActiviti.this, "Failed to remove item from favorites", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                }
            });
        }
    }

    private void getBundles() {
        ojbect = (ItemDomain) getIntent().getSerializableExtra("object");
        binding.titleTxt.setText(ojbect.getTitle());
        binding.pricetxt.setText("$" + ojbect.getPrice());
        binding.ratingBar.setRating((float) ojbect.getRating());
        binding.ratingtxt.setText(ojbect.getRating() + " Rating");
        binding.addtocardbtn.setOnClickListener(view -> {
            if (selectedSize == null) {
                Toast.makeText(DetailActiviti.this, "Please select a size", Toast.LENGTH_SHORT).show();
                return;
            }
            ojbect.setNumberinCard(numberoder);
            managmentCart.insertFood(ojbect, selectedSize);
        });

        binding.FavBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isclicked = !isclicked;
                saveFavoriteState(isclicked);
                updateFavoriteButtonState();
                if (isclicked) {
                    if (user != null) {
                        addFavorite(ojbect);
                    } else {
                        Toast.makeText(DetailActiviti.this, "User is not signed in", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    removeFavorite(ojbect);
                }
            }
        });

        binding.BackBtn.setOnClickListener(view -> finish());
    }

    private void updateFavoriteButtonState() {
        if (isclicked) {
            binding.FavBtn.setColorFilter(R.color.grey);
        } else {
            binding.FavBtn.setColorFilter(null);
        }
    }

    private void saveFavoriteState(boolean isFavorite) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(FAVORITE_KEY_PREFIX + ojbect.getTitle(), isFavorite);
        editor.apply();
    }

    private boolean loadFavoriteState() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return sharedPreferences.getBoolean(FAVORITE_KEY_PREFIX + ojbect.getTitle(), false);
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        DescriptionFragment tab1 = new DescriptionFragment();
        ReviewFragment tab2 = new ReviewFragment();
        SoldFragment tab3 = new SoldFragment();
        Bundle bundle1 = new Bundle();
        Bundle bundle2 = new Bundle();
        Bundle bundle3 = new Bundle();
        bundle1.putString("description", ojbect.getDescription());
        tab1.setArguments(bundle1);
        tab2.setArguments(bundle2);
        tab3.setArguments(bundle3);
        adapter.addFragment(tab1, "Descriptions");
        adapter.addFragment(tab2, "Reviews");
        adapter.addFragment(tab3, "Sold");
        binding.viewpager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewpager);
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> fragmentsList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentsList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentsList.size();
        }

        private void addFragment(Fragment fragment, String title) {
            fragmentsList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }
}
