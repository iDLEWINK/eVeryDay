package com.mobdeve.s14.group24.everyday;

public class MediaEntry {
    private int imageID;
    private int moodRating;
    private String caption;
//    TODO: Implement Date and Time
//    private Date date;
//    private Time time;

    public MediaEntry (int imageID, int moodRating, String caption) {
        this.imageID = imageID;
        this.moodRating = moodRating;
        this.caption = caption;
//        TODO: Add date and time
    }

    public int getImageID() {
        return imageID;
    }

    public int getMoodRating() {
        return moodRating;
    }

    public String getCaption() {
        return caption;
    }

//    TODO: Getters for date and time
}
