package com.vital.santasecret.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vital.santasecret.Model.User;
import com.vital.santasecret.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private final List<User> users;

    public UsersAdapter(Context context, List<User> users){
        this.users = users;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public UsersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.users_list, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersAdapter.ViewHolder holder, int position) {
        User user = users.get(position);
        holder.nameView.setText(user.getDisplayName());
        Glide.with(inflater.getContext()).load(user.getPhotoUrl()).into(holder.photo);

        holder.itemView.setOnClickListener(view -> {
            
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView photo;
        final TextView nameView;
        ViewHolder(View view){
            super(view);
            photo = view.findViewById(R.id.boxLogo);
            nameView = view.findViewById(R.id.nameOfBoxInBoxList);
        }
    }
}