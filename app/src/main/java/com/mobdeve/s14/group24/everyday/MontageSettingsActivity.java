package com.mobdeve.s14.group24.everyday;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
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

    private String filePath = null;

    private Handler handler = new Handler(Looper.getMainLooper()) {
        //Handler
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();

            //If an error is detected with the current runnable process
            Log.d("aaa", data.getInt(Keys.PROGRESS_VALUE.name() )+ "");
            if (data.getInt(Keys.PROGRESS_VALUE.name()) == -1) {
                llProgress.setVisibility(View.GONE);
                Toast.makeText(MontageSettingsActivity.this, data.getString(Keys.PROGRESS_MESSAGE.name()), Toast.LENGTH_SHORT).show();
            }

            tvProgress.setText(data.getString(Keys.PROGRESS_MESSAGE.name()));
            pbProgress.setProgress(data.getInt(Keys.PROGRESS_VALUE.name()));

            //If progress reached max or the entire process of creating montage was successful
            if (pbProgress.getMax() == data.getInt(Keys.PROGRESS_VALUE.name())) {
                llProgress.setVisibility(View.GONE);
                Toast.makeText(MontageSettingsActivity.this, "Successfully Created Montage", Toast.LENGTH_SHORT).show();

                //Prompt for accessing and viewing the newly created montage
                AlertDialog.Builder builder = new AlertDialog.Builder(MontageSettingsActivity.this);
                builder.setTitle("Montage Creation Successful");
                builder.setMessage("Do You Want to Watch the Montage?");

                //Launches intent for viewing of the newly created montage
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.parse(filePath), "image/gif");
                        filePath = null;
                        finish();
                        startActivity(intent);
                    }
                });
                //Closes dialog
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                builder.create().show();
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_montage_settings);

        initComponents();
    }

    //Initializes views in the montage settings layout
    private void initComponents() {
        etLength = findViewById(R.id.et_montage_length);
        tvStartDate = findViewById(R.id.et_montage_start_date);
        tvEndDate = findViewById(R.id.et_montage_end_date);
        btnCreate = findViewById(R.id.btn_montage_create);
        pbProgress = findViewById(R.id.pb_montage_progress);
        tvProgress = findViewById(R.id.tv_montage_progress);
        llProgress = findViewById(R.id.ll_montage_progress);

        etLength.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        //On click of start date prompts a date picker dialog
        tvStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStart = true;
                DatePicker pickerDialogFragment;
                pickerDialogFragment = new DatePicker();
                pickerDialogFragment.show(getSupportFragmentManager(), "DATE PICK");
            }
        });

        //On click of end date prompts a date picker dialog
        tvEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStart = false;
                DatePicker pickerDialogFragment;
                pickerDialogFragment = new DatePicker();
                pickerDialogFragment.show(getSupportFragmentManager(), "DATE PICK");
            }
        });

        //On click of create prompts the validation of value and start of creation process of montage
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Validator for empty duration
                if (etLength.getText().toString().trim().isEmpty()) {
                    Toast.makeText(MontageSettingsActivity.this, "Duration must not be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Validator for invalid duration
                else if (Integer.parseInt(etLength.getText().toString().trim()) <= 0) {
                    Toast.makeText(MontageSettingsActivity.this, "Duration must be greater than 0", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Validator for empty start date
                else if (startDate == null) {
                    Toast.makeText(MontageSettingsActivity.this, "Please pick a Start Date", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Validator for empty end date
                else if (endDate == null) {
                    Toast.makeText(MontageSettingsActivity.this, "Please pick an End Date", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (startDate.toString().compareTo(endDate.toString()) > 0) {
                    Toast.makeText(MontageSettingsActivity.this, "Start Date must be greater than End Date", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Sets progress bar to visible
                llProgress.setVisibility(View.VISIBLE);

                ThreadHelper.execute(new Runnable() {
                    @Override
                    public void run() {
                        //If create montage was unsuccessful, pass an error message/value to handler
                        try {
                            createMontage();
                        }
                        catch (Exception e) {
                            Message message = Message.obtain();
                            Bundle bundle = new Bundle();
                            bundle.putString(Keys.PROGRESS_MESSAGE.name(), e.getMessage());
                            bundle.putInt(Keys.PROGRESS_VALUE.name(), -1);
                            handler.sendMessage(message);
                        }
                    }
                });
            }
        });
    }

    //Method for handling the creation of montage process. Returns true if successful and false if unsuccessful.
    public void createMontage() throws Exception {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(this);
        int progress = 0;

        Message message = Message.obtain();

        //Add progress on bundle
        Bundle bundle = new Bundle();
        bundle.putString(Keys.PROGRESS_MESSAGE.name(), "Getting your photos...");
        bundle.putInt(Keys.PROGRESS_VALUE.name(), progress);
        message.setData(bundle);
        handler.sendMessage(message);

        //ArrayList of retrieved mediaEntries within the specified start and end date
        ArrayList<MediaEntry> entriesToUse = databaseHelper.getRowByDateRange(startDate, endDate);
        ArrayList<Bitmap> bitmaps = new ArrayList<>();

        if (entriesToUse.size() == 0) {
            
            throw new Exception("You have no entries in that date range");
        }

        //Sets max of progressBar to fit that of the amount of entries
        pbProgress.setMax(entriesToUse.size() * 2 + 1);

        //Retrieves, processes, and sets the data to be used in compilation in the next step
        for (int i = 0; i < entriesToUse.size(); i++) {
            //Handles messages for each individual entries
            message = Message.obtain();
            bundle = new Bundle();
            bundle.putString(Keys.PROGRESS_MESSAGE.name(), "Processing photo #" + (i + 1) + "...");
            bundle.putInt(Keys.PROGRESS_VALUE.name(), ++progress);
            message.setData(bundle);
            handler.sendMessage(message);
            try {
                //Converts file image to bitmap and add accordingly to bitmaps arraylist
                File file = new File(entriesToUse.get(i).getImagePath());
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file), null, options);
                bitmaps.add(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }

        try {
            message = Message.obtain();
            bundle = new Bundle();
            bundle.putString(Keys.PROGRESS_MESSAGE.name(), "Creating output file...");
            bundle.putInt(Keys.PROGRESS_VALUE.name(), ++progress);
            message.setData(bundle);
            handler.sendMessage(message);

            //Gets path where the montage is to be saved
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
            //Formats timestamp to be added as date detail to the file
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            //Formats the file name of the newly created montage
            String gifFilename = "eVeryDay_Montage_" + timeStamp + ".gif";

            //Sets up writing to device
            File fPath = new File(path);
            File fGifFilename = new File(fPath, gifFilename);
            filePath = fGifFilename.getPath();
            FileOutputStream outStream = new FileOutputStream(filePath);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            //GIF encoder
            AnimatedGIFEncoder encoder = new AnimatedGIFEncoder();
            encoder.setDelay(Float.valueOf(Float.parseFloat(etLength.getText().toString().toString()) * 1000).intValue());
            encoder.start(byteArrayOutputStream);

            for (int i = 0; i < bitmaps.size(); i++) {
                message = Message.obtain();
                bundle = new Bundle();
                bundle.putString(Keys.PROGRESS_MESSAGE.name(), "Encoding photo #" + (i + 1) + "...");
                bundle.putInt(Keys.PROGRESS_VALUE.name(), ++progress);
                message.setData(bundle);
                handler.sendMessage(message);
                //GIFencoder adds processed photo as a frame to the currently processed montage gif file
                encoder.addFrame(bitmaps.get(i));
            }
            encoder.finish();
            byte[] bytes = byteArrayOutputStream.toByteArray();

            outStream.write(bytes);

            //Ends writing to device
            outStream.close();
        } catch(Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //Sets the date of instance variables startDate or endDate depending on the value chosen from the date picker
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
