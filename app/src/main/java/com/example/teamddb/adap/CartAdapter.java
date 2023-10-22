package com.example.teamddb.adap;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.teamddb.R;
import com.example.teamddb.database.DatabaseHelper;
import com.example.teamddb.model.CartItem;

import java.util.ArrayList;

public class CartAdapter extends ArrayAdapter<CartItem> {
    private Context context;
    private DatabaseHelper dbHelper;

    public CartAdapter(Context context, ArrayList<CartItem> cartItems) {
        super(context, 0, cartItems);
        this.context = context;
        this.dbHelper = new DatabaseHelper(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cart_item, parent, false);
        }

        final CartItem cartItem = getItem(position);

        TextView itemNameTextView = convertView.findViewById(R.id.cartItemName);
        TextView itemPriceTextView = convertView.findViewById(R.id.cartItemPrice);
        TextView itemQuantityTextView = convertView.findViewById(R.id.cartItemQuantity);
        Button editQuantityButton = convertView.findViewById(R.id.editQuantityButton);
        Button deleteItemButton = convertView.findViewById(R.id.deleteItemButton);
        Button checkoutButton = convertView.findViewById(R.id.cartItemCheckoutButton);

        itemNameTextView.setText(cartItem.getName());
        itemPriceTextView.setText("Giá: " + cartItem.getPrice() + " đ");
        itemQuantityTextView.setText("Số lượng: " + cartItem.getQuantity());

        editQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditQuantityDialog(position, cartItem);
            }
        });

        deleteItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog(position, cartItem);
            }
        });

        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCheckoutConfirmationDialog(position, cartItem);
            }
        });

        return convertView;
    }

    private void showEditQuantityDialog(final int position, final CartItem cartItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Sửa số lượng");

        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_quantity, null);
        builder.setView(dialogView);

        final EditText quantityEditText = dialogView.findViewById(R.id.quantityEditText);
        quantityEditText.setText(String.valueOf(cartItem.getQuantity()));

        builder.setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int newQuantity = Integer.parseInt(quantityEditText.getText().toString());
                cartItem.setQuantity(newQuantity);
                dbHelper.updateCartItemQuantity(cartItem.getId(), newQuantity);
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

    private void showDeleteConfirmationDialog(final int position, final CartItem cartItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xác nhận xóa");

        builder.setMessage("Bạn có chắc chắn muốn xóa mục hàng này khỏi giỏ hàng không?");

        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                remove(getItem(position));
                dbHelper.removeFromCart(cartItem.getId());
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

    private void showCheckoutConfirmationDialog(final int position, final CartItem cartItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xác nhận thanh toán");

        View dialogView = LayoutInflater.from(context).inflate(R.layout.checkout_confirmation_dialog, null);
        TextView confirmationMessageTextView = dialogView.findViewById(R.id.confirmationMessageTextView);

        final int totalAmount = cartItem.getPrice() * cartItem.getQuantity();
        confirmationMessageTextView.setText("Tổng số tiền cần thanh toán: " + totalAmount + " đ");

        builder.setView(dialogView);

        builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                performCheckout(position, cartItem);
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
        db.delete("giohang_chuyenxe", "id=?", new String[] { String.valueOf(cartItem.getId()) });
//        db.oncreate("history", "id=?", new String[] { String.valueOf(cartItem.getId()) });
        remove(getItem(position));
        notifyDataSetChanged();
        db.close();
    }
}
