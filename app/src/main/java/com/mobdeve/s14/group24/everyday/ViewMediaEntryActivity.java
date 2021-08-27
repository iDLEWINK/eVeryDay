package com.mobdeve.s14.group24.everyday;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewMediaEntryActivity extends AppCompatActivity {

    private TextView tvDate;
    private ImageView ivImage;
    private TextView tvCaption;
    private TextView tvMood;
    private ImageView ivMood;
    private ImageButton ibEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_media_entry);

        tvDate = findViewById(R.id.tv_view_media_entry_date);
        ivImage = findViewById(R.id.iv_view_media_entry_image);
        tvCaption = findViewById(R.id.tv_view_media_entry_caption);
        tvMood = findViewById(R.id.tv_view_media_entry_mood);
        ivMood = findViewById(R.id.iv_view_media_entry_mood);
        ibEdit = findViewById(R.id.ib_edit_media_entry);

        Intent intent = getIntent();
        int id = intent.getIntExtra(Keys.KEY_ID.name(), -1);
        String date = intent.getStringExtra(Keys.KEY_DATE.name());
        String imagePath = intent.getStringExtra(Keys.KEY_IMAGE_PATH.name());
        String caption = intent.getStringExtra(Keys.KEY_CAPTION.name());
        int mood = intent.getIntExtra(Keys.KEY_MOOD.name(), 0);

        tvDate.setText(date);
        ivImage.setImageURI(Uri.parse(imagePath));
        tvCaption.setText(caption);

        if (mood < 1 || mood > 5)
            ivMood.setVisibility(View.GONE);
        else
            ivMood.setVisibility(View.VISIBLE);

        switch (mood) {
            case 1:
                ivMood.setImageResource(R.drawable.mood_face_res1);
                tvMood.setText("Bad Mood");
                break;
            case 2:
                ivMood.setImageResource(R.drawable.mood_face_res2);
                tvMood.setText("Poor Mood");
                break;
            case 3:
                ivMood.setImageResource(R.drawable.mood_face_res3);
                tvMood.setText("Okay Mood");
                break;
            case 4:
                ivMood.setImageResource(R.drawable.mood_face_res4);
                tvMood.setText("Good Mood");
                break;
            case 5:
                ivMood.setImageResource(R.drawable.mood_face_res5);
                tvMood.setText("Great Mood");
                break;
            default:
                tvMood.setText("Not yet set");
        }

        ibEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewMediaEntryActivity.this, EditMediaEntryActivity.class);
                intent.putExtra(Keys.KEY_ID.name(), id);
                intent.putExtra(Keys.KEY_DATE.name(), date);
                intent.putExtra(Keys.KEY_IMAGE_PATH.name(), imagePath);
                intent.putExtra(Keys.KEY_CAPTION.name(), caption);
                intent.putExtra(Keys.KEY_MOOD.name(), mood);
                startActivity(intent);
                finish();
            }
        });
    }
}