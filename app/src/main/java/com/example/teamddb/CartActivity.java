package com.example.teamddb;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    private ListView listView;
    private CartAdapter cartAdapter;
    private static ArrayList<CartItem> cartItems; // Biến static để lưu trữ dữ liệu giỏ hàng
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        dbHelper = new DatabaseHelper(this);
        listView = findViewById(R.id.cartListView);

        cartItems = dbHelper.getCartItems();

        cartAdapter = new CartAdapter(this, cartItems);
        listView.setAdapter(cartAdapter);

        // Xử lý sự kiện bấm vào để sửa số lượng
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Xử lý logic sửa số lượng ở vị trí position
                CartItem itemToEdit = cartItems.get(position);
                showEditQuantityDialog(itemToEdit); // Hiển thị dialog sửa số lượng
            }
        });

        // Xử lý sự kiện nhấn giữ để xóa
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Xử lý logic xóa mục hàng ở vị trí position
                CartItem itemToRemove = cartItems.get(position);
                cartItems.remove(position); // Xóa mục hàng khỏi danh sách
                cartAdapter.notifyDataSetChanged(); // Cập nhật ListView
                // Cập nhật cơ sở dữ liệu nếu cần
                dbHelper.removeFromCart(itemToRemove.getId());
                return true; // Đánh dấu rằng sự kiện đã được xử lý
            }
        });
        Button backToPlacesButton = findViewById(R.id.backToPlacesButton);
        backToPlacesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển về màn hình danh sách địa điểm
                Intent intent = new Intent(CartActivity.this, DanhSachChuyenXe.class);
                startActivity(intent);
            }
        });

    }

    private void showEditQuantityDialog(final CartItem itemToEdit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sửa số lượng");

        // Inflate layout cho dialog sửa số lượng
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_quantity, null);
        builder.setView(dialogView);

        final EditText quantityEditText = dialogView.findViewById(R.id.quantityEditText);
        quantityEditText.setText(String.valueOf(itemToEdit.getQuantity()));

        builder.setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Lấy số lượng từ EditText
                int newQuantity = Integer.parseInt(quantityEditText.getText().toString());
                // Cập nhật số lượng trong danh sách và cơ sở dữ liệu
                itemToEdit.setQuantity(newQuantity);
                dbHelper.updateCartItemQuantity(itemToEdit.getId(), newQuantity);
                cartAdapter.notifyDataSetChanged(); // Cập nhật ListView
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
