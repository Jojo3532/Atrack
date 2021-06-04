package com.jojo.atrack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Employee extends AppCompatActivity {

    dbHelper DBH;
    ListView lvEmployee;
    Cursor EmployeeCursor;
    ArrayList<String> EmployeeList;
    ArrayAdapter EmployeeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        DBH = new dbHelper(this);
        lvEmployee = findViewById(R.id.lvEmployee);

        EmployeeList = new ArrayList<>();
        loadEmployees();

        lvEmployee.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                cls.SelectedEmpID = lvEmployee.getItemAtPosition(i).toString().substring(0, 7);
                Intent intent = new Intent(Employee.this, EmployeeDetail.class);
                startActivity(intent);
            }
        });
    }

    private void loadEmployees(){
        EmployeeCursor = DBH.getData(
                this,
                "SELECT EmpID || ' - ' || LastName || ' ' ||  FirstName || ' ' || substr(MiddleName, 1,1) || '. '  AS NAME FROM tblEmployee ORDER BY LastName ASC;",
                null
                );

        if (EmployeeCursor.getCount() == 0){
            Toast.makeText(this, "No record found", Toast.LENGTH_LONG).show();
        } else {
            while (EmployeeCursor.moveToNext()){
                EmployeeList.add(EmployeeCursor.getString(0));
            }

            EmployeeAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, EmployeeList);
            lvEmployee.setAdapter(EmployeeAdapter);
        }
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
    }
}