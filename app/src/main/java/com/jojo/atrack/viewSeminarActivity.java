package com.jojo.atrack;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class viewSeminarActivity extends AppCompatActivity {

    dbHelper DBH;
    ListView lvSeminar;
    Cursor SeminarCusrsor;
    ArrayList<String> SeminarList;
    ArrayAdapter SeminarAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_seminar);
        DBH = new dbHelper(this);
        lvSeminar = findViewById(R.id.lvSeminar);
        SeminarList = new ArrayList<>();

        loadSeminars();

        lvSeminar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                String idClicked = lvSeminar.getItemAtPosition(i).toString();
                Toast.makeText(viewSeminarActivity.this, idClicked, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadSeminars(){
        SeminarCusrsor = DBH.getData(this, "SELECT SeminarID, Title, DFrom, DTo FROM tblSeminar WHERE AddedBy = ?", new String[]{cls.Uname});

        if (SeminarCusrsor.getCount() == 0){
            Toast.makeText(this, "No record found", Toast.LENGTH_LONG).show();
        } else {
            while (SeminarCusrsor.moveToNext()){
                SeminarList.add(SeminarCusrsor.getString(1));
            }

            SeminarAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, SeminarList);
            lvSeminar.setAdapter(SeminarAdapter);
        }
    }
}