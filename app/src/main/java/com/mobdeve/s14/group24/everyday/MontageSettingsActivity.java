package com.mobdeve.s14.group24.everyday;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MontageSettingsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private EditText etLength;
    private TextView tvStartDate;
    private TextView tvEndDate;
    private Button btnCreate;
    private ProgressBar pbLoading;
    private boolean isStart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_montage_settings);

        initComponents();
    }

    private void initComponents() {
        etLength = findViewById(R.id.et_montage_length);
        tvStartDate = findViewById(R.id.et_montage_start_date);
        tvEndDate = findViewById(R.id.et_montage_end_date);
        btnCreate = findViewById(R.id.btn_montage_create);
        pbLoading = findViewById(R.id.pb_montage_loading);

        tvStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStart = true;
                DatePicker pickerDialogFragment;
                pickerDialogFragment = new DatePicker();
                pickerDialogFragment.show(getSupportFragmentManager(), "DATE PICK");
            }
        });

        tvEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStart = false;
                DatePicker pickerDialogFragment;
                pickerDialogFragment = new DatePicker();
                pickerDialogFragment.show(getSupportFragmentManager(), "DATE PICK");
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isSuccessful = false;
                pbLoading.setVisibility(View.VISIBLE);

                // Call montage maker, set isSuccessful
//                isSuccessful =
                createMontage();

                pbLoading.setVisibility(View.GONE);
                finish();
//                if (isSuccessful)
//                    Toast.makeText(MontageSettingsActivity.this, "Successfully Created Montage", Toast.LENGTH_SHORT).show();
//                else
//                    Toast.makeText(MontageSettingsActivity.this, "Montage Creation Failed", Toast.LENGTH_SHORT).show();
                Log.d("HELP", "YES CLICK");
            }
        });
    }

    public void createMontage() {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(this);

        BitmapToVideoEncoder bitmapToVideoEncoder = new BitmapToVideoEncoder(new BitmapToVideoEncoder.BitmapToVideoEncoderCallback() {
            @Override
            public void onEncodingComplete(File outputFile) {
                Toast.makeText(getApplicationContext(),  "Encoding complete!", Toast.LENGTH_LONG).show();
            }
        });

        bitmapToVideoEncoder.startEncoding(100, 200, new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera"));

        ArrayList<MediaEntry> mediaEntries = new ArrayList<MediaEntry>();
        Cursor cursor = databaseHelper.readAllData();

        while (cursor.moveToNext()) {
            mediaEntries.add(0,
                    new MediaEntry(cursor.getInt(0),
                            new CustomDate(cursor.getString(1)),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getInt(4)));
        }

        for (int i = 0; i < mediaEntries.size(); i++) {

            try {
                Log.d("PLZ", mediaEntries.get(i).getImagePath());
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(mediaEntries.get(i).getImagePath()));
                bitmapToVideoEncoder.queueFrame(bitmap);
                Log.d("CONVERTING", "" + i);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("HUHU`", "NAG-ERROR");
            }
        }

//        return false;
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String selectedDate = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        if (isStart)
            tvStartDate.setText(selectedDate);
        else
            tvEndDate.setText(selectedDate);
    }
}