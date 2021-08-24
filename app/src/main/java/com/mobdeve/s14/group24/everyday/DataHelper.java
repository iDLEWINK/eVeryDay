package com.mobdeve.s14.group24.everyday;

import java.util.ArrayList;

public class DataHelper {
    public static ArrayList<MediaEntry> initializeData() {

        ArrayList<MediaEntry> data = new ArrayList<>();
        data.add(new MediaEntry(
                R.drawable.sample,
                R.drawable.mood_border_1,
                ""));
        data.add(new MediaEntry(
                R.drawable.sample,
                R.drawable.mood_border_1,
                ""));
        data.add(new MediaEntry(
                R.drawable.sample,
                R.drawable.mood_border_1,
                ""));
        data.add(new MediaEntry(
                R.drawable.sample,
                R.drawable.mood_border_1,
                ""));
        data.add(new MediaEntry(
                R.drawable.sample,
                R.drawable.mood_border_1,
                ""));

        return data;
    }
}
