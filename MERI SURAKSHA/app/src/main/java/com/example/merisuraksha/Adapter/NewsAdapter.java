package com.example.merisuraksha.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.merisuraksha.Activity.webview;
import com.example.merisuraksha.Domain.Articles;
import com.example.merisuraksha.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
       Context context;
       ArrayList<Articles> articlesArrayList;

    public NewsAdapter(Context context, ArrayList<Articles> articlesArrayList) {
        this.context = context;
        this.articlesArrayList = articlesArrayList;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.sample_row,parent,false);
        return new NewsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        Articles articles = articlesArrayList.get(position);
        // logical to time formating
        String dateInString = articles.getPublishedAt();
        SimpleDateFormat formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
        }
        try {
            Date date = formatter.parse(dateInString);
            formatter = new SimpleDateFormat("dd MMM yyyy");
            String strDate = formatter.format(date);
            SimpleDateFormat time = new SimpleDateFormat("h:mm a");

            String strTime = time.format(date);
            String total = strDate + "\n"+strTime;
            holder.publish.setText("Published At:- "+total);
        }catch (ParseException e){
            return ;
        }
        holder.title.setText(articles.getTitle());
        holder.description.setText(articles.getDescription());
        Glide.with(context).load(articles.getUrlToImage()).error(R.drawable.fallback).fallback(R.drawable.fallback).into(holder.image);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, webview.class);
                intent.putExtra("url",articles.getUrl());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return articlesArrayList.size();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder{
        TextView title,description,publish;
        ImageView image;
        CardView cardView;
        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.ititle);
            description = itemView.findViewById(R.id.idescprition);
            publish = itemView.findViewById(R.id.ipublish);
            image = itemView.findViewById(R.id.newsimage);
            cardView = itemView.findViewById(R.id.cardview);
        }
    }
}
