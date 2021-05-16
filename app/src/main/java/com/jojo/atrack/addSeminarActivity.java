package com.jojo.atrack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class addSeminarActivity extends AppCompatActivity {
    EditText etTitle, etStart, etEnd;
    Button btnSave;
    cls jcls = new cls();
    dbHelper DB;

    Date date = new Date();
    DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    String cDate = df.format(date);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_seminar);

        etTitle = (EditText) findViewById(R.id.xadsTitle);
        etStart = (EditText) findViewById(R.id.xadsStart);
        etEnd = (EditText) findViewById(R.id.xadsEnd);
        btnSave = (Button) findViewById(R.id.xbtnSave);

        etStart.setText(cDate);
        etEnd.setText(cDate);
        DB = new dbHelper(this);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isValid = false;
                if(isdtValid() && jcls.isNotEmpty(etTitle.getText().toString())){
                    if (DB.insertSeminar(addSeminarActivity.this, etTitle.getText().toString(), etStart.getText().toString(), etEnd.getText().toString())){
                        Toast.makeText(addSeminarActivity.this, "New Seminar added", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(addSeminarActivity.this, addParticipantsActivity.class);
                        intent.putExtra("Title", etTitle.getText().toString());
                        startActivity(intent);
                    }else {
                        //Toast.makeText(addSeminarActivity.this, "Unable to add to the database", Toast.LENGTH_LONG).show();
                    }

                }else {
                    Toast.makeText(addSeminarActivity.this, "Invalid seminar details, please double check your inputs", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private Boolean isdtValid(){
        String sFrom = etStart.getText().toString(), sTo = etEnd.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        try {
            if (sdf.parse(sFrom).getTime() <= sdf.parse(sTo).getTime()){
                return true;
            } else return false;

        }catch (Exception ee){
            return false;
        }
    }
}