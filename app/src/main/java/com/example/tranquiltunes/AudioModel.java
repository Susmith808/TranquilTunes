package com.example.tranquiltunes;

public class AudioModel {
    private String name;
    private String url;
    private String imageUrl;

    public AudioModel() {
    }

    public AudioModel(String name, String url, String imageUrl) {
        this.name = name;
        this.url = url;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
