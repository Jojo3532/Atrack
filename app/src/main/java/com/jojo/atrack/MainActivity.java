package com.jojo.atrack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    EditText etUsername, etPassword;
    Button btnLogin;
    TextView tvSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsername = findViewById(R.id.xusername);
        etPassword = findViewById(R.id.xpassword);
        btnLogin = findViewById(R.id.xbtnLogIn);
        tvSignUp = findViewById(R.id.xsignUp);
    //setting Up database
        dbHelper DBH = new dbHelper(MainActivity.this);
        try {
            DBH.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        try {
            DBH.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                Cursor res = DBH.checkuser(username, password);

                if(res.getCount() != 1){
                    Toast.makeText(MainActivity.this, "User not found" , Toast.LENGTH_LONG).show();
                } else
                {
                    while (res.moveToNext()) {
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        intent.putExtra("username", res.getString(0));
                        intent.putExtra("password", res.getString(1));
                        cls.Uname = res.getString(0);
                        startActivity(intent);
                    }
                }
            }
        });
    }
}