package com.example.teamddb.controller;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.teamddb.Place;
import com.example.teamddb.R;
import com.example.teamddb.adap.CustomAdapter;
import com.example.teamddb.database.DatabaseHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
public class DanhSachChuyenXe extends AppCompatActivity {
    private Button cartButton;
    private GridView placeGridView;
    private ArrayList<Place> placeList;
    private CustomAdapter placeAdapter;
    private DatabaseHelper dbHelper;
    private EditText searchEditText;
    private TextView searchLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listsp_form);

        dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.onUpgrade(db, 1, 2);
        placeGridView = findViewById(R.id.placeList);

        // Tạo danh sách sản phẩm và Adapter
        placeList = new ArrayList<>();
        Place place1 = new Place(R.drawable.hn_hp, "Sun World", 165000, "Ha Noi", "Hai Phong");
        dbHelper.addPlace(place1);
        Place place2 = new Place(R.drawable.hn_dn, "Asia Park", 95000,  "Ha Noi", "Da Nang");
        dbHelper.addPlace(place2);
        Place place3 = new Place(R.drawable.img, "VinWonders", 543200,"Hue", "Ho Chi Minh");
        dbHelper.addPlace(place3);
        Place place4 = new Place(R.drawable.img_1, "ZooDoo", 130000, "Hai Phong", "Hue");
        dbHelper.addPlace(place4);

        navigate();

        placeList = dbHelper.getdata_chuyenxe();
        placeAdapter = new CustomAdapter(this, placeList);

        // Thiết lập Adapter cho GridView
        placeGridView.setAdapter(placeAdapter);
        placeAdapter.notifyDataSetChanged();

        cartButton = findViewById(R.id.cartButton);

        cartButton.setOnClickListener(view -> {
            Intent intent = new Intent(DanhSachChuyenXe.this, CartActivity.class);
            startActivity(intent);
        });

        // Khởi tạo các thành phần liên quan đến tìm kiếm
        searchEditText = findViewById(R.id.searchEditText);

        // Xử lý sự kiện khi EditText thay đổi
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không cần xử lý trước sự thay đổi của EditText
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Thực hiện tìm kiếm dựa trên nội dung của EditText và cập nhật danh sách địa điểm
                performSearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Không cần xử lý sau sự thay đổi của EditText
            }
        });
    }

    // Hàm thực hiện tìm kiếm và cập nhật danh sách hiển thị
    private void performSearch(String query) {
        ArrayList<Place> filteredList = new ArrayList<>();
        for (Place place : placeList) {
            if (place.getDiemden().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(place);
            }
        }
        placeAdapter = new CustomAdapter(DanhSachChuyenXe.this, filteredList);
        placeGridView.setAdapter(placeAdapter);
        placeAdapter.notifyDataSetChanged();
    }
    private void navigate() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom);
        bottomNavigationView.setSelectedItemId(R.id.buyticket);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.buyticket) {
                return true;
            } else if (itemId == R.id.acc) {
                startActivity(new Intent(this, con_acc.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.history) {
                startActivity(new Intent(this, DanhSachChuyenXe.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            }
            return false;
        });
    }
}
