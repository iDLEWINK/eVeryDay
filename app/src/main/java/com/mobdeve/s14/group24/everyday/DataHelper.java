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

    //Constructor for data helper
    public DataHelper (Context context) {
        this.context = context;
        databaseHelper = DatabaseHelper.getInstance(context);
        sp = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        if (!sp.getBoolean(Keys.INITIALIZED_TABLE.name(), false))
            initializeData();
    }

    //Initializes all sample data
    private void initializeData() {
        CustomDate dates[] = {
                new CustomDate("2021/07/18"),
                new CustomDate("2021/07/19"),
                new CustomDate("2021/07/20"),
                new CustomDate("2021/07/21"),
                new CustomDate("2021/07/22"),
                new CustomDate("2021/07/23"),
                new CustomDate("2021/07/24"),
                new CustomDate("2021/07/25"),
                new CustomDate("2021/07/26"),
        };
        String captions[] = {
                "Making dinner tonight <3\nHope it tastes good!",
                "Discovered a new mountain trail! Life is really full of adventures. So glad today",
                "Had to wait in the car for so long :/\nKind ofbumbed out rn",
                "I have so much work to do today! UGH so annoyed",
                "Having a nice afternoon break with some bread",
                "Swamped at work again, so much to do, so many deadlines. Hope I can make it.",
                "Hanged out with my friend today",
                "Visited my friend in a different city\nWe took this photo in the hotel lobby\nI love the lilghting.",
                "Shoooping day!!\nI love getting new stuff :)\nI have to try these on soon."
        };
        Drawable drawables[] = {
                context.getDrawable(R.drawable.sample_1),
                context.getDrawable(R.drawable.sample_2),
                context.getDrawable(R.drawable.sample_3),
                context.getDrawable(R.drawable.sample_4),
                context.getDrawable(R.drawable.sample_5),
                context.getDrawable(R.drawable.sample_6),
                context.getDrawable(R.drawable.sample_7),
                context.getDrawable(R.drawable.sample_8),
                context.getDrawable(R.drawable.sample_9)
        };
        int moods[] = {4, 5, 2, 1, 3, 1, 4, 4, 5};

        //Retrieves directory of where files are saved
        File location = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        //Writing to device functionality
        for (int i = 0; i < dates.length; i++) {
            Bitmap bitmap = ((BitmapDrawable) drawables[i]).getBitmap();
            //Saves file (video or image) to specified file directory
            File image = new File(location, dates[i].toStringFileName());
            if (!image.exists()) {
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(image);
                    //Compresses image/bitmap
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
            }
            //Save media entries to database
            databaseHelper.addEntry(dates[i], image.getAbsolutePath(), captions[i], moods[i]);
        }
        sp.edit().putBoolean(Keys.INITIALIZED_TABLE.name(), true).commit();
    }

/*    public void resetData() {
        context.deleteDatabase(DatabaseHelper.DATABASE_NAME);
    }
*/
    //Retrieves and returns all data from the database as a media entry arrayList
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
