package com.jojo.atrack;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    TextView tvUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        tvUser = (TextView) findViewById(R.id.xhuser);
        tvUser.setText(intent.getStringExtra("username"));
        statVariables.cUser = tvUser.getText().toString();
    }

    public void AddSeminar_click(View v){
        Intent intent = new Intent(HomeActivity.this, addSeminarActivity.class);
        startActivity(intent);
    }

    public void viewSeminars_click(View v){
        Intent intent = new Intent(HomeActivity.this, viewSeminarActivity.class);
        startActivity(intent);
    }

    public void participants_click(View v){

    }

    @Override
    public void onBackPressed()
    {
        finishAffinity();
    }
}