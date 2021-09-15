package com.mobdeve.s14.group24.everyday;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class MediaEntryViewHolder extends RecyclerView.ViewHolder {

    private TextView tvDate;
    private ImageView ivImage;
    private ImageView ivMood;

    public MediaEntryViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);

        ivMood = itemView.findViewById(R.id.iv_item_media_mood);
        tvDate = itemView.findViewById(R.id.tv_item_media_date);
        ivImage = itemView.findViewById(R.id.iv_item_media_image);
    }

    public void setTvDate(String date) {
        tvDate.setText(date);
    }

    public void setIvImage(String imagePath) {
        String ext = imagePath.contains(".") ? imagePath.substring(imagePath.lastIndexOf(".")).toLowerCase() : "";

        if (ext.equals(".jpeg") || ext.equals(".jpg"))
            ivImage.setImageURI(Uri.parse(imagePath));
        else
            ivImage.setImageBitmap(ThumbnailUtils.createVideoThumbnail(imagePath, MediaStore.Video.Thumbnails.MICRO_KIND));
    }

    public void setIvImage(Bitmap image) {
        ivImage.setImageBitmap(image);
    }

    public void setIvMood(int mood) {
        if (mood == 1) {
            ivMood.setImageResource(R.drawable.mood_border_1);
        } else if (mood == 2) {
            ivMood.setImageResource(R.drawable.mood_border_2);
        } else if (mood == 3) {
            ivMood.setImageResource(R.drawable.mood_border_3);
        } else if (mood == 4) {
            ivMood.setImageResource(R.drawable.mood_border_4);
        } else if (mood == 5) {
            ivMood.setImageResource(R.drawable.mood_border_5);
        } else {
            ivMood.setImageResource(R.drawable.mood_border_0);
        }
    }
}



