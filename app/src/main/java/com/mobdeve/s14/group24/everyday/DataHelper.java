package com.mobdeve.s14.group24.everyday;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class DataHelper {

    private Context context;
    private SharedPreferences sp;
    private DatabaseHelper databaseHelper;
    private static final String INIT_TABLE = "initializedTable";

    public DataHelper (Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context);
        sp = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        if (!sp.getBoolean(INIT_TABLE, false))
            initializeData();
    }

    private void initializeData() {
        CustomDate dates[] = {new CustomDate("2021/08/22"), new CustomDate("2021/08/23"), new CustomDate("2021/08/24")};
        String captions[] = {"Default Photo 1 \nCool mountain trail", "Default Photo 2 \nEna Photo", "Default Photo 3 \n Chiaki Nanami"};
        Drawable drawables[] = {context.getDrawable(R.drawable.sample), context.getDrawable(R.drawable.sample1), context.getDrawable(R.drawable.sample2)};
        int moods[] = {3, 4, 5};

        File location = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

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
            databaseHelper.addEntry(dates[i], image.getAbsolutePath(), captions[i], moods[i]);
        }
        sp.edit().putBoolean(INIT_TABLE, true).commit();
    }

//    public void resetData() {
//        context.deleteDatabase(DatabaseHelper.DATABASE_NAME);
//    }

    public ArrayList<MediaEntry> retrieveData () {
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

        return mediaEntries;
    }

}
