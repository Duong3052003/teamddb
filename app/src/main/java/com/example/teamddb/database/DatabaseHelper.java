package com.example.teamddb;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "bacaubedangkhoc";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_CHUYENXE_TABLE = "CREATE TABLE chuyenxe (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "name TEXT," +
            "price INT," +
            "diemdi TEXT," +
            "diemden TEXT," +
            "image_url TEXT);";

    private static final String CREATE_GIOHANGCHUYENXE_TABLE = "CREATE TABLE giohang_chuyenxe (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "name TEXT," +
            "price INT," +
            "quantity INT);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CHUYENXE_TABLE);
//        db.execSQL(CREATE_GIOHANGCHUYENXE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS chuyenxe");
//        db.execSQL("DROP TABLE IF EXISTS giohang_chuyenxe");
        onCreate(db);
    }
    public void addPlace(Place place) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("price", place.getPrice());
        values.put("name", place.getName());
        values.put("diemdi", place.getDiemdi());
        values.put("diemden", place.getDiemden());
        values.put("image_url", place.getImageResource());
        db.insert("chuyenxe", null, values);
        db.close();
    }

    public ArrayList<Place> getdata_chuyenxe() {
        ArrayList<Place> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name, price, diemdi, diemden, image_url FROM chuyenxe", null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                int price = cursor.getInt(cursor.getColumnIndex("price"));
                String diemdi = cursor.getString(cursor.getColumnIndex("diemdi"));
                String diemden = cursor.getString(cursor.getColumnIndex("diemden"));
                int imageResourceId = cursor.getInt(cursor.getColumnIndex("image_url"));
                Place item = new Place(imageResourceId, name, price, diemdi, diemden);
                list.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return list;
    }

    public ArrayList<CartItem> getCartItems() {
        ArrayList<CartItem> cartItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM giohang_chuyenxe", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                int price = cursor.getInt(cursor.getColumnIndex("price"));
                int quantity = cursor.getInt(cursor.getColumnIndex("quantity"));
                cartItems.add(new CartItem(id, name, price, quantity));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return cartItems;
    }

    // Cập nhật số lượng sản phẩm trong giỏ hàng
    public void updateCartItemQuantity(int cartItemId, int newQuantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("quantity", newQuantity);
        db.update("giohang_chuyenxe", values, "id = ?", new String[]{String.valueOf(cartItemId)});
        db.close();
    }

    // Xóa sản phẩm khỏi giỏ hàng
    public void removeFromCart(int cartItemId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("giohang_chuyenxe", "id = ?", new String[]{String.valueOf(cartItemId)});
        db.close();
    }
}

