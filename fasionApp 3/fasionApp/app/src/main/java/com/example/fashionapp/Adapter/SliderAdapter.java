package com.example.fashionapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.fashionapp.Domain.SliderItem;
import com.example.fashionapp.R;

import java.util.ArrayList;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewhodel> {
    private ArrayList<SliderItem> sliderItems;
    private ViewPager2 viewPager2;
    private Context context;
    private Runnable runnable=new Runnable() {
        @Override
        public void run() {
            sliderItems.addAll(sliderItems);
            notifyDataSetChanged();
        }
    };

    public SliderAdapter(ArrayList<SliderItem> sliderItems, ViewPager2 viewPager2) {
        this.sliderItems = sliderItems;
        this.viewPager2 = viewPager2;
    }


    @NonNull
    @Override
    public SliderAdapter.SliderViewhodel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context= parent.getContext();
        return new SliderViewhodel(LayoutInflater.from(context).inflate(R.layout.slide_item_container,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SliderAdapter.SliderViewhodel holder, int position) {
    holder.setImage(sliderItems.get(position));
    if(position==sliderItems.size()-2)
    {
        viewPager2.post(runnable);
    }
    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }

    public class SliderViewhodel extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public SliderViewhodel(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageSlide);
        }
        void setImage(SliderItem sliderItem)
        {
            RequestOptions requestOptions=new RequestOptions();
            requestOptions=requestOptions.transform(new CenterCrop());
            Glide.with(context)
                    .load(sliderItem.getUrl())
                    .apply(requestOptions)
                    .into(imageView);
        }
    }
}
