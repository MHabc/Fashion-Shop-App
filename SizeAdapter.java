package com.example.fashionapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fashionapp.R;
import com.example.fashionapp.databinding.ViewhodelSizeBinding;

import java.util.ArrayList;

public class SizeAdapter extends RecyclerView.Adapter<SizeAdapter.Viewhodel> {
    private ArrayList<String> items;
    private Context context;
    private int selectedPosition = -1;
    private OnSizeSelectedListener listener;

    public SizeAdapter(ArrayList<String> items, OnSizeSelectedListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SizeAdapter.Viewhodel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewhodelSizeBinding binding = ViewhodelSizeBinding.inflate(LayoutInflater.from(context), parent, false);
        return new Viewhodel(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SizeAdapter.Viewhodel holder, int position) {
        holder.binding.sizetxt.setText(items.get(position));
        holder.binding.getRoot().setOnClickListener(view -> {
            int lastSelectedPosition = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(lastSelectedPosition);
            notifyItemChanged(selectedPosition);

            if (listener != null) {
                listener.onSizeSelected(items.get(selectedPosition));
            }
        });

        if (selectedPosition == holder.getAdapterPosition()) {
            holder.binding.sizeLayout.setBackgroundResource(R.drawable.size_selected);
            holder.binding.sizetxt.setTextColor(context.getResources().getColor(R.color.green));
        } else {
            holder.binding.sizeLayout.setBackgroundResource(R.drawable.size_unselected);
            holder.binding.sizetxt.setTextColor(context.getResources().getColor(R.color.black));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class Viewhodel extends RecyclerView.ViewHolder {
        ViewhodelSizeBinding binding;

        public Viewhodel(ViewhodelSizeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
