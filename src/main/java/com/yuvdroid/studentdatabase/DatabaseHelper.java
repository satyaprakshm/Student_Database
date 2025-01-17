package com.yuvdroid.studentdatabase;





import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "student.db";
    private static final int DATABASE_VERSION = 2; // Incremented version for the new column

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the students table with the isDeleted column
        String createTable = "CREATE TABLE students (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, isDeleted INTEGER DEFAULT 0)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Check if the old version is less than 2 to add the new column
        if (oldVersion < 2) {
            // Add the new column isDeleted to the students table
            db.execSQL("ALTER TABLE students ADD COLUMN isDeleted INTEGER DEFAULT 0");
        }
        // You can add more upgrade logic for future versions here
    }
}