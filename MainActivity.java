package com.example.fashionapp;
import androidx.core.view.GravityCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import com.bumptech.glide.Glide;
import com.example.fashionapp.Adapter.CategoryAdapter;
import com.example.fashionapp.Adapter.PopularAdapter;
import com.example.fashionapp.Adapter.SliderAdapter;
import com.example.fashionapp.Domain.CategoryItem;
import com.example.fashionapp.Domain.ItemDomain;
import com.example.fashionapp.Domain.SliderItem;

import com.example.fashionapp.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {


   private ActivityMainBinding binding;
    private TextView txttendaidien;
    private TextView txtemail;
    private ImageView imgavartar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=com.example.fashionapp.databinding.ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
         imgavartar = binding.navView.getHeaderView(0).findViewById(R.id.imgavatar);
         txttendaidien= binding.navView.getHeaderView(0).findViewById(R.id.tentxt);
         txtemail = binding.navView.getHeaderView(0).findViewById(R.id.emailtxt);
         binding.navView.setNavigationItemSelectedListener(this);
        OnclickItemMenu();
        initBanner();
        initCategory();
        initPopuplar();
        bottomnavigation();
        ShowUserInformation();
        seeall();
        binding.edit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Search();
            }
        });
        updateItem();
    }

    private void seeall() {
        binding.textView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, AllActivyti.class);
                startActivity(intent);
            }
        });
    }

    private void Search() {
        Intent intent=new Intent(MainActivity.this,SearchActivity2.class);
        startActivity(intent);
    }




   private void updateItem() {
        FirebaseDatabase database1= FirebaseDatabase.getInstance();
        DatabaseReference myref=database1.getReference("Items");
        List<ItemDomain> itemDomains=new ArrayList<>();
        ArrayList<String> Url1=new ArrayList<>();
        ArrayList<String> Url2=new ArrayList<>();
        ArrayList<String> Url3=new ArrayList<>();
        ArrayList<String> Url4=new ArrayList<>();
       ArrayList<String> Url5=new ArrayList<>();
       ArrayList<String> Url6=new ArrayList<>();
       ArrayList<String> Url7=new ArrayList<>();
       ArrayList<String> Url8=new ArrayList<>();
       ArrayList<String> Url9=new ArrayList<>();
       ArrayList<String> Url10=new ArrayList<>();
       ArrayList<String> Url11=new ArrayList<>();
       ArrayList<String> Url12=new ArrayList<>();
       ArrayList<String> Url13=new ArrayList<>();
       ArrayList<String> Url14=new ArrayList<>();
       ArrayList<String> Url15=new ArrayList<>();
       ArrayList<String> Url16=new ArrayList<>();
       ArrayList<String> Url17=new ArrayList<>();
       ArrayList<String> Url18=new ArrayList<>();
       ArrayList<String> Url19=new ArrayList<>();
       ArrayList<String> Url20=new ArrayList<>();
       ArrayList<String> Url21=new ArrayList<>();
       ArrayList<String> Url22=new ArrayList<>();
       ArrayList<String> Url23=new ArrayList<>();
       ArrayList<String> Url24=new ArrayList<>();

        Url1.add("https://firebasestorage.googleapis.com/v0/b/fashionapp-6bf21.appspot.com/o/Aidas1.png?alt=media&token=5a2bba25-64c8-4512-a956-2cac8658b709");
        Url2.add("https://firebasestorage.googleapis.com/v0/b/fashionapp-6bf21.appspot.com/o/Nike1.png?alt=media&token=41af67b2-5710-4790-9be7-c91b08b70254");
        Url3.add("https://firebasestorage.googleapis.com/v0/b/fashionapp-6bf21.appspot.com/o/Zara1.png?alt=media&token=730f2f5e-9550-485e-a090-616da7199417");
        Url4.add("https://firebasestorage.googleapis.com/v0/b/fashionapp-6bf21.appspot.com/o/Puma1.png?alt=media&token=2ebb4957-ab7a-4be2-bf1c-aa84c9bcd8da");
       Url5.add("https://firebasestorage.googleapis.com/v0/b/fashionapp-6bf21.appspot.com/o/aosomingantayA.png?alt=media&token=36365e78-7bf6-4788-911b-8879287851e1");
       Url6.add("https://firebasestorage.googleapis.com/v0/b/fashionapp-6bf21.appspot.com/o/aopolotenis.png?alt=media&token=b3ef832b-effe-4143-84f0-0865963aa6fe");
       Url7.add("https://firebasestorage.googleapis.com/v0/b/fashionapp-6bf21.appspot.com/o/aothunnubrand.png?alt=media&token=3255035c-fe36-4cbf-934e-9ab74a0c4a87");
       Url8.add("https://firebasestorage.googleapis.com/v0/b/fashionapp-6bf21.appspot.com/o/aothunnupride.png?alt=media&token=ceba6499-6d0d-462d-8e63-6218a87a38ce");
       Url9.add("https://firebasestorage.googleapis.com/v0/b/fashionapp-6bf21.appspot.com/o/Nike%20Miler%20Flash.png?alt=media&token=9adbcfac-efe2-4ade-9145-e6ea90c49748");
       Url10.add("https://firebasestorage.googleapis.com/v0/b/fashionapp-6bf21.appspot.com/o/Nike%20Sportswear%20Premium%20Essentials.png?alt=media&token=5a3a60b0-c8f2-4246-8727-aa4b0c722d8a");
       Url11.add("https://firebasestorage.googleapis.com/v0/b/fashionapp-6bf21.appspot.com/o/Nike%20Sportswear%20Essential.png?alt=media&token=1102932b-2f19-4b6c-8d70-d957b6e84446");
       Url12.add("https://firebasestorage.googleapis.com/v0/b/fashionapp-6bf21.appspot.com/o/Nike%20Air.png?alt=media&token=c5b1ac4f-7930-4d14-8cc2-1856f51ac660");
       Url13.add("https://firebasestorage.googleapis.com/v0/b/fashionapp-6bf21.appspot.com/o/BEVERLY%20HILLS%20POLO%20CLUB.png?alt=media&token=6f1ad008-6aac-42f1-92e7-da65a0571bec");
       Url14.add("https://firebasestorage.googleapis.com/v0/b/fashionapp-6bf21.appspot.com/o/%C3%81o%20thun%20unisex%20c%E1%BB%95%20tr%C3%B2n%20tay%20ng%E1%BA%AFn%20Better%20Classic.png?alt=media&token=e49d21c8-6b04-4895-97fc-85b1fc96e922");
       Url15.add("https://firebasestorage.googleapis.com/v0/b/fashionapp-6bf21.appspot.com/o/%C3%81o%20thun%20unisex%20c%E1%BB%95%20tr%C3%B2n%20tay%20ng%E1%BA%AFn%20Basic%20Small%20Logo.png?alt=media&token=9b40cf9a-4519-4718-9119-8491fa358f6f");
       Url16.add("https://firebasestorage.googleapis.com/v0/b/fashionapp-6bf21.appspot.com/o/%C3%81o%20thun%20unisex%20c%E1%BB%95%20tr%C3%B2n%20tay%20ng%E1%BA%AFn%20Classic%20Monogram%20Biglux.png?alt=media&token=b09469af-4491-47b4-9410-a06558087beb");
       Url17.add("https://firebasestorage.googleapis.com/v0/b/fashionapp-6bf21.appspot.com/o/%C3%81O%20S%C6%A0%20MI%20V%E1%BA%A2I%20D%E1%BB%86T%20H%E1%BB%8CA%20TI%E1%BA%BET%20K%E1%BA%BA%20S%E1%BB%8CC.png?alt=media&token=241f70e1-ccc7-4b03-a2c0-686cf34e5dea");
       Url18.add("https://firebasestorage.googleapis.com/v0/b/fashionapp-6bf21.appspot.com/o/%C3%81O%20S%C6%A0%20MI%20V%E1%BA%A2I%20R%C5%A8%20B%C3%93NG%20PH%E1%BB%90I%20M%C3%80U%20OMBRE%20IN%20H%E1%BB%8CA%20TI%E1%BA%BET.png?alt=media&token=aa2c9d17-f358-4ede-9532-d77930283a8b");
       Url19.add("https://firebasestorage.googleapis.com/v0/b/fashionapp-6bf21.appspot.com/o/%C3%81O%20D%C3%81NG%20GI%20L%C3%8A%20V%E1%BA%A2I%20TH%C3%94.png?alt=media&token=1c2817d8-a621-4257-8521-1e85d585282c");
       Url20.add("https://firebasestorage.googleapis.com/v0/b/fashionapp-6bf21.appspot.com/o/%C3%81O%20GI%20L%C3%8A%20TAILORED%20FIT.png?alt=media&token=65679c02-dd54-48c3-86f0-fa061aaa2f57");
       Url21.add("");
       Url22.add("");
       Url23.add("");
       Url24.add("");
        itemDomains.add(new ItemDomain("áo thun 1","chiếc áo 1 ",Url1,25,36,6,4.4,0));
        itemDomains.add(new ItemDomain("áo thun 2","chiếc áo 2 ",Url2,25,37,7,4.3,0));
        itemDomains.add(new ItemDomain("áo thun 3","chiếc áo 3 ",Url3,25,48,8,4.5,0));
        itemDomains.add(new ItemDomain("áo thun 4","chiếc áo 4 ",Url4,25,30,9,4.6,0));
       itemDomains.add(new ItemDomain("ao so mi ngan tay  ","chiếc áo 5 ",Url5,25,36,6,0.2,0));
       itemDomains.add(new ItemDomain("ao polo tenis","chiếc áo 6 ",Url6,25,36,6,0.4,0));
       itemDomains.add(new ItemDomain("ao thun nu brand","chiếc áo 7 ",Url7,25,36,6,0.6,0));
       itemDomains.add(new ItemDomain("ao thun nu pride","chiếc áo 8 ",Url8,25,36,6,0.8,0));
       itemDomains.add(new ItemDomain("Nike Miler Flash  ","chiếc áo 5 ",Url9,25,36,6,1.2,0));
       itemDomains.add(new ItemDomain("Nike Sportswear Premium Essentials","chiếc áo 6 ",Url10,25,36,6,1.4,0));
       itemDomains.add(new ItemDomain("Nike Sportswear Essential","chiếc áo 7 ",Url11,25,36,6,1.6,0));
       itemDomains.add(new ItemDomain("Nike Air","chiếc áo 8 ",Url12,25,36,6,1.8,0));
       itemDomains.add(new ItemDomain("BEVERLY HILLS POLO CLUB ","chiếc áo 5 ",Url17,25,36,6,2.2,0));
       itemDomains.add(new ItemDomain("ÁO SƠ MI VẢI RŨ BÓNG PHỐI MÀU","chiếc áo 6 ",Url18,25,36,6,2.3,0));
       itemDomains.add(new ItemDomain("ÁO DÁNG GI LÊ VẢI THÔ","chiếc áo 7 ",Url19,25,36,6,2.6,0));
       itemDomains.add(new ItemDomain("ÁO GI LÊ TAILORED FIT","chiếc áo 8 ",Url20,25,36,6,2.8,0));

        myref.setValue(itemDomains).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

            } else {
                Toast.makeText(this, "add  ", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void OnclickItemMenu() {
        setSupportActionBar(binding.toolbar);
        ActionBarDrawerToggle  toggle=new ActionBarDrawerToggle(this,binding.drawerLayout,binding.toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void bottomnavigation() {
      binding.cardfabtn.setOnClickListener(view -> {
          Intent intent=new Intent(MainActivity.this,CartActivity.class);
          startActivity(intent);
      });
      binding.cardfavorite.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent intent=new Intent(MainActivity.this,FavoriteActivity.class);
              startActivity(intent);
          }
      });
      binding.profile.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent intent=new Intent(MainActivity.this,ProfileAcitivity.class);
              startActivity(intent);
          }
      });
    }

    private void initPopuplar() {
        DatabaseReference myref= database.getReference("Items");
        binding.progressBarPopular.setVisibility(View.VISIBLE);
        ArrayList<ItemDomain> itemDomains= new ArrayList<>();
        myref.limitToFirst(4).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    for(DataSnapshot issue: snapshot.getChildren())
                    {
                        itemDomains.add(issue.getValue(ItemDomain.class));
                    }
                    if (!itemDomains.isEmpty())
                    {
                        binding.recyclerviewPopular.setLayoutManager(new GridLayoutManager(MainActivity.this,2));
                        binding.recyclerviewPopular.setAdapter(new PopularAdapter(itemDomains));
                    }
                    binding.progressBarPopular.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initCategory() {
        DatabaseReference myref=database.getReference("Category");
        binding.progressBarOfficial.setVisibility(View.VISIBLE);
        ArrayList<CategoryItem> items= new ArrayList<>();
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    for(DataSnapshot issue: snapshot.getChildren())
                    {
                        items.add(issue.getValue(CategoryItem.class));
                    }
                    if (!items.isEmpty())
                    {
                        binding.recyclerViewOfficial.setLayoutManager(new LinearLayoutManager(MainActivity.this
                                ,LinearLayoutManager.HORIZONTAL,false));
                        binding.recyclerViewOfficial.setAdapter(new CategoryAdapter(items));

                    }
                   binding.progressBarOfficial.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initBanner() {
        DatabaseReference myref= database.getReference("Banner");
        binding.progressBar.setVisibility(View.VISIBLE);
        ArrayList<SliderItem> items=new ArrayList<>();
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(snapshot.exists())
            {
                for (DataSnapshot issue:snapshot.getChildren()){
                                items.add(issue.getValue(SliderItem.class));
                }
                banners(items);
                binding.progressBar.setVisibility(View.GONE);

            }


        }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        }
    private void banners(ArrayList<SliderItem> items) {
       binding.ViewpagerSlider.setAdapter(new SliderAdapter(items,binding.ViewpagerSlider));
       binding.ViewpagerSlider.setClipToPadding(false);
       binding.ViewpagerSlider.setClipChildren(false);
       binding.ViewpagerSlider.setOffscreenPageLimit(3);
       binding.ViewpagerSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        CompositePageTransformer compositePageTransformer= new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        binding.ViewpagerSlider.setPageTransformer(compositePageTransformer);
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
            if (name==null)
            {

            }
            else {
                txttendaidien.setVisibility(View.VISIBLE);
            }
         txttendaidien.setText(name);
         txtemail.setText(email);
        Glide.with(this)
                .load(uri)
                .error(R.drawable.avar_img)
                .into(imgavartar);

    }






    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id=menuItem.getItemId();
        if(id==R.id.nav_home)
        {

        } else if (id==R.id.nav_favorite) {

        } else if (id==R.id.nav_changeaddressandphone) {
            Intent intent=new Intent(MainActivity.this, changePhoneAndAdressActivity.class);
            startActivity(intent);
            finishAffinity();




        } else if (id==R.id.navchangeProfile) {
           Intent intent=new Intent(MainActivity.this, changeprof.class);
           startActivity(intent);

        } else if (id==R.id.nav_changePassword) {
            Intent intent=new Intent(MainActivity.this,ChangePasswordActiviti.class);
            startActivity(intent);

        } else if (id==R.id.nav_Singout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent=new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }


}