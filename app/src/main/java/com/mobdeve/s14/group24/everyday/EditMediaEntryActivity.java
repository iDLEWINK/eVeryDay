package com.mobdeve.s14.group24.everyday;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

public class EditMediaEntryActivity extends AppCompatActivity {

    RadioGroup rgMoods;
    EditText etCaption;
    Button btnEditMediaEntry;

    private TextView tvDate;
    private ImageView ivImage;
    private TextView tvCaption;
    private TextView tvMood;
    private ImageButton ibEdit;


    DatabaseHelper dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_media_entry);

        //pass int ID somehow, intents?
        //initialize mood
        //etCaption = findViewById();
        //btnEditMediaEntry = findViewById();

        dbh = new DatabaseHelper(this);

        btnEditMediaEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbh.updateData(
                        0,
                        dbh.getRowById(1).getImagePath(),
                        etCaption.getText().toString().trim(),
                        0 //add mood somehow
                );
            }
        });
    }
}