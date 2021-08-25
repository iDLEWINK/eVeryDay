package com.mobdeve.s14.group24.everyday;

import android.net.Uri;

public class MediaEntry {
    private Date date;
    private Uri imagePath;
    private String caption;
    private int moodRating;

    public MediaEntry (Date date, Uri imagePath, String caption, int moodRating) {
        this.date = date;
        this.imagePath = imagePath;
        this.caption = caption;
        this.moodRating = moodRating;
    }

    public Date getDate() {
        return date;
    }

    public Uri getImagePath() {
        return imagePath;
    }

    public String getCaption() {
        return caption;
    }

    public int getMoodRating() {
        return moodRating;
    }
}
