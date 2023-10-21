package com.example.teamddb;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.DialogInterface;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<Place> {
    private Context context;

    public CustomAdapter(Context context, ArrayList<Place> places) {
        super(context, 0, places);
        this.context = context;
    }

    // Trong lớp CustomAdapter
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Place item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_form, parent, false);
        }

        TextView nameTextView = convertView.findViewById(R.id.placeName);
        TextView priceTextView = convertView.findViewById(R.id.placePrice);
        TextView diemDi = convertView.findViewById(R.id.placeDiemdi);
        TextView diemDen = convertView.findViewById(R.id.placeDiemden);
        ImageView imageView = convertView.findViewById(R.id.placeImage);
        Button orderButton = convertView.findViewById(R.id.orderButton); // Thêm nút đặt hàng

        nameTextView.setText(item.getName());
        diemDi.setText(item.getDiemdi());
        diemDen.setText(item.getDiemden());
        priceTextView.setText(String.valueOf(item.getPrice()));
        imageView.setImageResource(item.getImageResource());


        // Xử lý sự kiện khi nút "Đặt hàng" được bấm
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOrderDialog(item);
            }
        });

        return convertView;
    }


    private void showOrderDialog(final Place place) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Đặt hàng: " + place.getName());

        // Tạo nội dung của AlertDialog từ tệp XML
        View dialogView = LayoutInflater.from(context).inflate(R.layout.order_dialog, null);
        final ImageView imageView = dialogView.findViewById(R.id.imageView);
        final TextView nameTextView = dialogView.findViewById(R.id.nameTextView);
        final TextView priceTextView = dialogView.findViewById(R.id.priceTextView);
        final TextView quantityTextView = dialogView.findViewById(R.id.quantityTextView);
        final Button increaseButton = dialogView.findViewById(R.id.increaseButton);
        final Button decreaseButton = dialogView.findViewById(R.id.decreaseButton);

        imageView.setImageResource(place.getImageResource());
        nameTextView.setText(place.getName());
        priceTextView.setText("Giá: " + place.getPrice() + " đ");
        final int[] quantity = {1}; // Số lượng mặc định là 1
        quantityTextView.setText("Số lượng: " + quantity[0]);

        // Xử lý sự kiện tăng số lượng
        increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity[0]++;
                quantityTextView.setText("Số lượng: " + quantity[0]);
            }
        });

        // Xử lý sự kiện giảm số lượng
        decreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity[0] > 1) {
                    quantity[0]--;
                    quantityTextView.setText("Số lượng: " + quantity[0]);
                }
            }
        });

        builder.setView(dialogView);

        // Thêm nút "Xác nhận" vào AlertDialog
        builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Lưu thông tin đặt hàng vào bảng giohang_diadiem
                saveOrderToDatabase(place, quantity[0]);
            }
        });

        // Thêm nút "Hủy" vào AlertDialog
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Hiển thị AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // Phương thức lưu thông tin đặt hàng vào bảng giohang_diadiem
    private void saveOrderToDatabase(Place place, int quantity) {
        SQLiteDatabase db = context.openOrCreateDatabase("bacaubedangkhoc", Context.MODE_PRIVATE, null);
        ContentValues values = new ContentValues();
        values.put("name", place.getName());
        values.put("price", place.getPrice());
        values.put("quantity", quantity);
        db.insert("giohang_chuyenxe", null, values);
        db.close();
    }
}
