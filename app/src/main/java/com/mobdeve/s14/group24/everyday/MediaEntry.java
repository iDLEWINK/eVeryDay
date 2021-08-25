package com.mobdeve.s14.group24.everyday;

import android.net.Uri;

public class MediaEntry {
    private Date date;
    private String imagePath;
    private String caption;
    private int mood;

    public MediaEntry (Date date, String imagePath, String caption, int mood) {
        this.date = date;
        this.imagePath = imagePath;
        this.caption = caption;
        this.mood = mood;
    }

    public Date getDate() {
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
