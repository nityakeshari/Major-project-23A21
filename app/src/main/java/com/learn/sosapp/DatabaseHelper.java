package com.learn.sosapp;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "SOS.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create your table(s) here
        String createTableQuery = "CREATE TABLE Contacts (_id INTEGER PRIMARY KEY AUTOINCREMENT, Contact1 TEXT, Contact2 TEXT, Contact3 TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrade if needed
        // You can drop and recreate the table(s) here
        String dropTableQuery = "DROP TABLE IF EXISTS Contacts";
        db.execSQL(dropTableQuery);
        onCreate(db);
    }

    public long insertData(String variableValue, String variableName) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(variableName, variableValue);

        long result = db.insert("Contacts", null, values);
        db.close();
        return result;
    }

    @SuppressLint("Range")
    public String getVariable(String variableName) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT "+variableName+" FROM Contacts";
        Cursor cursor = db.rawQuery(query, null);

        String variableValue = null;
        if (cursor.moveToFirst()) {
            variableValue = cursor.getString(cursor.getColumnIndex(variableName));
        }

        cursor.close();
        db.close();
        return variableValue;
    }
    public int updateVariable(String newVariableValue, String variableName) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(variableName, newVariableValue);

        int rowsAffected = db.update("Contacts", values, null, null);
        db.close();
        return rowsAffected;
    }
}