package com.example.teamddb;
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

import com.example.teamddb.adap.CustomAdapter;
import com.example.teamddb.controller.DanhSachChuyenXe;
import com.example.teamddb.controller.con_acc;
import com.example.teamddb.database.DatabaseHelper;
import com.example.teamddb.model.Place;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
public class History extends AppCompatActivity {
    private GridView placeGridView;
    private ArrayList<Place> placeList;
    private CustomAdapter placeAdapter;
    private DatabaseHelper dbHelper;
    private EditText searchEditText;
    private TextView searchLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_layout);

        dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.onUpgrade(db, 1, 2);
        placeGridView = findViewById(R.id.placeListHistory);

        // Tạo danh sách sản phẩm và Adapter
        placeList = new ArrayList<>();

        navigate();

        placeList = dbHelper.getdata_chuyenxe();
        placeAdapter = new CustomAdapter(this, placeList);

        // Thiết lập Adapter cho GridView
        placeGridView.setAdapter(placeAdapter);
        placeAdapter.notifyDataSetChanged();

        // Khởi tạo các thành phần liên quan đến tìm kiếm
//        searchHistoryEditText = findViewById(R.id.searchHistoryEditText);
//
//        // Xử lý sự kiện khi EditText thay đổi
//        searchHistoryEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                // Không cần xử lý trước sự thay đổi của EditText
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                // Thực hiện tìm kiếm dựa trên nội dung của EditText và cập nhật danh sách địa điểm
//                performSearch(s.toString());
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                // Không cần xử lý sau sự thay đổi của EditText
//            }
//        });
    }

    // Hàm thực hiện tìm kiếm và cập nhật danh sách hiển thị
    private void performSearch(String query) {
        ArrayList<Place> filteredList = new ArrayList<>();
        for (Place place : placeList) {
            if (place.getDiemden().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(place);
            }
        }
        placeAdapter = new CustomAdapter(History.this, filteredList);
        placeGridView.setAdapter(placeAdapter);
        placeAdapter.notifyDataSetChanged();
    }
    private void navigate() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom);
        bottomNavigationView.setSelectedItemId(R.id.history);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.history) {
                return true;
            } else if (itemId == R.id.acc) {
                startActivity(new Intent(this, con_acc.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.buyticket) {
                startActivity(new Intent(this, DanhSachChuyenXe.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            }
            return false;
        });
    }
}
