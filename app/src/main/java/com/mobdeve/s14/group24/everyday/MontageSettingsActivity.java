package com.mobdeve.s14.group24.everyday;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MontageSettingsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private EditText etLength;
    private TextView tvStartDate;
    private TextView tvEndDate;
    private Button btnCreate;
    private ProgressBar pbProgress;
    private TextView tvProgress;
    private LinearLayout llProgress;
    private boolean isStart = false;

    private CustomDate startDate = null;
    private CustomDate endDate = null;

    final static String PROGRESS_MESSAGE = "PROGRESS_MESSAGE";
    final static String PROGRESS_VALUE = "PROGRESS_NUMBER";
    final static String ERROR = "ERROR";

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();

            if (data.getString(PROGRESS_MESSAGE) != null && data.getString(PROGRESS_MESSAGE).equals(ERROR)) {
                llProgress.setVisibility(View.GONE);
                Toast.makeText(MontageSettingsActivity.this, "Montage Creation Failed", Toast.LENGTH_SHORT).show();
            }

            tvProgress.setText(data.getString(PROGRESS_MESSAGE));
            pbProgress.setProgress(data.getInt(PROGRESS_VALUE));

            if (pbProgress.getMax() == data.getInt(PROGRESS_VALUE)) {
                llProgress.setVisibility(View.GONE);
                Toast.makeText(MontageSettingsActivity.this, "Successfully Created Montage", Toast.LENGTH_SHORT).show();
            }

        }
    };

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
        pbProgress = findViewById(R.id.pb_montage_progress);
        tvProgress = findViewById(R.id.tv_montage_progress);
        llProgress = findViewById(R.id.ll_montage_progress);

        etLength.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

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
                if (startDate == null) {
                    Toast.makeText(MontageSettingsActivity.this, "Please pick a start date", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (endDate == null) {
                    Toast.makeText(MontageSettingsActivity.this, "Please pick an end date", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (startDate.toStringDB().compareTo(endDate.toStringDB()) >= 0) {
                    Toast.makeText(MontageSettingsActivity.this, "Start date must be greater than end date", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (etLength.getText().toString().trim().isEmpty()) {
                    Toast.makeText(MontageSettingsActivity.this, "Length must not be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (Float.parseFloat(etLength.getText().toString().trim()) <= 0) {
                    Toast.makeText(MontageSettingsActivity.this, "Length must be greater than 0", Toast.LENGTH_SHORT).show();
                    return;
                }

                llProgress.setVisibility(View.VISIBLE);

                ThreadHelper.execute(new Runnable() {
                    @Override
                    public void run() {
                        if (!createMontage()) {
                            Message message = Message.obtain();
                            Bundle bundle = new Bundle();
                            bundle.putString(PROGRESS_MESSAGE, ERROR);
                            bundle.putInt(PROGRESS_VALUE, 0);
                            handler.sendMessage(message);
                        }
                    }
                });
            }
        });
    }

    public boolean createMontage() {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(this);
        int progress = 0;

        Message message = Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putString(PROGRESS_MESSAGE, "Getting your photos...");
        bundle.putInt(PROGRESS_VALUE, progress);
        message.setData(bundle);
        handler.sendMessage(message);

        ArrayList<MediaEntry> entriesToUse = databaseHelper.getRowByDateRange(startDate, endDate);
        ArrayList<Bitmap> bitmaps = new ArrayList<>();

        pbProgress.setMax(entriesToUse.size() * 2 + 1);

        for (int i = 0; i < entriesToUse.size(); i++) {
            message = Message.obtain();
            bundle = new Bundle();
            bundle.putString(PROGRESS_MESSAGE, "Processing photo #" + (i + 1) + "...");
            bundle.putInt(PROGRESS_VALUE, ++progress);
            message.setData(bundle);
            handler.sendMessage(message);
            try {
                File file = new File(entriesToUse.get(i).getImagePath());
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file), null, options);
                bitmaps.add(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            message = Message.obtain();
            bundle = new Bundle();
            bundle.putString(PROGRESS_MESSAGE, "Creating output file...");
            bundle.putInt(PROGRESS_VALUE, ++progress);
            message.setData(bundle);
            handler.sendMessage(message);

            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            String gifFilename = "eVeryDay_Montage_" + timeStamp + ".gif";


            FileOutputStream outStream = new FileOutputStream(Paths.get(path, gifFilename).toString());
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            AnimatedGIFEncoder encoder = new AnimatedGIFEncoder();
            encoder.setDelay(Float.valueOf(Float.parseFloat(etLength.getText().toString().toString()) * 1000).intValue());
            encoder.start(byteArrayOutputStream);

            for (int i = 0; i < bitmaps.size(); i++) {
                message = Message.obtain();
                bundle = new Bundle();
                bundle.putString(PROGRESS_MESSAGE, "Encoding photo #" + (i + 1) + "...");
                bundle.putInt(PROGRESS_VALUE, ++progress);
                message.setData(bundle);
                handler.sendMessage(message);
                encoder.addFrame(bitmaps.get(i));
            }
            encoder.finish();
            byte[] bytes = byteArrayOutputStream.toByteArray();

            outStream.write(bytes);
            outStream.close();
            return true;
        } catch(Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        if (isStart) {
            startDate = new CustomDate(year, month, dayOfMonth);
            tvStartDate.setText(startDate.toStringFull());
        }
        else {
            endDate = new CustomDate(year, month, dayOfMonth);
            tvEndDate.setText(endDate.toStringFull());
        }
    }
}
