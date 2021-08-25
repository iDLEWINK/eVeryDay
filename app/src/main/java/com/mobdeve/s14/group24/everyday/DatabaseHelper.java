package com.mobdeve.s14.group24.everyday;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    public static final String DATABASE_NAME = "Entries.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "entries";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_CAPTION = "caption";
    private static final String COLUMN_IMAGE_PATH = "image_path";
    private static final String COLUMN_MOOD = "mood";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DATE + " DATE NOT NULL UNIQUE, " +
                COLUMN_CAPTION + " TEXT DEFAULT '', " +
                COLUMN_IMAGE_PATH + " TEXT UNIQUE, " +
                COLUMN_MOOD + " INTEGER(1)" +
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addEntry(Date date, String imagePath, String caption, int mood){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_DATE, date.toStringDB());
        cv.put(COLUMN_IMAGE_PATH, imagePath);
        cv.put(COLUMN_CAPTION, caption);
        cv.put(COLUMN_MOOD, mood);
    }

    public Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if(db != null)
            cursor = db.rawQuery(query, null);

        return cursor;
    }

    public void updateData(int id, String imagePath, String caption, String mood){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_IMAGE_PATH, imagePath);
        cv.put(COLUMN_CAPTION, caption);
        cv.put(COLUMN_MOOD, mood);
    }

    public void deleteOneRow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "_id=?", new String[]{row_id});
    }

    public void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }

    public ArrayList<MediaEntry> getAllDataAsList() {
        ArrayList<MediaEntry> list = new ArrayList<MediaEntry>();
        Cursor cursor = readAllData();
        while (cursor.moveToNext()) {
            list.add(new MediaEntry(
                    Uri.parse(cursor.getString(3)),
                    cursor.getInt(5),
                    cursor.getString(3)
            ));
        }
    }

}