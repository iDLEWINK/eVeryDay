package com.mobdeve.s14.group24.everyday;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.File;

public class EditMediaEntryActivity extends AppCompatActivity {

    private ImageView ivImage;
    private VideoView vvImage;
    private TextView tvDate;
    private RadioGroup rgMoods;
    private RadioButton rbMood1;
    private RadioButton rbMood2;
    private RadioButton rbMood3;
    private RadioButton rbMood4;
    private RadioButton rbMood5;
    private EditText etCaption;
    private ImageButton ibRetakePhoto;
    private ImageButton ibEdit;
    private ImageButton ibDelete;

    private DatabaseHelper databaseHelper;
    private CameraHelper cameraHelper;
    private SharedPreferences sp;

    private int id;
    private String date;
    private String imagePath;
    private String oldImagePath;
    private String caption;
    private int mood;

    //Start activity for result for camera
    private ActivityResultLauncher activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        imagePath = cameraHelper.getCurrentPath();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_media_entry);

        ivImage = findViewById(R.id.iv_edit_media_entry_image);
        vvImage = findViewById(R.id.vv_edit_media_entry_image);
        tvDate = findViewById(R.id.tv_edit_media_entry_date);
        rgMoods = findViewById(R.id.rg_moods);
        rbMood1 = findViewById(R.id.rb_edit_mood1);
        rbMood2 = findViewById(R.id.rb_edit_mood2);
        rbMood3 = findViewById(R.id.rb_edit_mood3);
        rbMood4 = findViewById(R.id.rb_edit_mood4);
        rbMood5 = findViewById(R.id.rb_edit_mood5);
        etCaption = findViewById(R.id.et_edit_media_entry_caption);
        ibRetakePhoto = findViewById(R.id.ib_retake_photo);
        ibEdit = findViewById(R.id.ib_submit_edit);
        ibDelete = findViewById(R.id.ib_delete);

        //Loads intent values of selected media entry to screen var values
        Intent intent = getIntent();
        id = intent.getIntExtra(Keys.KEY_ID.name(), -1);
        date = intent.getStringExtra(Keys.KEY_DATE.name());
        imagePath = intent.getStringExtra(Keys.KEY_IMAGE_PATH.name());
        oldImagePath = imagePath;
        caption = intent.getStringExtra(Keys.KEY_CAPTION.name());
        mood = intent.getIntExtra(Keys.KEY_MOOD.name(), 0);

        //Sets intent values
        setValues();

        //Initializes both helpers for database and camera
        databaseHelper = DatabaseHelper.getInstance(this);
        cameraHelper = new CameraHelper(getApplicationContext());
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
/*
        rgMoods.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

            }
        });

        View.OnClickListener unselectMood = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton button = (RadioButton) v;
                if (button.isChecked())
                    rgMoods.clearCheck();
            }
        };

        rbMood1.setOnClickListener(unselectMood);
        rbMood2.setOnClickListener(unselectMood);
        rbMood3.setOnClickListener(unselectMood);
        rbMood4.setOnClickListener(unselectMood);
        rbMood5.setOnClickListener(unselectMood);*/

        ibRetakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditMediaEntryActivity.this);
                builder.setTitle("What do you want to capture?");
                builder.setItems(
                        new String[]{"Take a photo", "Take a moment"},
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0)
                                    activityResultLauncher.launch(cameraHelper.makePhotoIntent());
                                else if (which == 1)
                                    activityResultLauncher.launch(cameraHelper.makeVideoIntent());
                            }
                        }
                );
                builder.show();
            }
        });

        //Sets on click listener for the edit image button
        ibEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editEntry();
            }
        });

        //Sets on click listener for the delete image button
        ibDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEntry();
            }
        });
    }

    private void editEntry() {
        //Prompts initial dialog for confirmation of edit action
        AlertDialog.Builder alert = new AlertDialog.Builder(EditMediaEntryActivity.this);
        alert.setTitle("Save Changes");
        alert.setMessage("Do you want to save your changes for this Entry?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (rgMoods.getCheckedRadioButtonId()) {
                    case R.id.rb_edit_mood1:
                        mood = 1; break;
                    case R.id.rb_edit_mood2:
                        mood = 2; break;
                    case R.id.rb_edit_mood3:
                        mood = 3; break;
                    case R.id.rb_edit_mood4:
                        mood = 4; break;
                    case R.id.rb_edit_mood5:
                        mood = 5; break;
                    case -1:
                        mood = 0; break;
                }

                caption = etCaption.getText().toString().trim();

                ThreadHelper.execute(new Runnable() {
                    @Override
                    public void run() {
                        databaseHelper.updateData(id, imagePath, caption, mood);
                        if (imagePath != oldImagePath)
                            new File(oldImagePath).delete();
                    }
                });

                Intent intent = new Intent(EditMediaEntryActivity.this, ViewMediaEntryActivity.class);
                intent.putExtra(Keys.KEY_ID.name(), id);
                intent.putExtra(Keys.KEY_DATE.name(), date);
                intent.putExtra(Keys.KEY_IMAGE_PATH.name(), imagePath);
                intent.putExtra(Keys.KEY_CAPTION.name(), etCaption.getText().toString().trim());
                intent.putExtra(Keys.KEY_MOOD.name(), mood);
                setResult(Activity.RESULT_OK, intent);

                //Activates dataset modified key and returns intent to view with newly added variables
                sp.edit().putBoolean(Keys.MODIFIED_DATA_SET.name(), true).commit();
                dialog.dismiss();
                finish();
            }
        });
        //Dismiss dialog if confirm is not accepted
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    private void deleteEntry() {
        //Prompts initial dialog for confirmation of delete action
        AlertDialog.Builder alert = new AlertDialog.Builder(EditMediaEntryActivity.this);
        alert.setTitle("Delete Entry");
        alert.setMessage("Do you really want to delete this Entry? This cannot be undone.");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new File(imagePath).delete();

                ThreadHelper.execute(new Runnable() {
                    @Override
                    public void run() {
                        databaseHelper.deleteOneRow(id);
                    }
                });

                Intent intent = new Intent(EditMediaEntryActivity.this, ViewMediaEntryActivity.class);
                intent.putExtra(Keys.KEY_ID.name(), -1);
                setResult(Activity.RESULT_OK, intent);
                sp.edit().putBoolean(Keys.DELETED_DATA_SET.name(), true).commit();
                dialog.dismiss();
                finish();
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    //Sets the activity elements value with the loaded intent values
    private void setValues() {
        tvDate.setText(date);
        etCaption.setText(caption);

        String ext = imagePath.contains(".") ? imagePath.substring(imagePath.lastIndexOf(".")).toLowerCase() : "";

        if (ext.equals(".jpeg") || ext.equals(".jpg")) {
            ivImage.setVisibility(View.VISIBLE);
            ivImage.setImageURI(Uri.parse(imagePath));
            vvImage.setVisibility(View.GONE);
        }
        else {
            vvImage.setVisibility(View.VISIBLE);
            vvImage.setVideoURI(Uri.parse(imagePath));
            vvImage.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                }
            });
            vvImage.start();
            ivImage.setVisibility(View.GONE);
        }

        switch (mood) {
            case 1:
                rbMood1.setChecked(true);
                break;
            case 2:
                rbMood2.setChecked(true);
                break;
            case 3:
                rbMood3.setChecked(true);
                break;
            case 4:
                rbMood4.setChecked(true);
                break;
            case 5:
                rbMood5.setChecked(true);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setValues();
    }
}