package com.snakes_ladder.dice.util

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AppDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE " + PERSON_TABLE_NAME + "(" +
                PERSON_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                PERSON_COLUMN_NAME + " TEXT, " +
                PERSON_COLUMN_ADDRESS + " TEXT, " +
                PERSON_COLUMN_MOBILE + " TEXT, " +
                PERSON_COLUMN_PASSWORD + " INTEGER)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + PERSON_TABLE_NAME)
        onCreate(db)
    }

    fun insertPerson(name: String, address: String, mobile: String, password: String): Boolean {
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(PERSON_COLUMN_NAME, name)
        contentValues.put(PERSON_COLUMN_ADDRESS, address)
        contentValues.put(PERSON_COLUMN_MOBILE, mobile)
        contentValues.put(PERSON_COLUMN_PASSWORD, password)
        db.insert(PERSON_TABLE_NAME, null, contentValues)
        return true
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

    fun getPerson(mobile: String): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM " + PERSON_TABLE_NAME + " WHERE " +
                PERSON_COLUMN_MOBILE + "=?", arrayOf(mobile))
    }

    companion object {

        val DATABASE_NAME = "SQLiteDice.db"
        private val DATABASE_VERSION = 1
        val PERSON_TABLE_NAME = "person"
        val PERSON_COLUMN_ID = "_id"
        val PERSON_COLUMN_NAME = "name"
        val PERSON_COLUMN_ADDRESS = "address"
        val PERSON_COLUMN_MOBILE = "mobile"
        val PERSON_COLUMN_PASSWORD = "password"
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