package com.mobdeve.s14.group24.everyday;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    public static final String DATABASE_NAME = "MediaEntries.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "media_entries";
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
                COLUMN_DATE + " DATE NOT NULL, " +
                COLUMN_IMAGE_PATH + " TEXT UNIQUE, " +
                COLUMN_CAPTION + " TEXT DEFAULT '', " +
                COLUMN_MOOD + " INTEGER(1) DEFAULT 0" +
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public MediaEntry addEntry(CustomDate date, String imagePath, String caption, int mood){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_DATE, date.toStringDB());
        cv.put(COLUMN_IMAGE_PATH, imagePath);
        cv.put(COLUMN_CAPTION, caption);
        cv.put(COLUMN_MOOD, mood);
        long id = db.insert(TABLE_NAME,null, cv);
        return getRowById(id);
    }

    public MediaEntry addEntry(String imagePath){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_DATE, new CustomDate().toStringDB());
        cv.put(COLUMN_IMAGE_PATH, imagePath);
        long id = db.insert(TABLE_NAME,null, cv);
        return getRowById(id);
    }

    public Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null)
            cursor = db.rawQuery(query, null);

        return cursor;
    }

    public MediaEntry getRowById(long id){
        String query = "SELECT * FROM " + TABLE_NAME +
                " WHERE " + COLUMN_ID + " = " + id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        if(db != null)
            cursor = db.rawQuery(query, null);

        if (cursor.getCount() == 0)
            return null;

        return cursorToMediaEntry(cursor);
    }

    public MediaEntry getRowByDate(CustomDate date){
        String query = "SELECT * FROM " + TABLE_NAME +
                " WHERE " + COLUMN_DATE + " = '" + date.toStringDB() + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        if(db != null)
            cursor = db.rawQuery(query, null);

        if (cursor.getCount() == 0)
            return null;

        return cursorToMediaEntry(cursor);
    }

    public void updateData(int id, String imagePath, String caption, int mood){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_IMAGE_PATH, imagePath);
        cv.put(COLUMN_CAPTION, caption);
        cv.put(COLUMN_MOOD, mood);
        Log.d("aaa", id + " " + imagePath + " " + caption + " " + mood);
        Log.d("aaa", db.update(TABLE_NAME, cv, "_id=?", new String[]{Integer.toString(id)}) + "");
    }

    public void deleteOneRow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "_id=?", new String[]{row_id});
    }

    public void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }

    private MediaEntry cursorToMediaEntry(Cursor cursor) {
        cursor.moveToNext();
        return new MediaEntry(
                cursor.getInt(0),
                new CustomDate(cursor.getString(1)),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getInt(4)
        );
    }

}