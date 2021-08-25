package com.mobdeve.s14.group24.everyday;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvGallery;
    private RecyclerView.Adapter mediaEntryAdapter;

    DatabaseHelper databaseHelper;
    private ArrayList<MediaEntry> mediaEntries;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRecyclerView();
    }

    public void initRecyclerView () {
        DataHelper dataHelper = new DataHelper(MainActivity.this);

        mediaEntries = dataHelper.retrieveData();
        this.rvGallery = findViewById(R.id.rv_activity_main_gallery);
        this.rvGallery.setLayoutManager(new GridLayoutManager(this, 3));
        this.mediaEntryAdapter = new MediaEntryAdapter(mediaEntries);
        this.rvGallery.setAdapter(mediaEntryAdapter);
    }
}