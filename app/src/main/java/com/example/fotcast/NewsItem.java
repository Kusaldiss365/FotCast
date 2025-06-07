package com.example.fotcast;

public class NewsItem {
    private String title;
    private String category;
    private String content;
    private String key;

    public NewsItem() {} // Required for Firebase

    public NewsItem(String title, String category, String content) {
        this.title = title;
        this.category = category;
        this.content = content;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getKey() { return key; }        // <-- Getter
    public void setKey(String key) { this.key = key; }  // <-- Setter

    private boolean liked; // add this field

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }
}
