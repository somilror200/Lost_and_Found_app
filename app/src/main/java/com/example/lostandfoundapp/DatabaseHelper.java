package com.example.lostandfoundapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "LostAndFound.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "adverts";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_POST_TYPE = "post_type";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_ADVERTS_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT NOT NULL, "
                + COLUMN_PHONE + " TEXT NOT NULL, "
                + COLUMN_DESCRIPTION + " TEXT NOT NULL, "
                + COLUMN_DATE + " TEXT NOT NULL, "
                + COLUMN_LOCATION + " TEXT NOT NULL, "
                + COLUMN_POST_TYPE + " TEXT NOT NULL)";
        db.execSQL(SQL_CREATE_ADVERTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long getItemId(int position) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {COLUMN_ID};
        Cursor cursor = db.query(TABLE_NAME, projection, null, null, null, null, null);
        cursor.moveToPosition(position);
        long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID));
        cursor.close();
        db.close();
        return itemId;
    }
}
