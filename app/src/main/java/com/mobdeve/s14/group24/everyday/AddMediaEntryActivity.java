package com.mobdeve.s14.group24.everyday;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

public class AddMediaEntryActivity extends AppCompatActivity {

    //mood
    EditText etCaption;
    Button addMediaEntry;

    DatabaseHelper dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_media_entry);

        //initialize mood
        //etCaption = findViewById();
        //addMediaEntry = findViewById();

        dbh = new DatabaseHelper(this);

        addMediaEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbh.addEntry(
                        new Date(),
                        dbh.getRow(1).getImagePath(),
                        etCaption.getText().toString().trim(),
                        0 //add mood somehow
                );
            }
        });
    }
}