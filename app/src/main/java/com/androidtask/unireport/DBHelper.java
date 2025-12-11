package com.androidtask.unireport;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "UniReports.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "reports";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Runs once when the app is installed to create the table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "description TEXT, " +
                "location TEXT)";
        db.execSQL(createTable);
    }

    // Runs if you change the version number (updates the schema)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // --- CRUD OPERATIONS ---

    // 1. ADD (Create)
    public boolean addReport(String title, String desc, String loc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("title", title);
        cv.put("description", desc);
        cv.put("location", loc);

        long result = db.insert(TABLE_NAME, null, cv);
        db.close();
        return result != -1; // Returns true if insert was successful
    }

    // 2. GET ALL (Read)
    public List<Report> getAllReports() {
        List<Report> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM " + TABLE_NAME + " ORDER BY id DESC"; // Newest first

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                // Extract data from the cursor
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String desc = cursor.getString(2);
                String loc = cursor.getString(3);

                // Create object and add to list
                returnList.add(new Report(id, title, desc, loc));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return returnList;
    }
}
