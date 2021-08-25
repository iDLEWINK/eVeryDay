package com.mobdeve.s14.group24.everyday;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class DataHelper {

    Context context;

    public DataHelper (Context context) {
        this.context = context;
        initializeData();
    }

    private void initializeData() {
        Date dates[] = {new Date("2021/08/22"), new Date("2021/08/23"), new Date("2021/08/24")};
        String captions[] = {"sunny today", "cool adventure", "wow! awesome"};
        Drawable drawables[] = {context.getDrawable(R.drawable.sample), context.getDrawable(R.drawable.sample1), context.getDrawable(R.drawable.sample2)};
        int moods[] = {3, 4, 5};

        File location = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        context.deleteDatabase(DatabaseHelper.DATABASE_NAME);
        DatabaseHelper dbh = new DatabaseHelper(context);

        for (int i = 0; i < dates.length; i++) {
            Bitmap bitmap = ((BitmapDrawable) drawables[i]).getBitmap();
            File image = new File(location, dates[i].toStringFileName());
            if (!image.exists()) {
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(image);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
            }
            dbh.addEntry(dates[i], image.getAbsolutePath(), captions[i], moods[i]);
        }

    }

    public ArrayList<MediaEntry> retrieveData () {
        ArrayList<MediaEntry> mediaEntries = new ArrayList<MediaEntry>();
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        Cursor cursor = databaseHelper.readAllData();

        while (cursor.moveToNext()) {
            mediaEntries.add(new MediaEntry(
                    new Date(cursor.getString(1)),
                    Uri.parse(cursor.getString(2)),
                    cursor.getString(3),
                    cursor.getInt(4)
            ));
        }
        return mediaEntries;
    }

}
