package com.example.teamddb.controller;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AlertDialogLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teamddb.Database;
import com.example.teamddb.R;
import com.example.teamddb.adap.adap_acc;
import com.example.teamddb.model.acc;
import com.example.teamddb.ultils.RecyclerTouchListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

    public class con_acc extends AppCompatActivity {

        Database db;
        List<acc> a;
        RecyclerView recyclerview;
        EditText ed;
        adap_acc aa;
    FloatingActionButton them;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_acc);
        recyclerview = findViewById(R.id.rc);
        ed = findViewById(R.id.search);
        db = new Database(this);
        them = findViewById(R.id.them);
        them.setOnClickListener(view -> showDialog(null,-1));
        getData();
    }


    private void getData() {
        a = db.getAll();
        aa = new adap_acc(this, a);

        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerview.setAdapter(aa);

        recyclerview.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerview, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                showDialog(a.get(position), position);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        aa.notifyDataSetChanged();
    }

    private void showDialog(acc ac, int position) {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.dialogthemacc, null);
        final EditText editTaiKhoan = promptsView.findViewById(R.id.taikhoan);
        final EditText editMatKhau = promptsView.findViewById(R.id.matkhau);
        final EditText editEmail = promptsView.findViewById(R.id.email);
        final EditText editHoTen = promptsView.findViewById(R.id.hoten);

        AlertDialog.Builder dialog_them = new AlertDialog.Builder(con_acc.this);
        dialog_them.setView(promptsView);

        dialog_them.setCancelable(true)
                .setPositiveButton("Lưu", null)
                .setNeutralButton("Huỷ", (dialog, id) -> dialog.cancel());

        if (ac != null) {
            editTaiKhoan.setText(ac.getAccName());
            editMatKhau.setText(ac.getPassword());
            editEmail.setText(ac.getEmail());
            editHoTen.setText(ac.getName());
            dialog_them.setNegativeButton("Xoá", (dialog, id) -> {
                new AlertDialog.Builder(this)
                        .setIcon(R.drawable.baseline_delete_forever_24)
                        .setTitle("Xác nhận xoá")
                        .setMessage("Bạn có xác nhận muốn xoá tài khoản " + ac.getAccName() + " ?")
                        .setPositiveButton("Có", (d, which) -> {
//                            if (ac.getAdmin().matches("1")) {
//                                Toast.makeText(this, "Không thể xoá tài khoản đang đăng nhập", Toast.LENGTH_SHORT).show();
//                            } else {
                            delete(position, ac.getId());
                            Toast.makeText(this, "Đã xoá tài khoản " + ac.getAccName(), Toast.LENGTH_SHORT).show();
//                            }
                        })
                        .setNegativeButton("Không", null)
                        .setNeutralButton("Huỷ", null)
                        .show();

            });
        }
        AlertDialog alertDialog = dialog_them.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view -> {
            String strTaiKhoan = editTaiKhoan.getText().toString(),
                    strMatKhau = editMatKhau.getText().toString();
            if (strTaiKhoan.matches("")) {
                editTaiKhoan.requestFocus();
//                editTaiKhoan.setError(getString(R.string.TaiKhoanNull));
            } else if (strMatKhau.matches("")) {
                editMatKhau.requestFocus();
//                editMatKhau.setError(getString(R.string.MatKhauNull));
            } else if (checkTaiKhoanDuplicate(a, strTaiKhoan, ac)) {
//                editTaiKhoan.setError(getString(R.string.TaiKhoanExists));
                editTaiKhoan.requestFocus();
            } else if (ac == null) {
                create(editTaiKhoan.getText().toString(), editMatKhau.getText().toString(),
                    editEmail.getText().toString(), editHoTen.getText().toString());
                alertDialog.dismiss();
            } else {
                update(position, ac.getId(), editTaiKhoan.getText().toString(), editMatKhau.getText().toString(),
                        editEmail.getText().toString(), editHoTen.getText().toString());
                alertDialog.dismiss();
            }
        });
    }
    
    private boolean checkTaiKhoanDuplicate(List<acc> a, String stracc, acc ac) {
        List<acc> taiKhoanListCheck = new ArrayList<>(a);
        if (ac != null) {
            taiKhoanListCheck.remove(ac);
        }
        for (acc x : taiKhoanListCheck) {
            if (x.getName().matches(stracc)) {
                return true;
            }
        }
        return false;
    }

    private void create(String accname, String pass,String email , String name) {
        long id = db.create(accname, pass, email,name);
        acc ac = new acc();
        ac.setId(id);
        ac.setAccName(accname);
        ac.setPassword(pass);
        ac.setEmail(email);
        ac.setName(name);
        a.add(0, ac);
        aa.notifyDataSetChanged();
    }

    private void update(int position, long id, String accname, String pass,String email , String name) {
        db.update(id, accname, pass, email,name);
        acc ac = new acc();
        ac.setId(id);
        ac.setAccName(accname);
        ac.setPassword(pass);
        ac.setEmail(email);
        ac.setName(name);
        a.set(position, ac);
        aa.notifyItemChanged(position);
    }

    private void delete(int position, long id) {
        db.delete(id);
        a.remove(position);
        aa.notifyItemRemoved(position);
    }
}
