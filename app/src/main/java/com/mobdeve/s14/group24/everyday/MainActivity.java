package com.mobdeve.s14.group24.everyday;

import androidx.annotation.Nullable;
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
import android.util.Log;
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

    private DatabaseHelper databaseHelper;
    private DataHelper dataHelper;
    private ArrayList<MediaEntry> mediaEntries;

    private FloatingActionButton fabCamera;
    private FloatingActionButton fabMontage;
    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);

        dataHelper = new DataHelper(MainActivity.this);

        initRecyclerView();
        initFabCamera();
        initFabMontage();
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
                if (databaseHelper.getRowByDate(new CustomDate()) != null)
                    Toast.makeText(getApplicationContext(), "You have already made an entry for today", Toast.LENGTH_LONG).show();
                else {
                    CameraHelper cameraHelper = new CameraHelper(getApplicationContext());
                    startActivityForResult(cameraHelper.makeIntent(), CameraHelper.REQUEST_IMAGE_CAPTURE);
                    currentPhotoPath = cameraHelper.getCurrentPhotoPath();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CameraHelper.REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            MediaEntry mediaEntry = databaseHelper.addEntry(currentPhotoPath);

            Intent intent = new Intent(MainActivity.this, ViewMediaEntryActivity.class);

            intent.putExtra(Keys.KEY_ID.name(), mediaEntry.getId());
            intent.putExtra(Keys.KEY_DATE.name(), mediaEntry.getDate().toStringFull());
            intent.putExtra(Keys.KEY_IMAGE_PATH.name(), mediaEntry.getImagePath());
            intent.putExtra(Keys.KEY_CAPTION.name(), mediaEntry.getCaption());
            intent.putExtra(Keys.KEY_MOOD.name(), mediaEntry.getMood());

            startActivity(intent);
        }
    }

    private void initFabMontage () {
        fabMontage = findViewById(R.id.fab_activity_main_montage);

        fabMontage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MontageSettingsActivity.class);
                startActivity(intent);
            }
        });
    }
}
