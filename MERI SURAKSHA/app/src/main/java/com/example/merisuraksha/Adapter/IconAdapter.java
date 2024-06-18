package com.example.merisuraksha.Adapter;

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
import com.example.merisuraksha.Activity.ChatBot;
import com.example.merisuraksha.Activity.HealthNews;
import com.example.merisuraksha.Activity.Magnetometer;
import com.example.merisuraksha.Activity.MagnoInst;
import com.example.merisuraksha.Activity.MapsActivity;
import com.example.merisuraksha.Activity.SirenActivity;
import com.example.merisuraksha.Activity.SmsActivity;
import com.example.merisuraksha.Domain.IconDomain;
import com.example.merisuraksha.R;

import java.util.ArrayList;

public class IconAdapter extends RecyclerView.Adapter<IconAdapter.ViewHolder> {

    ArrayList<IconDomain> iconDomainArrayList;
    Context context;

    public IconAdapter(ArrayList<IconDomain> iconDomainArrayList) {
        this.iconDomainArrayList = iconDomainArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_icon,parent,false);
        context = parent.getContext();
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.icon_name.setText(iconDomainArrayList.get(position).getName() );
        int drawableResourceId = holder.itemView.getResources().getIdentifier(iconDomainArrayList.get(position).getImgpath(), "drawable", holder.itemView.getContext().getPackageName());
        Glide.with(holder.itemView.getContext()).load(drawableResourceId).into(holder.icon);

        holder.itemView.setOnClickListener(v -> {
            // Handle item click here, launch different activities based on the item clicked
            switch (position) {
                case 0:
                    Intent intent = new Intent(context, Magnetometer.class);
                    context.startActivity(intent);
                    break;
                case 1:
                    intent = new Intent(context, MagnoInst.class);
                      context.startActivity(intent);
                    break;
                case 2:
                    intent = new Intent(context, MapsActivity.class);
                    context.startActivity(intent);
                    break;
                case 3:
                    intent = new Intent(context, SmsActivity.class);
                    context.startActivity(intent);
                    break;
                case 4:
                    intent = new Intent(context, SirenActivity.class);
                    context.startActivity(intent);
                    break;
                case 5:
                    intent = new Intent(context, HealthNews.class);
                    context.startActivity(intent);
                    break;
                case 6:
                    intent = new Intent(context, ChatBot.class);
                    context.startActivity(intent);
                    break;
//
//                // Add more cases for additional activities as needed
                default:
                    break;
            }
             //Add more conditions for additional activities as needed
        });

    }

    @Override
    public int getItemCount() {
        return iconDomainArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView icon;
        TextView icon_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon_image);
            icon_name = itemView.findViewById(R.id.icon_Name);
        }
    }
}
