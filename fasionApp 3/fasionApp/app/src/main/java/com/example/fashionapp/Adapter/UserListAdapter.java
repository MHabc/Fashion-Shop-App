package com.example.fashionapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fashionapp.ChatActivity;
import com.example.fashionapp.R;
import com.example.fashionapp.Users;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {

    private Context context;
    private List<Users> userList;

    public UserListAdapter(Context context, List<Users> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Users user = userList.get(position);

        // Set user data to views
        holder.userEmail.setText(user.getEmail());
        Glide.with(context)
                .load(user.getPhotoUri())
                .error(R.drawable.avar_img)
                .circleCrop()
                .into(holder.userImage);

        // Handle click events
        holder.itemView.setOnClickListener(v -> {
            // Start ChatActivity with selected user
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("userId", user.getUid());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView userImage;
        TextView userEmail;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.user_image);
            userEmail = itemView.findViewById(R.id.user_email);
        }
    }
}
