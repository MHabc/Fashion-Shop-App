package com.example.fashionapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fashionapp.Domain.Order;
import com.example.fashionapp.R;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.OrderViewHolder> {

    private List<Order> orderList;

    public HistoryAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.historycart, parent, false);
        return new OrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        // Bind data to views
        holder.txtstatus.setText("trạng thái : "+order.getTimestamp());
        holder.textPhoneNumber.setText("Phone number: " + order.getPhoneNumber());
        holder.textDeliveryAddress.setText("Delivery address: " + order.getDeliveryAddress());
        holder.textTotalFee.setText("Total fee: $" + order.getTotalFee());
        holder.textItems.setText("Items: " + order.getItems());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView textPhoneNumber, textDeliveryAddress, textTotalFee, textItems,txtstatus;

        public OrderViewHolder(View itemView) {
            super(itemView);
            txtstatus=itemView.findViewById(R.id.txtstatus);
            textPhoneNumber = itemView.findViewById(R.id.text_phone_number);
            textDeliveryAddress = itemView.findViewById(R.id.text_delivery_address);
            textTotalFee = itemView.findViewById(R.id.texttotal);
            textItems = itemView.findViewById(R.id.text_items);
        }
    }
}
