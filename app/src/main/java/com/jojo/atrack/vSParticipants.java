package com.jojo.atrack;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class vSParticipants extends AppCompatActivity {

    dbHelper DBH;
    ListView lvParticipants;
    Cursor ParticipantsCursor;
    ArrayList<String> ParticipantsList;
    ArrayAdapter ParticipantsAdapter;
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vsparticipants);

        DBH = new dbHelper(this);
        lvParticipants = findViewById(R.id.lvParticipants);
        tvTitle = findViewById(R.id.Title);

        ParticipantsList = new ArrayList<>();
        loadParticipants();
        tvTitle.setText(cls.SelectedTitle);
    }

    private void loadParticipants(){
        ParticipantsCursor = DBH.getData(this,
                "SELECT p.SeminarID, FirstName || ' ' || substr(MiddleName, 1,1) || '. ' || LastName AS NAME FROM tblParticipants p LEFT JOIN tblEmployee e ON p.EmpID = e.EmpID LEFT JOIN tblSeminar s ON p.SeminarID = s.SeminarID WHERE s.Title = ? ORDER BY NAME ASC;",
                new String[]{cls.SelectedTitle});

        if (ParticipantsCursor.getCount() == 0){
            Toast.makeText(this, "No record found", Toast.LENGTH_LONG).show();
        } else {
            while (ParticipantsCursor.moveToNext()){
                ParticipantsList.add(ParticipantsCursor.getString(1));
            }

            ParticipantsAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ParticipantsList);
            lvParticipants.setAdapter(ParticipantsAdapter);
        }
    }
}