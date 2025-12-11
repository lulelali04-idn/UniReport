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
    private static final int DATABASE_VERSION = 5;

    private static final String TABLE_REPORTS = "reports";
    private static final String TABLE_USERS = "users";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createReports = "CREATE TABLE " + TABLE_REPORTS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "description TEXT, " +
                "location TEXT, " +
                "category TEXT, " +
                "status TEXT, " +
                "image TEXT)";
        db.execSQL(createReports);

        String createUsers = "CREATE TABLE " + TABLE_USERS + " (" +
                "username TEXT PRIMARY KEY, " +
                "password TEXT, " +
                "role TEXT)";
        db.execSQL(createUsers);
        db.execSQL("INSERT INTO " + TABLE_USERS + " VALUES ('admin', 'admin123', 'admin')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPORTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }
    public boolean registerUser(String username, String password, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("password", password);
        cv.put("role", role);
        return db.insert(TABLE_USERS, null, cv) != -1;
    }

    public String checkLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT role FROM " + TABLE_USERS + " WHERE username=? AND password=?", new String[]{username, password});
        if (cursor.moveToFirst()) {
            String role = cursor.getString(0);
            cursor.close();
            return role;
        }
        cursor.close();
        return null;
    }
    public boolean addReport(String title, String desc, String loc, String category, String image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("title", title);
        cv.put("description", desc);
        cv.put("location", loc);
        cv.put("category", category);
        cv.put("status", "Active");
        cv.put("image", image); // Save image
        return db.insert(TABLE_REPORTS, null, cv) != -1;
    }

    public void updateReportStatus(int id, String newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("status", newStatus);
        db.update(TABLE_REPORTS, cv, "id=?", new String[]{String.valueOf(id)});
    }

    public List<Report> getAllReports() {
        List<Report> returnList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_REPORTS + " ORDER BY id DESC", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String desc = cursor.getString(2);
                String loc = cursor.getString(3);
                String category = cursor.getString(4);
                String status = cursor.getString(5);
                String image = cursor.getString(6); // Retrieve image
                returnList.add(new Report(id, title, desc, loc, category, status, image));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return returnList;
    }

    public void deleteReport(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_REPORTS, "id=?", new String[]{String.valueOf(id)});
        db.close();
    }
}