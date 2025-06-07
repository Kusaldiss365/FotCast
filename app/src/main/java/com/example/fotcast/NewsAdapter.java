package com.example.fotcast;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<NewsItem> newsList;

    public NewsAdapter(List<NewsItem> newsList) {
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsItem item = newsList.get(position);

        holder.titleText.setText(item.getTitle());
        holder.categoryText.setText(item.getCategory());
        holder.contentText.setText(item.getContent());

        // Set heart icon color
        if (item.isLiked()) {
            holder.likeButton.setColorFilter(Color.RED);
        } else {
            holder.likeButton.setColorFilter(Color.GRAY);
        }

        // Handle like toggle
        holder.likeButton.setOnClickListener(v -> {
            boolean liked = !item.isLiked();
            item.setLiked(liked);
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {

        TextView titleText, categoryText, contentText;
        ImageButton likeButton;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.tvTitle);
            categoryText = itemView.findViewById(R.id.tvCategory);
            contentText = itemView.findViewById(R.id.tvContent);
            likeButton = itemView.findViewById(R.id.btnLike);
        }
    }
}
