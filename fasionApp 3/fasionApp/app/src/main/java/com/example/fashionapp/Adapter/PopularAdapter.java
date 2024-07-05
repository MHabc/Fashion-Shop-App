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
import com.example.fashionapp.DetailActiviti;
import com.example.fashionapp.Domain.ItemDomain;
import com.example.fashionapp.databinding.ViewhodelCategoryBinding;
import com.example.fashionapp.databinding.ViewhodelPopularBinding;

import java.util.ArrayList;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.Viewhodel> {
    ArrayList<ItemDomain> itemDomains;
    Context context;

    public PopularAdapter(ArrayList<ItemDomain> itemDomains)
    {
        this.itemDomains=itemDomains;
    }
    @NonNull
    @Override
    public PopularAdapter.Viewhodel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context= parent.getContext();
       ViewhodelPopularBinding binding= ViewhodelPopularBinding.inflate(LayoutInflater.from(context),parent,false);
        return new Viewhodel(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularAdapter.Viewhodel holder, int position) {

     holder.binding.title2.setText(itemDomains.get(position).getTitle());
     holder.binding.reviewtxt.setText(""+itemDomains.get(position).getReview());
     holder.binding.pricetxt.setText("$"+itemDomains.get(position).getPrice());
     holder.binding.ratingBartxt.setText("("+itemDomains.get(position).getRating()+")");
     holder.binding.oldpricetxt.setText("$"+itemDomains.get(position).getOldPrice());
     holder.binding.ratingBar.setRating((float) itemDomains.get(position).getRating());
        RequestOptions requestOptions =new RequestOptions();
        requestOptions=requestOptions.transform(new CenterCrop());
        Glide.with(context)
                .load(itemDomains.get(position).getPicUrl().get(0))
                .apply(requestOptions)
                .into(holder.binding.pic);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(context, DetailActiviti.class);
                intent.putExtra("object",itemDomains.get(position));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemDomains.size();
    }

    public class Viewhodel extends RecyclerView.ViewHolder {
        ViewhodelPopularBinding binding;

        public Viewhodel(ViewhodelPopularBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
