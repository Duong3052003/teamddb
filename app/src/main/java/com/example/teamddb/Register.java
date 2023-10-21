package com.example.teamddb;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {

    EditText usernameEditText, passwordEditText, emailEditText, nameEditText;
    Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layyout);

        usernameEditText = findViewById(R.id.editTextUsername);
        passwordEditText = findViewById(R.id.editTextPassword);
        emailEditText = findViewById(R.id.editTextEmail);
        nameEditText = findViewById(R.id.editTextName);
        registerButton = findViewById(R.id.buttonRegister);

        registerButton.setOnClickListener(v -> {

            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String name = nameEditText.getText().toString();

            // Thực hiện xử lý đăng ký ở đây, ví dụ: kiểm tra và lưu trữ thông tin

            // Kiểm tra dữ liệu hợp lệ
            if (username.isEmpty() || password.isEmpty() || email.isEmpty() || name.isEmpty()) {
                // Hiển thị thông báo lỗi nếu thông tin đăng ký không hợp lệ
                Toast.makeText(Register.this, "Vui lòng điền đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
            }
            if (!isUserAlreadyRegistered(username)) {
                // Thêm tài khoản mới vào cơ sở dữ liệu
                Database database = new Database(this);
                database.create(username, password, email, name);

                // Đăng ký thành công, thực hiện hành động khác (chẳng hạn chuyển đến màn hình đăng nhập)
            } else {
                // Hiển thị thông báo rằng tài khoản đã tồn tại
                Toast.makeText(Register.this, "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(this, "Đăng kí thành công", Toast.LENGTH_SHORT).show();
            // Sau khi đăng ký thành công, chuyển đến hoạt động đăng nhập
            Intent loginIntent = new Intent(Register.this, Login.class);
            startActivity(loginIntent);
        });
    }

    ;

    public boolean isUserAlreadyRegistered(String username) {
        Database dbHelper = new Database(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = null;

        try {

            String query = "SELECT * FROM tbTaiKhoan WHERE AccName = ?";
            cursor = db.rawQuery(query, new String[]{username});
            if (cursor != null && cursor.getCount() > 0) {
                // Tài khoản đã tồn tại
                return true;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return false;
    }

    ;
}
