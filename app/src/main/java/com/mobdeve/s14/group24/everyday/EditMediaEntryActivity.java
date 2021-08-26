package com.mobdeve.s14.group24.everyday;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

public class EditMediaEntryActivity extends AppCompatActivity {

    private RadioGroup rgMoods;
    private EditText etCaption;
    private ImageButton ibRetakePhoto;

    private DatabaseHelper dbh;

    private int id;
    private String date;
    private String imagePath;
    private String caption;
    private int mood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_media_entry);

        //pass int ID somehow, intents?
        //initialize mood
        //etCaption = findViewById();
        //btnEditMediaEntry = findViewById();
        ibRetakePhoto = findViewById(R.id.ib_retake_photo);

        Intent intent = getIntent();
        id = intent.getIntExtra(Keys.KEY_ID.name(), -1);
        date = intent.getStringExtra(Keys.KEY_DATE.name());
        imagePath = intent.getStringExtra(Keys.KEY_IMAGE_PATH.name());
        caption = intent.getStringExtra(Keys.KEY_CAPTION.name());
        mood = intent.getIntExtra(Keys.KEY_MOOD.name(), 0);

        dbh = new DatabaseHelper(this);

        ibRetakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraHelper cameraHelper = new CameraHelper(getApplicationContext());
                startActivityForResult(cameraHelper.makeIntent(), CameraHelper.REQUEST_IMAGE_CAPTURE);
                imagePath = cameraHelper.getCurrentPhotoPath();
            }
        });
    }
}