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
    private ImageButton ibEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_media_entry);

        tvDate = findViewById(R.id.tv_view_media_entry_date);
        ivImage = findViewById(R.id.iv_view_media_entry_image);
        tvCaption = findViewById(R.id.tv_view_media_entry_caption);
        tvMood = findViewById(R.id.tv_view_media_entry_mood);
        ibEdit = findViewById(R.id.ib_edit_media_entry);

        Intent intent = getIntent();
        String date = intent.getStringExtra(Keys.KEY_DATE.name());
        String imagePath = intent.getStringExtra(Keys.KEY_IMAGE_PATH.name());
        String caption = intent.getStringExtra(Keys.KEY_CAPTION.name());
        int mood = intent.getIntExtra(Keys.KEY_MOOD.name(), 0);

        tvDate.setText(date);
        ivImage.setImageURI(Uri.parse(imagePath));
        tvCaption.setText(caption);
        tvMood.setText("" + mood);

        ibEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewMediaEntryActivity.this, EditMediaEntryActivity.class);
                intent.putExtra(Keys.KEY_DATE.name(), date);
                intent.putExtra(Keys.KEY_IMAGE_PATH.name(), imagePath);
                intent.putExtra(Keys.KEY_CAPTION.name(), caption);
                intent.putExtra(Keys.KEY_MOOD.name(), mood);
                startActivity(intent);
            }
        });
    }
}