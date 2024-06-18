package com.example.merisuraksha.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.merisuraksha.Adapter.NewsAdapter;
import com.example.merisuraksha.Domain.Articles;
import com.example.merisuraksha.Domain.POJO;
import com.example.merisuraksha.R;
import com.example.merisuraksha.Utils.ApiUtilies;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HealthNews extends AppCompatActivity {
    ArrayList<Articles> articlesArrayList ;
    private RecyclerView recyclerViewofhealth;
    NewsAdapter newsAdapter;
    final String API_KEY = "69fe837d5d0b4ef198a5bc1e05a29e70";
    String country = "in";
    private String category = "health";
    ShimmerFrameLayout shimmerFrameLayout ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_news);
        recyclerViewofhealth = findViewById(R.id.recycleofenter);
        articlesArrayList = new ArrayList<>();
        shimmerFrameLayout =findViewById(R.id.shimmer);
        shimmerFrameLayout.startShimmer();
        recyclerViewofhealth.setLayoutManager(new LinearLayoutManager(HealthNews.this));
        newsAdapter = new NewsAdapter(HealthNews.this,articlesArrayList);
        recyclerViewofhealth.setAdapter(newsAdapter);
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                findNews();
            }
        };
        handler.postDelayed(runnable,1000);

    }
    private void findNews() {

        ApiUtilies.getApiInterface().getCateNews(country,category,100,API_KEY).enqueue(new Callback<POJO>() {
            @Override
            public void onResponse(Call<POJO> call, Response<POJO> response) {
                if(response.isSuccessful()){
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    recyclerViewofhealth.setVisibility(View.VISIBLE);
                    articlesArrayList.addAll(response.body().getArticles());
                    newsAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<POJO> call, Throwable t) {

            }
        });




    }
}