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
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
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

    private DatabaseHelper databaseHelper;
    private CameraHelper cameraHelper;
    private SharedPreferences sp;

    private int id;
    private String date;
    private String imagePath;
    private String caption;
    private int mood;

    private ActivityResultLauncher activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        new File(imagePath).delete();
                        imagePath = cameraHelper.getCurrentPhotoPath();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_media_entry);

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

        databaseHelper = DatabaseHelper.getInstance(this);
        cameraHelper = new CameraHelper(getApplicationContext());
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        ibRetakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityResultLauncher.launch(cameraHelper.makeIntent());
            }
        });
        
        ibEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editEntry();
            }
        });

        ibDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEntry();
            }
        });
    }

    private void editEntry() {
        AlertDialog.Builder alert = new AlertDialog.Builder(EditMediaEntryActivity.this);
        alert.setTitle("Edit Confirmation");
        alert.setMessage("Are you sure you want to edit with these changes?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (rbMood1.isChecked())
                    mood = 1;
                else if (rbMood2.isChecked())
                    mood = 2;
                else if (rbMood3.isChecked())
                    mood = 3;
                else if (rbMood4.isChecked())
                    mood = 4;
                else if (rbMood5.isChecked())
                    mood = 5;

                caption = etCaption.getText().toString().trim();
                databaseHelper.updateData(id, imagePath, caption, mood);
                Intent intent = new Intent(EditMediaEntryActivity.this, ViewMediaEntryActivity.class);
                intent.putExtra(Keys.KEY_ID.name(), id);
                intent.putExtra(Keys.KEY_DATE.name(), date);
                intent.putExtra(Keys.KEY_IMAGE_PATH.name(), imagePath);
                intent.putExtra(Keys.KEY_CAPTION.name(), etCaption.getText().toString().trim());
                intent.putExtra(Keys.KEY_MOOD.name(), mood);
                setResult(Activity.RESULT_OK, intent);

                sp.edit().putBoolean(Keys.MODIFIED_DATA_SET.name(), true).commit();
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

    private void deleteEntry() {
        AlertDialog.Builder alert = new AlertDialog.Builder(EditMediaEntryActivity.this);
        alert.setTitle("Delete");
        alert.setMessage("Are you sure you want to delete?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new File(imagePath).delete();
                databaseHelper.deleteOneRow(id);
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

    @Override
    protected void onResume() {
        super.onResume();
        ivImage.setImageURI(Uri.parse(imagePath));
    }

}