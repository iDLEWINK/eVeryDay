package com.mobdeve.s14.group24.everyday;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvGallery;
    private RecyclerView.Adapter mediaEntryAdapter;

    private DatabaseHelper databaseHelper;
    private DataHelper dataHelper;
    private ArrayList<MediaEntry> mediaEntries;
    private SharedPreferences sp;

    private FloatingActionButton fabCamera;
    private FloatingActionButton fabMontage;
    private String currentPhotoPath;

    private ActivityResultLauncher activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        ThreadHelper.execute(new Runnable() {
                            @Override
                            public void run() {
                                MediaEntry mediaEntry = databaseHelper.getRowById(databaseHelper.addEntry(currentPhotoPath));

                                Intent intent = new Intent(MainActivity.this, ViewMediaEntryActivity.class);
                                sp.edit().putBoolean(Keys.INSERTED_DATA_SET.name(), true).commit();
                                mediaEntries.add(0, mediaEntry);

                                intent.putExtra(Keys.KEY_ID.name(), mediaEntry.getId());
                                intent.putExtra(Keys.KEY_DATE.name(), mediaEntry.getDate().toStringFull());
                                intent.putExtra(Keys.KEY_IMAGE_PATH.name(), mediaEntry.getImagePath());
                                intent.putExtra(Keys.KEY_CAPTION.name(), mediaEntry.getCaption());
                                intent.putExtra(Keys.KEY_MOOD.name(), mediaEntry.getMood());

                                startActivity(intent);
                            }
                        });
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = DatabaseHelper.getInstance(this);
        dataHelper = new DataHelper(MainActivity.this);
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        initRecyclerView();
        initFabCamera();
        initFabMontage();
    }

    @Override
    protected void onResume() {
        super.onResume();

        int lastClickedPosition = sp.getInt(Keys.CUR_DATA_SET_POS.name(), 0);

        if (sp.getBoolean(Keys.MODIFIED_DATA_SET.name(), false)) {
            ThreadHelper.execute(new Runnable() {
                @Override
                public void run() {
                    mediaEntries.set(
                            lastClickedPosition,
                            databaseHelper.getRowById(mediaEntries.get(lastClickedPosition).getId())
                    );
                    mediaEntryAdapter.notifyItemChanged(lastClickedPosition);
                }
            });
        }

        if (sp.getBoolean(Keys.INSERTED_DATA_SET.name(), false))
            mediaEntryAdapter.notifyItemInserted(0);

        if (sp.getBoolean(Keys.DELETED_DATA_SET.name(), false))
            mediaEntryAdapter.notifyItemRemoved(lastClickedPosition);

        sp.edit()
                .putBoolean(Keys.MODIFIED_DATA_SET.name(), false)
                .putBoolean(Keys.INSERTED_DATA_SET.name(), false)
                .putBoolean(Keys.DELETED_DATA_SET.name(), false)
                .commit();
    }

    private void initRecyclerView () {
        mediaEntries = dataHelper.retrieveData();
        this.rvGallery = findViewById(R.id.rv_activity_main_gallery);
        this.rvGallery.setLayoutManager(new GridLayoutManager(this, 2));
        this.mediaEntryAdapter = new MediaEntryAdapter(mediaEntries);
        this.rvGallery.setAdapter(mediaEntryAdapter);
    }

    private void initFabCamera () {
        fabCamera = findViewById(R.id.fab_activity_main_camera);

        fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*                if (databaseHelper.getRowByDate(new CustomDate()) != null)
                    Toast.makeText(getApplicationContext(), "You have already made an entry for today", Toast.LENGTH_LONG).show();
                else {
*/                    CameraHelper cameraHelper = new CameraHelper(getApplicationContext());
                    activityResultLauncher.launch(cameraHelper.makeIntent());
                    currentPhotoPath = cameraHelper.getCurrentPhotoPath();
//                }
            }
        });
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
