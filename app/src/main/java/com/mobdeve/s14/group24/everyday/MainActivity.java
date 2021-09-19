package com.mobdeve.s14.group24.everyday;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvGallery;
    private MediaEntryAdapter mediaEntryAdapter;

    private DatabaseHelper databaseHelper;
    private DataHelper dataHelper;
    private ArrayList<MediaEntry> mediaEntries;
    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;

    private FloatingActionButton fabCamera;
    private FloatingActionButton fabMontage;
    private ImageButton ibSort;
    private ProgressBar pbLoading;
    private TextView tvLoading;
    private TextView tvEmpty;

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

                                //Initializes intent for viewing of media entry
                                Intent intent = new Intent(MainActivity.this, ViewMediaEntryActivity.class);
                                sp.edit().putBoolean(Keys.INSERTED_DATA_SET.name(), true).commit();

                                //If descending, append media entry to the start of the arrayList
                                if(sp.getBoolean(Keys.KEY_DESCENDING.name(), true))
                                    mediaEntries.add(0, mediaEntry);
                                //Else, append to the end of the arrayList
                                else
                                    mediaEntries.add(mediaEntry);

                                //Add the keys to send to intent for viewMediaEntry
                                intent.putExtra(Keys.KEY_ID.name(), mediaEntry.getId());
                                intent.putExtra(Keys.KEY_DATE.name(), mediaEntry.getDate().toStringFull());
                                intent.putExtra(Keys.KEY_IMAGE_PATH.name(), mediaEntry.getImagePath());
                                intent.putExtra(Keys.KEY_CAPTION.name(), mediaEntry.getCaption());
                                intent.putExtra(Keys.KEY_MOOD.name(), mediaEntry.getMood());

                                //Start the activity with the intent for viewMediaEntry
                                startActivity(intent);
                            }
                        });
                    }
                }
            }
    );

    //Initializes necessary objects and loads data on launch
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pbLoading = findViewById(R.id.pb_loading_main);
        tvLoading = findViewById(R.id.tv_loading_main);
        tvEmpty = findViewById(R.id.tv_gallery_empty);

        //Sets loading indicator to visible before the main data and screen are loaded
        pbLoading.setVisibility(View.VISIBLE);
        tvLoading.setVisibility(View.VISIBLE);

        //Initialize Database and Shared Preferences
        databaseHelper = DatabaseHelper.getInstance(this);
        dataHelper = new DataHelper(MainActivity.this);
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        spEditor = sp.edit();

        //Initialize recycler view
        initRecyclerView();

        //Initialize action buttons for camera and montage
        initFabCamera();
        initFabMontage();

        //Calls loadSort method to retrieve saved sort preference
        loadSort();

        //Initialize sort image button
        initIbSort();

        //Initialize notification
        initNotification();

    }

    //Calls updateRecyclerView() method onResume
    @Override
    protected void onResume() {
        super.onResume();

        //Calls updateRecyclerView method to match screen with potential changes
        updateRecyclerView();
    }

    private void initRecyclerView () {
        ThreadHelper.execute(new Runnable() {
            @Override
            public void run() {
                //Retrieves saved sort preference
                boolean sortOrder = sp.getBoolean(Keys.KEY_DESCENDING.name(), true);
                mediaEntries = dataHelper.retrieveData();

                //Ascending
                if(!sortOrder)
                    Collections.reverse(mediaEntries);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.this.rvGallery = findViewById(R.id.rv_activity_main_gallery);
                        MainActivity.this.rvGallery.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                        MainActivity.this.mediaEntryAdapter = new MediaEntryAdapter(mediaEntries);
                        MainActivity.this.rvGallery.setAdapter(mediaEntryAdapter);
                        pbLoading.setVisibility(View.GONE);
                        tvLoading.setVisibility(View.GONE);
                        updateRecyclerView();
                    }
                });
            }
        });
    }

    //Initializes the sort image button
    private void initIbSort () {
        ibSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Executor singleExecutor = Executors.newSingleThreadExecutor();
                singleExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        //Retrieves the saved sort preference
                        boolean sortOrder = sp.getBoolean(Keys.KEY_DESCENDING.name(), true);

                        //If descending
                        if(sortOrder) {
                            //Set image button icon to ascending, and set the keys to false(ascending)
                            ibSort.setImageResource(R.drawable.sort_ascending);
                            spEditor.putBoolean(Keys.KEY_DESCENDING.name(), false);
                        } else {
                            //Set image button icon to descending, and set the keys to true(descending)
                            ibSort.setImageResource(R.drawable.sort_descending);
                            spEditor.putBoolean(Keys.KEY_DESCENDING.name(), true);
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Initialize new ArrayList with the current mediaEntries as data and reverse it
                                ArrayList reversedMedia = new ArrayList(mediaEntries);
                                Collections.reverse(reversedMedia);
                                //Call adapter method setData with the argument reversedMedia to make changes to screen data
                                mediaEntryAdapter.setData(reversedMedia);
                            }
                        });

                        //Save sort value to shared preferences
                        spEditor.apply();
                    }
                });
            }
        });
    }

    //Loads the sort values from shared preferences
    private void loadSort() {
        ibSort = findViewById(R.id.ib_sort);

        //Fetches sort value from shared preferencees
        boolean sortDescending = sp.getBoolean(Keys.KEY_DESCENDING.name(), true);

        //Sets the sort image button icon depending on the sort value found from shared preferences
        if(sortDescending) {
            ibSort.setImageResource(R.drawable.sort_descending);
        } else {
            ibSort.setImageResource(R.drawable.sort_ascending);
        }
    }

    private void initFabCamera () {
        fabCamera = findViewById(R.id.fab_activity_main_camera);

        fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*                if (databaseHelper.getRowByDate(new CustomDate()) != null)
                    Toast.makeText(getApplicationContext(), "You have already made an entry for today", Toast.LENGTH_LONG).show();
                else {
*/

                //Shows prompt for whether picture or photo is to be taken
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Select an Option");
                builder.setItems(
                    new String[]{"Capture a Photo", "Capture a Video"},
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            CameraHelper cameraHelper = new CameraHelper(getApplicationContext());
                            if (which == 0) {
                                //Launch camera intent for photo
                                activityResultLauncher.launch(cameraHelper.makePhotoIntent());
                                currentPhotoPath = cameraHelper.getCurrentPath();
                            }
                            else if (which == 1) {
                                //Launch camera intent for video
                                activityResultLauncher.launch(cameraHelper.makeVideoIntent());
                                currentPhotoPath = cameraHelper.getCurrentPath();
                            }
                        }
                    }
                );
                builder.show();
            }
        });
    }

    //Initializes action button for montage functionality
    private void initFabMontage () {
        fabMontage = findViewById(R.id.fab_activity_main_montage);

        fabMontage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Launch intent on montage settings screen on click
                Intent intent = new Intent(MainActivity.this, MontageSettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initNotification () {

        Calendar calendar = Calendar.getInstance();

        //Fetches a random hour in the day given the calendar
        Random random = new Random();
        calendar.set(Calendar.HOUR_OF_DAY, random.nextInt(24));

        //Creates intent for notification
        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }


    private void updateRecyclerView () {
        if (mediaEntries == null)
            return;

        //Position of last accessed media entry
        int lastClickedPosition = sp.getInt(Keys.CUR_DATA_SET_POS.name(), 0);

        //Update if after modifying contents of a media entry
        if (sp.getBoolean(Keys.MODIFIED_DATA_SET.name(), false) && mediaEntryAdapter != null) {
            ThreadHelper.execute(new Runnable() {
                @Override
                public void run() {
                    mediaEntries.set(
                            lastClickedPosition,
                            databaseHelper.getRowById(mediaEntries.get(lastClickedPosition).getId())
                    );
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mediaEntryAdapter.notifyItemChanged(lastClickedPosition);
                        }
                    });
                }
            });
        }

        //Update if after creating new media entry
        if (sp.getBoolean(Keys.INSERTED_DATA_SET.name(), false) && mediaEntryAdapter != null){
            //Inserted data set will be notified at index 0 if descending and the last element of the ArrayList if ascending
            int index = sp.getBoolean(Keys.KEY_DESCENDING.name(), true) ? 0 : mediaEntries.size();
            mediaEntryAdapter.notifyItemInserted(index);
        }

        //Update if after deleting a media entry
        if (sp.getBoolean(Keys.DELETED_DATA_SET.name(), false) && mediaEntryAdapter != null) {
            //Removes the last accessed mediaEntry from the ArrayList
            mediaEntries.remove(lastClickedPosition);
            mediaEntryAdapter.notifyItemRemoved(lastClickedPosition);
        }

        sp.edit()
                .putBoolean(Keys.MODIFIED_DATA_SET.name(), false)
                .putBoolean(Keys.INSERTED_DATA_SET.name(), false)
                .putBoolean(Keys.DELETED_DATA_SET.name(), false)
                .commit();

        if (mediaEntries.size() == 0)
            tvEmpty.setVisibility(View.VISIBLE);
        else
            tvEmpty.setVisibility(View.GONE);
    }

}
