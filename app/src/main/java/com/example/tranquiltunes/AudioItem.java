package com.example.tranquiltunes;

public class AudioItem {
    private String name;
    private String url;

    public AudioItem(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
