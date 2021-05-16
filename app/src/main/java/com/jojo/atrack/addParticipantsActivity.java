package com.jojo.atrack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class addParticipantsActivity extends AppCompatActivity {

    TextView tvTitle, tvName, tvPosition;
    EditText etEmpID;
    dbHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_participants);

        DB = new dbHelper(this);
        etEmpID = (EditText) findViewById(R.id.xpartID);
        tvName = findViewById(R.id.xpartName);
        tvPosition = findViewById(R.id.xpartPosition);
        tvTitle = findViewById(R.id.xadpTitle);

        Intent intent = getIntent();
        tvTitle.setText(intent.getStringExtra("Title"));

        etEmpID.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {

                    Cursor res = DB.selParticipant(etEmpID.getText().toString());

                    if(res.getCount() == 1){
                        res.moveToNext();
                        tvName.setText(res.getString(0));
                        tvPosition.setText(res.getString(1));
                        etEmpID.selectAll();
                    } else
                    {
                        tvName.setText("UNKNOWN");
                        tvPosition.setText("Position");
                        Toast.makeText(addParticipantsActivity.this, "Invalid ID Number", Toast.LENGTH_LONG).show();
                    }
                    return true;
                } else {
                    return false;
                }
            }
            });

}
    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
    }
}