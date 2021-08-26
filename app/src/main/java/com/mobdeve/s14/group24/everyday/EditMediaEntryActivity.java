package com.mobdeve.s14.group24.everyday;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.File;

public class EditMediaEntryActivity extends AppCompatActivity {

    private ImageView ivImage;
    private TextView tvDate;
    private RadioButton rbMood1;
    private RadioButton rbMood2;
    private RadioButton rbMood3;
    private RadioButton rbMood4;
    private RadioButton rbMood5;
    private EditText etCaption;
    private ImageButton ibRetakePhoto;
    private ImageButton ibEdit;
    private ImageButton ibDelete;

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

        // TODO implement moods bci have no idea how radio buttons work
        ivImage = findViewById(R.id.iv_edit_media_entry_image);
        tvDate = findViewById(R.id.tv_edit_media_entry_date);
        rbMood1 = findViewById(R.id.rb_edit_mood1);
        rbMood2 = findViewById(R.id.rb_edit_mood2);
        rbMood3 = findViewById(R.id.rb_edit_mood3);
        rbMood4 = findViewById(R.id.rb_edit_mood4);
        rbMood5 = findViewById(R.id.rb_edit_mood5);
        etCaption = findViewById(R.id.et_edit_media_entry_caption);
        ibRetakePhoto = findViewById(R.id.ib_retake_photo);
        ibEdit = findViewById(R.id.ib_submit_edit);
        ibDelete = findViewById(R.id.ib_delete);

        Intent intent = getIntent();
        id = intent.getIntExtra(Keys.KEY_ID.name(), -1);
        date = intent.getStringExtra(Keys.KEY_DATE.name());
        imagePath = intent.getStringExtra(Keys.KEY_IMAGE_PATH.name());
        caption = intent.getStringExtra(Keys.KEY_CAPTION.name());
        mood = intent.getIntExtra(Keys.KEY_MOOD.name(), 0);

        ivImage.setImageURI(Uri.parse(imagePath));
        tvDate.setText(date);
        etCaption.setText(caption);
        switch (mood) {
            case 1:
                rbMood1.setSelected(true);
                break;
            case 2:
                rbMood2.setSelected(true);
                break;
            case 3:
                rbMood3.setSelected(true);
                break;
            case 4:
                rbMood4.setSelected(true);
                break;
            case 5:
                rbMood5.setSelected(true);
                break;
        }
        
        dbh = new DatabaseHelper(this);
        ibRetakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraHelper cameraHelper = new CameraHelper(getApplicationContext());
                startActivityForResult(cameraHelper.makeIntent(), CameraHelper.REQUEST_IMAGE_CAPTURE);
                new File(imagePath).delete();
                imagePath = cameraHelper.getCurrentPhotoPath();
            }
        });
        
        ibEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rbMood1.isSelected())
                dbh.updateData(
                        id,
                        imagePath,
                        etCaption.getText().toString().trim(),
                        mood /*idk how radio buttons work*/
                );
                Intent intent = new Intent(EditMediaEntryActivity.this, ViewMediaEntryActivity.class);
                intent.putExtra(Keys.KEY_ID.name(), id);
                intent.putExtra(Keys.KEY_DATE.name(), date);
                intent.putExtra(Keys.KEY_IMAGE_PATH.name(), imagePath);
                intent.putExtra(Keys.KEY_CAPTION.name(), etCaption.getText().toString().trim());
                intent.putExtra(Keys.KEY_MOOD.name(), mood);
                startActivity(intent);
                finish();
            }
        });

        ibDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbh.deleteOneRow(String.valueOf(id));
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ivImage.setImageURI(Uri.parse(imagePath));
    }

}