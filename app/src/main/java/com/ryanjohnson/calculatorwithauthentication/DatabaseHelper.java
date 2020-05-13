package com.ryanjohnson.calculatorwithauthentication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    public static final String DATABASE_NAME = "Results.db";
    public static final String TABLE_NAME = "results_table";
    public static final String COL_1 = "Operation";
    public static final String COL_2 = "Result";
    public static final String COL_3 = "ResultBase";
    public static final String COL_4 = "Date";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (Operation TEXT, Result TEXT, ResultBase INTEGER, Date TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addResult(String result, int resultBase, String operation) {
        DateFormat dateFormat = new SimpleDateFormat("M/d/yy");
        Date date = new Date();

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, operation);
        contentValues.put(COL_2, result);
        contentValues.put(COL_3, resultBase);
        contentValues.put(COL_4, dateFormat.format(date));

        long transactionResult = db.insert(TABLE_NAME, null, contentValues);

        if(transactionResult == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void clearDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        String clearDBQuery = "DELETE FROM "+TABLE_NAME;
        db.execSQL(clearDBQuery);
    }
}