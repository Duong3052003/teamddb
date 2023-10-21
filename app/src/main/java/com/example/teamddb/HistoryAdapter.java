package com.example.teamddb;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.teamddb.database.DatabaseHelper;
import com.example.teamddb.model.CartItem;

import java.util.ArrayList;

public class HistoryAdapter extends ArrayAdapter<HistoryItem> {
    private Context context;
    private DatabaseHelper dbHelper;

    public HistoryAdapter(Context context, ArrayList<HistoryItem> HistoryItem) {
        super(context, 0, HistoryItem);
        this.context = context;
        this.dbHelper = new DatabaseHelper(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.history_item, parent, false);
        }

        final HistoryItem historyItem = getItem(position);

        TextView itemNameTextView = convertView.findViewById(R.id.HistoryItemName);
        TextView itemPriceTextView = convertView.findViewById(R.id.HistoryItemPrice);
        TextView itemQuantityTextView = convertView.findViewById(R.id.HistoryItemQuantity);
        Button deleteItemButton = convertView.findViewById(R.id.deleteItemHistoryButton);

        itemNameTextView.setText(historyItem.getName());
        itemPriceTextView.setText("Giá: " + historyItem.getPrice() + " đ");
        itemQuantityTextView.setText("Số lượng: " + historyItem.getQuantity());

        deleteItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog(position, historyItem);
            }
        });

        return convertView;
    }

    private void showDeleteConfirmationDialog(final int position, final HistoryItem historyItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xác nhận xóa");

        builder.setMessage("Xóa mục hàng này khỏi lịch sử đặt vé?");

        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                remove(getItem(position));
                dbHelper.removeFromCart(historyItem.getId());
                notifyDataSetChanged();
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

    private void performCheckout(int position, CartItem cartItem) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("history", "id=?", new String[] { String.valueOf(cartItem.getId()) });
        remove(getItem(position));
        notifyDataSetChanged();
        db.close();
    }
}
