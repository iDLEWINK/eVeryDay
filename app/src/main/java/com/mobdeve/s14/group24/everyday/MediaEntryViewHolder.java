package com.mobdeve.s14.group24.everyday;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.net.URI;

public class MediaEntryViewHolder extends RecyclerView.ViewHolder {

    private ImageView ivMoodRating;
    private TextView tvDate;
    private ImageView ivImage;

    public MediaEntryViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);

        ivMoodRating = itemView.findViewById(R.id.iv_item_media_mood_rating);
        tvDate = itemView.findViewById(R.id.tv_item_media_date);
        ivImage = itemView.findViewById(R.id.iv_item_media_image);
    }

    public void setIvMoodRating(int moodRating) {
        ivMoodRating.setImageResource(moodRating);
    }

    public void setTvDate(String date) {
        tvDate.setText(date);
    }

    public void setIvImage(Uri imagePath) {
        ivImage.setImageURI(imagePath);
    }
}



