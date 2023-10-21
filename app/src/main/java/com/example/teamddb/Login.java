package com.example.teamddb;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teamddb.database.Database;

public class Login extends AppCompatActivity {

    EditText usernameEditText, passwordEditText;
    Button loginButton,formregisterButton;
//    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

//        Database db = new Database(this);
//        db.create("nguyen","password123");

//        Database dbHelper = new Database(this);
//        db = dbHelper.getWritableDatabase();

        usernameEditText = findViewById(R.id.editTextUsername);
        passwordEditText = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.buttonLogin);
        formregisterButton = findViewById(R.id.buttonFormRegister);

        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            // Thực hiện kiểm tra thông tin đăng nhập
            if (isValidLogin(username, password)) {
                // Đăng nhập thành công, chuyển đến hoạt động chính hoặc trang chính của ứng dụng
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
            } else {
                // Đăng nhập thất bại, hiển thị thông báo lỗi
                Toast.makeText(Login.this, "Tên người dùng hoặc mật khẩu không hợp lệ", Toast.LENGTH_SHORT).show();
            }
        });

        formregisterButton.setOnClickListener(view -> {
            // Chuyển đến trang đăng ký khi người dùng nhấn nút "Đăng ký"
            Intent intent = new Intent(Login.this, Register.class);
            startActivity(intent);
        });
    }

    public boolean isValidLogin(String username, String password) {
        SQLiteOpenHelper dbHelper = new Database(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = { "id" };
        String selection = "username = ? AND password = ?";
        String[] selectionArgs = { username, password };

        Cursor cursor = db.query("tbTaiKhoan", columns, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            cursor.close();
            db.close();
            return true; // Thông tin đăng nhập hợp lệ.
        } else {
            cursor.close();
            db.close();
            return false; // Thông tin đăng nhập không hợp lệ.
        }
    }
}
