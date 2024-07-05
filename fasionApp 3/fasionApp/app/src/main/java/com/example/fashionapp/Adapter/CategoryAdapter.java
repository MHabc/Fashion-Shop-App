package com.example.fashionapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fashionapp.AdidasActivity;
import com.example.fashionapp.Domain.CategoryItem;
import com.example.fashionapp.NikeActivity;
import com.example.fashionapp.PumaAcitivity;
import com.example.fashionapp.ZaraActivity;
import com.example.fashionapp.databinding.ViewhodelCategoryBinding;


import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.Viewholder>{
   private ArrayList<CategoryItem> items;
   private Context context;
   public CategoryAdapter(ArrayList<CategoryItem> items)
   {
       this.items=items;

   }
    @NonNull
    @Override
    public CategoryAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       context = parent.getContext();
        ViewhodelCategoryBinding binding= ViewhodelCategoryBinding.inflate(LayoutInflater.from(context),parent,false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.Viewholder holder, int position) {
     holder.binding.title1.setText(items.get(position).getTitle());
        Glide.with(context)
                .load(items.get(position).getPicUrl())
                .into(holder.binding.pic1);
        if (position == 0) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, AdidasActivity.class);
                    context.startActivity(intent);
                }
            });
        }
        if (position == 1) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, NikeActivity.class);
                    context.startActivity(intent);
                }
            });
        }
        if (position == 2) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, PumaAcitivity.class);
                    context.startActivity(intent);
                }
            });
        }
        if (position == 3) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, ZaraActivity.class);
                    context.startActivity(intent);
                }
            });
        }
        if (position == 4) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, ZaraActivity.class);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
       ViewhodelCategoryBinding binding;

        public Viewholder(ViewhodelCategoryBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
