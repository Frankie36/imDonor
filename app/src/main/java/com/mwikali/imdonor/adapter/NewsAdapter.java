package com.mwikali.imdonor.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mwikali.imdonor.R;
import com.mwikali.imdonor.activity.NewsDetailActivity;
import com.mwikali.imdonor.models.News;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private Context context;
    private List<News> newsList;

    public NewsAdapter(Context context, List<News> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_feed, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        News news = newsList.get(position);
        holder.tvPublisherName.setText(news.source);
        holder.tvDate.setText(news.date);
        holder.tvNewsTitle.setText(news.title);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, NewsDetailActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgNewsPublisher, imgNews;
        TextView tvPublisherName, tvDate, tvNewsTitle;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgNewsPublisher = itemView.findViewById(R.id.imgNewsPublisher);
            imgNews = itemView.findViewById(R.id.imgNews);
            tvPublisherName = itemView.findViewById(R.id.tvPublisherName);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvNewsTitle = itemView.findViewById(R.id.tvNewsTitle);
        }
    }
}
