package com.mobdeve.s14.group24.everyday;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddMediaEntryActivity extends AppCompatActivity {

    //mood
    EditText etCaption;
    Button btnAddMediaEntry;

    DatabaseHelper dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_media_entry);

        //initialize mood
        //etCaption = findViewById();
        //btnAddMediaEntry = findViewById();

        dbh = new DatabaseHelper(this);

        btnAddMediaEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbh.addEntry(dbh.getRowById(1).getImagePath() /*put camera returned path here*/);
            }
        });
    }
}