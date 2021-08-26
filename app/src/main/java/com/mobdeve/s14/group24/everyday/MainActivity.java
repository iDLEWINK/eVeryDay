package com.mobdeve.s14.group24.everyday;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvGallery;
    private RecyclerView.Adapter mediaEntryAdapter;

    private DatabaseHelper dbh;
    private DataHelper dataHelper;
    private ArrayList<MediaEntry> mediaEntries;

    private FloatingActionButton fabCamera;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbh = new DatabaseHelper(this);
        dataHelper = new DataHelper(MainActivity.this);
        dataHelper.resetData();
        dataHelper.initializeData();
        initRecyclerView();
        initFabCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initRecyclerView();
    }

    private void initRecyclerView () {
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

                if (dbh.getRowByDate(new CustomDate()) != null) {
                    Toast.makeText(getApplicationContext(), "You have already made an entry for today", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                CameraHelper cameraHelper = new CameraHelper(getApplicationContext());
                startActivityForResult(cameraHelper.makeIntent(), CameraHelper.REQUEST_IMAGE_CAPTURE);
                dbh.addEntry(cameraHelper.getCurrentPhotoPath());
            }
        });
    }

}