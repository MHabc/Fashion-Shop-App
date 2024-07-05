package com.example.fashionapp;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fashionapp.Adapter.CardAdapter;

import com.example.fashionapp.Fragment.GioHangFragmentbottomsheet;
import com.example.fashionapp.Helper.ChangeNumberItemsListener;
import com.example.fashionapp.Helper.ManagmentCart;
import com.example.fashionapp.databinding.ActivityCartBinding;

public class CartActivity extends BaseActivity {
  ActivityCartBinding binding;
   public double tax;
   private ManagmentCart managmentCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding=ActivityCartBinding.inflate(getLayoutInflater());
        managmentCart=new ManagmentCart(this);
        CaculatorCard();
        setVarible();
        initCardList();
        setContentView(binding.getRoot());
        binding.checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GioHangFragmentbottomsheet bottomSheet = GioHangFragmentbottomsheet.newInstance(/* Các tham số cần thiết nếu có */);
                bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
            }
        });
    }

    private void initCardList() {
        if(managmentCart.getListCart().isEmpty())
        {
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scrollviewCard.setVisibility(View.GONE);
        }
        else {
               binding.emptyTxt.setVisibility(View.GONE);
               binding.scrollviewCard.setVisibility(View.VISIBLE);
        }
        binding.cardView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
       binding.cardView.setAdapter(new CardAdapter(managmentCart.getListCart(), this, new ChangeNumberItemsListener() {
           @Override
           public void changed() {
               CaculatorCard();
           }
       }));
    }

    private void setVarible() {
        binding.cardback.setOnClickListener(view -> finish());
    }

    private void CaculatorCard() {
        double percentTax=0.02;
        double delevery=10;
        tax=Math.round((managmentCart.getTotalFee()*percentTax*100.0))/100.0;
        double total= (double) Math.round((managmentCart.getTotalFee() + tax + delevery) * 100) /100;
        double itemtotal=Math.round(managmentCart.getTotalFee()*100/100);
        binding.totalfree.setText("$"+itemtotal);
        binding.tabtxt.setText("$"+tax);
        binding.delevery.setText("$"+delevery);
        binding.totaltxt.setText("$"+total);
    }
}