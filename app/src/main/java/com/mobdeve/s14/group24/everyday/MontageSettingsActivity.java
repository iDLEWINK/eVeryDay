package com.mobdeve.s14.group24.everyday;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
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
                // isSuccessful = makeMontage()
                pbLoading.setVisibility(View.GONE);
                finish();
                if (isSuccessful)
                    Toast.makeText(MontageSettingsActivity.this, "Successfully created montage", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MontageSettingsActivity.this, "Montage creation failed", Toast.LENGTH_SHORT).show();
            }
        });
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