package com.mobdeve.s14.group24.everyday;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewMediaEntryActivity extends AppCompatActivity {

    private TextView tvDate;
    private ImageView ivImage;
    private TextView tvCaption;
    private TextView tvMood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_media_entry);

        tvDate = findViewById(R.id.tv_view_media_entry_date);
        ivImage = findViewById(R.id.iv_view_media_entry_image);
        tvCaption = findViewById(R.id.tv_view_media_entry_caption);
        tvMood = findViewById(R.id.tv_view_media_entry_mood);

        Intent intent = getIntent();
        String date = intent.getStringExtra(Keys.KEY_DATE.name());
        String imagePath = intent.getStringExtra(Keys.KEY_IMAGE_PATH.name());
        String caption = intent.getStringExtra(Keys.KEY_CAPTION.name());
        int mood = intent.getIntExtra(Keys.KEY_MOOD.name(), 0);

        tvDate.setText(date);
        ivImage.setImageURI(Uri.parse(imagePath));
        tvCaption.setText(caption);
        tvMood.setText("" + mood);
    }
}