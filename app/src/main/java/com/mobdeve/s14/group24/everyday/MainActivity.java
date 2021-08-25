package com.mobdeve.s14.group24.everyday;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvGallery;
    private RecyclerView.Adapter mediaEntryAdapter;

    DatabaseHelper databaseHelper;
    private ArrayList<MediaEntry> mediaEntries;

    private FloatingActionButton fabCamera;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRecyclerView();
        initFabCamera();
    }

    private void initRecyclerView () {
        DataHelper dataHelper = new DataHelper(MainActivity.this);
        mediaEntries = dataHelper.retrieveData();

        this.rvGallery = findViewById(R.id.rv_activity_main_gallery);
        this.rvGallery.setLayoutManager(new GridLayoutManager(this, 3));
        this.mediaEntryAdapter = new MediaEntryAdapter(mediaEntries);
        this.rvGallery.setAdapter(mediaEntryAdapter);
    }

    private void initFabCamera () {
        fabCamera = findViewById(R.id.fab_activity_main_camera);
        fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                } catch (ActivityNotFoundException e) {
                    // display error state to the user
                    ;
                }
            }
        });
    }
}