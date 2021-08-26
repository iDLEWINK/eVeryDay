package com.mobdeve.s14.group24.everyday;

public class MediaEntry {
    private CustomDate date;
    private String imagePath;
    private String caption;
    private int mood;

    public MediaEntry (CustomDate date, String imagePath, String caption, int mood) {
        this.date = date;
        this.imagePath = imagePath;
        this.caption = caption;
        this.mood = mood;
    }

    public CustomDate getDate() {
        return date;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getCaption() {
        return caption;
    }

    public int getMood() {
        return mood;
    }
}
