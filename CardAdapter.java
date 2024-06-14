package com.example.fashionapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.fashionapp.Domain.ItemDomain;
import com.example.fashionapp.Helper.ChangeNumberItemsListener;
import com.example.fashionapp.Helper.ManagmentCart;
import com.example.fashionapp.databinding.ViewhodelCartBinding;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.Viewhodel> {
    ArrayList<ItemDomain> listitemselected;
    ChangeNumberItemsListener changeNumberItemsListener;
    private ManagmentCart managmentCart;

    public CardAdapter(ArrayList<ItemDomain> items, Context context, ChangeNumberItemsListener changeNumberItemsListener) {
        this.listitemselected = items;
        this.changeNumberItemsListener = changeNumberItemsListener;
        managmentCart=new ManagmentCart(context);

    }

    @NonNull
    @Override
    public CardAdapter.Viewhodel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewhodelCartBinding binding=ViewhodelCartBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new Viewhodel(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CardAdapter.Viewhodel holder, int position) {
    holder.binding.title3.setText(listitemselected.get(position).getTitle());
    holder.binding.freeitem.setText("$"+listitemselected.get(position).getSize()+listitemselected.get(position).getPrice());
    holder.binding.totalitem.setText("$"+Math.round(listitemselected.get(position).getNumberinCard()*listitemselected.get(position).getPrice()));
    holder.binding.numberitem.setText(String.valueOf(listitemselected.get(position).getNumberinCard()));
        RequestOptions requestOptions=new RequestOptions();
        requestOptions.transform(new CenterCrop());
        Glide.with(holder.itemView.getContext())
                .load(listitemselected.get(position).getPicUrl().get(0))
                .into(holder.binding.pic);
        holder.binding.PlusCardbtn.setOnClickListener(view -> {
            managmentCart.plusItem(listitemselected, position, new ChangeNumberItemsListener() {
                @Override
                public void changed() {
                    notifyDataSetChanged();
                    changeNumberItemsListener.changed();
                }
            });
        });
        holder.binding.minuscardbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                managmentCart.minusItem(listitemselected, position, new ChangeNumberItemsListener() {
                    @Override
                    public void changed() {
                        notifyDataSetChanged();
                        changeNumberItemsListener.changed();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return listitemselected.size();
    }

    public class Viewhodel  extends RecyclerView.ViewHolder{
        ViewhodelCartBinding binding;
        public Viewhodel(ViewhodelCartBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
