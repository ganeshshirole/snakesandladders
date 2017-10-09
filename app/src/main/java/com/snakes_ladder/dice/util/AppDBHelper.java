package com.snakes_ladder.dice.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "SQLiteDice.db";
    private static final int DATABASE_VERSION = 1;
    public static final String PERSON_TABLE_NAME = "person";
    public static final String PERSON_COLUMN_ID = "_id";
    public static final String PERSON_COLUMN_NAME = "name";
    public static final String PERSON_COLUMN_ADDRESS = "address";
    public static final String PERSON_COLUMN_MOBILE = "mobile";
    public static final String PERSON_COLUMN_PASSWORD = "password";

    public AppDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + PERSON_TABLE_NAME + "(" +
                PERSON_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                PERSON_COLUMN_NAME + " TEXT, " +
                PERSON_COLUMN_ADDRESS + " TEXT, " +
                PERSON_COLUMN_MOBILE + " TEXT, " +
                PERSON_COLUMN_PASSWORD + " INTEGER)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PERSON_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertPerson(String name, String address, String mobile, String password) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PERSON_COLUMN_NAME, name);
        contentValues.put(PERSON_COLUMN_ADDRESS, address);
        contentValues.put(PERSON_COLUMN_MOBILE, mobile);
        contentValues.put(PERSON_COLUMN_PASSWORD, password);
        db.insert(PERSON_TABLE_NAME, null, contentValues);
        return true;
    }

    /*public boolean updatePerson(Integer id, String name, String address, String mobile, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PERSON_COLUMN_NAME, name);
        contentValues.put(PERSON_COLUMN_ADDRESS, address);
        contentValues.put(PERSON_COLUMN_MOBILE, mobile);
        contentValues.put(PERSON_COLUMN_PASSWORD, password);
        db.update(PERSON_TABLE_NAME, contentValues, PERSON_COLUMN_ID + " = ? ", new String[]{Integer.toString(id)});
        return true;
    }*/

    public Cursor getPerson(String mobile) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + PERSON_TABLE_NAME + " WHERE " +
                PERSON_COLUMN_MOBILE + "=?", new String[]{mobile});
    }

    /*public Cursor getAllPersons() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + PERSON_TABLE_NAME, null);
    }

    public Integer deletePerson(String mobile) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(PERSON_TABLE_NAME,
                PERSON_COLUMN_MOBILE + " = ? ",
                new String[]{mobile});
    }*/

}