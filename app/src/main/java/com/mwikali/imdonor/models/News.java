package com.mwikali.imdonor.models;

public class News {
    public String id, title, date, source, image, url;

    public News() {
    }

    public News(String id, String title, String date, String source, String image, String url) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.source = source;
        this.image = image;
        this.url = url;
    }
}
