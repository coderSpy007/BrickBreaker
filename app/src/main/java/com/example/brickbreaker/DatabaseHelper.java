package com.example.brickbreaker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "game_data.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "game_scores";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_POINTS = "points";
    public static final String COLUMN_LEVEL = "level";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_POINTS + " INTEGER, " +
                COLUMN_LEVEL + " TEXT, " +
                COLUMN_TIMESTAMP + " TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertGameData(int points, String level) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_POINTS, points);
        values.put(COLUMN_LEVEL, level);
        values.put(COLUMN_TIMESTAMP, getTimestamp());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public Cursor getAllGameScores() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMN_POINTS, COLUMN_LEVEL, COLUMN_TIMESTAMP};
        return db.query(TABLE_NAME, projection, null, null, null, null, null);
    }

    private String getTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
    public void resetData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);

        db.close();
    }
}
