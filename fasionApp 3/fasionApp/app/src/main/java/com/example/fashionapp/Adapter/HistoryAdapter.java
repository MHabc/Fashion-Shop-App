package com.example.fashionapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fashionapp.Domain.Order;
import com.example.fashionapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.OrderViewHolder> {

    private final ArrayList<Order> orderList;
    private final Context context;

    public HistoryAdapter(Context context, ArrayList<Order> orderList) {
        this.context = context;
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
        holder.txtstatus.setText("Status: " + order.getTimestamp());
        holder.textPhoneNumber.setText("Phone number: " + order.getPhoneNumber());
        holder.textDeliveryAddress.setText("Delivery address: " + order.getDeliveryAddress());
        holder.textTotalFee.setText("Total fee: $" + order.getTotalFee());
        holder.textItems.setText("Items: " + order.getItems());

        if ("chờ xác nhận".equals(order.getTimestamp())) {
            holder.btncancel.setVisibility(View.VISIBLE);
        } else {
            holder.btncancel.setVisibility(View.GONE);
        }
        holder.btncancel.setOnClickListener(view -> cancelOrder(order, position));
        holder.btnduyet.setVisibility(View.GONE);
        holder.btnduyet.setOnClickListener(view -> approveOrder(order,position));


    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView textPhoneNumber, textDeliveryAddress, textTotalFee, textItems, txtstatus;
        Button btncancel, btnduyet;

        public OrderViewHolder(View itemView) {
            super(itemView);
            btncancel = itemView.findViewById(R.id.btncancel);
            btnduyet = itemView.findViewById(R.id.btnduyet);
            txtstatus = itemView.findViewById(R.id.txtstatus);
            textPhoneNumber = itemView.findViewById(R.id.text_phone_number);
            textDeliveryAddress = itemView.findViewById(R.id.text_delivery_address);
            textTotalFee = itemView.findViewById(R.id.texttotal);
            textItems = itemView.findViewById(R.id.text_items);
        }
    }

    private void cancelOrder(Order order, int position) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference()
                    .child("Orders")
                    .child(userId)
                    .child(order.getTimestamp2()); // Assuming Order class has getTimestamp2() method

            // Remove the order from Firebase
            orderRef.removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Remove the order from the local list and notify the adapter
                    orderList.remove(position);
                    notifyDataSetChanged(); // or notifyItemRemoved(position) if needed
                    Toast.makeText(context, "Order canceled successfully", Toast.LENGTH_SHORT).show();
                } else {
                    // Show error message
                    Toast.makeText(context, "Failed to cancel order", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // User is not authenticated, handle this case if needed
            Toast.makeText(context, "User not authenticated", Toast.LENGTH_SHORT).show();
        }
    }

    private void cancelOrder2(Order order, int position) {
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(order.getUserId())
                .child(order.getTimestamp2());

        orderRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                orderList.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context, "Order canceled successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to cancel order", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void approveOrder(Order order, int position) {
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(order.getUserId())
                .child(order.getTimestamp2());

        order.setTimestamp("đã xác nhận");
        orderRef.setValue(order).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                notifyDataSetChanged();
                Toast.makeText(context, "Order approved successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to approve order", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
