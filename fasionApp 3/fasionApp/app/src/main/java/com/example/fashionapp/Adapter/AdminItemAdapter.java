package com.example.fashionapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.fashionapp.AdminActivity;
import com.example.fashionapp.DetailActiviti;
import com.example.fashionapp.Domain.ItemDomain;
import com.example.fashionapp.databinding.ViewhodelPopularBinding;

import java.util.ArrayList;

public class AdminItemAdapter extends RecyclerView.Adapter<AdminItemAdapter.ViewHolder> {
    private ArrayList<ItemDomain> itemDomains;
    private Context context;

    public AdminItemAdapter(ArrayList<ItemDomain> itemDomains) {
        this.itemDomains = itemDomains;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewhodelPopularBinding binding = ViewhodelPopularBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemDomain item = itemDomains.get(position);

        holder.binding.title2.setText(item.getTitle());
        holder.binding.reviewtxt.setText(String.valueOf(item.getReview()));
        holder.binding.pricetxt.setText("$" + item.getPrice());
        holder.binding.ratingBartxt.setText("(" + item.getRating() + ")");
        holder.binding.oldpricetxt.setText("$" + item.getOldPrice());
        holder.binding.ratingBar.setRating((float) item.getRating());

        RequestOptions requestOptions = new RequestOptions().transform(new CenterCrop());

        Glide.with(context)
                .load(item.getPicUrl().get(0)) // Assuming picUrl is a list and we load the first image
                .apply(requestOptions)
                .into(holder.binding.pic);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch DetailActivity and pass item data
                Intent intent = new Intent(context, AdminActivity.class);
                intent.putExtra("itemDomain", item);
                context.startActivity(intent);


            }
        });


    }

    @Override
    public int getItemCount() {
        return itemDomains.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ViewhodelPopularBinding binding;

        public ViewHolder(ViewhodelPopularBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
