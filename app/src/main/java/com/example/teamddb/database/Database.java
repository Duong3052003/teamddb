package com.example.teamddb.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.example.teamddb.model.acc;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "dbTaiKhoan";
    private static final String TABLE_NAME = "tbTaiKhoan";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_ACCNAME = "AccName";
    private static final String COLUMN_PASSWORD = "Password";
    private static final String COLUMN_EMAIL = "Email";
    private static final String COLUMN_NAME = "Name";
    private static final String COLUMN_ADMIN = "Admin";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String value = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT, %s TEXT,%s TEXT",
                TABLE_NAME, COLUMN_ID, COLUMN_ACCNAME, COLUMN_PASSWORD, COLUMN_EMAIL,COLUMN_NAME);
        db.execSQL(value);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String value = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
        db.execSQL(value);
        onCreate(db);
    }

    public long create(String accname, String password, String email,String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ACCNAME, accname);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_NAME, name);
        long id = db.insert(TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public int update(long id, String accname, String password, String email,String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ACCNAME, accname);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_NAME, name);
        return db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public void delete(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public acc getValue(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{COLUMN_ID,COLUMN_ACCNAME,COLUMN_PASSWORD, COLUMN_EMAIL ,COLUMN_NAME}, COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        acc a = new acc();
        a.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
        a.setAccName(cursor.getString(cursor.getColumnIndex(COLUMN_ACCNAME)));
        a.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)));
        a.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
        a.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
        cursor.close();
        return a;
    }

    public List<acc> getAll() {
        List<acc> list = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                acc a = new acc();
                a.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                a.setAccName(cursor.getString(cursor.getColumnIndex(COLUMN_ACCNAME)));
                a.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)));
                a.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
                a.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                list.add(a);
            } while (cursor.moveToNext());
        }
        db.close();
        return list;
    }
}