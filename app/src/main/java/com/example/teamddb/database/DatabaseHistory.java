package com.example.teamddb.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.teamddb.HistoryItem;

import java.util.ArrayList;

public class DatabaseHistory extends SQLiteOpenHelper{
        private static final String DATABASE_NAME = "History";
        private static final int DATABASE_VERSION = 1;

        private static final String CREATE_HISTORY_TABLE = "CREATE TABLE history (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "price INT," +
                "quantity INT);";

        public DatabaseHistory(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_HISTORY_TABLE);
        }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public ArrayList<HistoryItem> getHistoryItems() {
            ArrayList<HistoryItem> historyItem = new ArrayList<>();
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM history", null);

            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex("id"));
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    int price = cursor.getInt(cursor.getColumnIndex("price"));
                    int quantity = cursor.getInt(cursor.getColumnIndex("quantity"));
                    historyItem.add(new HistoryItem(id, name, price, quantity));
                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();

            return historyItem;
        }

        // Xóa sản phẩm khỏi giỏ hàng
        public void removeFromHistory(int historyItemId) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete("history", "id = ?", new String[]{String.valueOf(historyItemId)});
            db.close();
        }
    }
