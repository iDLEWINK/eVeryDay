package com.mobdeve.s14.group24.everyday;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CameraHelper {

    private String currentPath;
    private Context context;

    public CameraHelper(Context context) {
        this.context = context;
    }

    //Creates and returns an intent for a camera photo capture
    public Intent makePhotoIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //Check if phone has access to a camera system feature
        if (context.getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(context.getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }

            //If image captured is successful, store accordingly
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(
                        context,
                        "com.mobdeve.s14.group24.everyday.fileprovider",
                        photoFile
                );
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                return takePictureIntent;
            }
            else
                return null;
        }
        else
            return null;
    }

    //Creates and returns an intent for a camera video capture
    public Intent makeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        //Check if phone has access to a camera system feature
        if (context.getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            File videoFile = null;
            try {
                videoFile = createVideoFile();
            } catch (IOException ex) {
                Toast.makeText(context.getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }
            //If video captured is successful, store accordingly
            if (videoFile != null) {
                Uri videoURI = FileProvider.getUriForFile(
                        context,
                        "com.mobdeve.s14.group24.everyday.fileprovider",
                        videoFile
                );
                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoURI);
                takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 1);
                return takeVideoIntent;
            }
            else
                return null;
        }
        else
            return null;
    }

    //Creates and return an image file captured with the appropriate details such as date and time
    private File createImageFile() throws IOException {
        //Sets and formats the appropriate date details
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        //Sets and formats captured image's file name
        String imageFileName = "JPEG_" + timeStamp + "_";
        //Retrieve destination storage directory after capturing image
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        currentPath = image.getAbsolutePath();
        return image;
    }

    //Creates and return a video file captured with the appropriate details such as date and time
    private File createVideoFile() throws IOException {
        //Sets and formats the appropriate date details
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        //Sets and formats captured video's file name
        String imageFileName = "MP4_" + timeStamp + "_";
        //Retrieve destination storage directory after capturing video
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".mp4",
                storageDir
        );

        currentPath = image.getAbsolutePath();
        return image;
    }

    //Returns the directory path of where the images and video captures are stored within the phone
    public String getCurrentPath() {
        return currentPath;
    }
}
