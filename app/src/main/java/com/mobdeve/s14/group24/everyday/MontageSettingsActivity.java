package com.mobdeve.s14.group24.everyday;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MontageSettingsActivity extends AppCompatActivity {

    EditText etLength;
    EditText etStartDate;
    EditText etEndDate;
    Button btnCreate;
    ProgressBar pbLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_montage_settings);

        initComponents();
    }

    private void initComponents() {
        etLength = findViewById(R.id.et_montage_length);
        etStartDate = findViewById(R.id.et_montage_start_date);
        etEndDate = findViewById(R.id.et_montage_end_date);
        btnCreate = findViewById(R.id.btn_montage_create);
        pbLoading = findViewById(R.id.pb_montage_loading);

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
}