package com.jojo.atrack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EmployeeDetail extends AppCompatActivity {

    EditText etLastName, etFirstName, etMiddleName, etPosition;
    Button btnSave;
    dbHelper DBH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_detail);

        DBH = new dbHelper(this);
        etLastName = findViewById(R.id.LastName);
        etFirstName = findViewById(R.id.FirstName);
        etMiddleName = findViewById(R.id.MiddleName);
        etPosition = findViewById(R.id.Position);
        btnSave = (Button) findViewById(R.id.BtnSave);

        Cursor cursor = DBH.getData(
                this,
                "SELECT LastName, FirstName, MiddleName, Position FROM tblEmployee WHERE EmpID = ?;",
                new String[]{cls.SelectedEmpID}
        );

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Error Occur", Toast.LENGTH_SHORT).show();
        } else {
            cursor.moveToNext();
            etLastName.setText(cursor.getString(0));
            etFirstName.setText(cursor.getString(1));
            etMiddleName.setText(cursor.getString(2));
            etPosition.setText(cursor.getString(3));
        }

        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Boolean qq = DBH.updateEmployee(
                        EmployeeDetail.this, cls.SelectedEmpID, etLastName.getText().toString(), etFirstName.getText().toString(), etMiddleName.getText().toString(), etPosition.getText().toString()
                );
                if (qq) {
                    Toast.makeText(EmployeeDetail.this, "Data has been successfully saved", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EmployeeDetail.this, Employee.class);
                    startActivity(intent);
                }
            }
        });
    }
}