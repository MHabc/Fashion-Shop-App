package com.example.fashionapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.example.fashionapp.Domain.ReviewDomain;
import com.example.fashionapp.R;
import com.example.fashionapp.databinding.ViewhodelReviewBinding;


import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.Viewhodel> {
    ArrayList<ReviewDomain> items;
    Context context;

    public ReviewAdapter(ArrayList<ReviewDomain> items) {
        this.items = items;

    }

    @NonNull
    @Override
    public ReviewAdapter.Viewhodel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context= parent.getContext();
        ViewhodelReviewBinding binding=ViewhodelReviewBinding.inflate(LayoutInflater.from(context),parent,false);
        return new Viewhodel(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.Viewhodel holder, int position) {
     holder.binding.nametxt.setText(items.get(position).getName());
     holder.binding.desctxt.setText(items.get(position).getDescription());
     holder.binding.ratingtxt.setText(""+items.get(position).getRating());
        Glide.with(context)
                .load(items.get(position).getPicUrl())
                .error(R.drawable.avar_img).transform(new GranularRoundedCorners(100,100,100,100))
                .into(holder.binding.pic);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Viewhodel extends RecyclerView.ViewHolder{
        ViewhodelReviewBinding binding;
        public Viewhodel(ViewhodelReviewBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
