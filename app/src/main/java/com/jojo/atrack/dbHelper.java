package com.jojo.atrack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
import android.text.format.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class dbHelper extends SQLiteOpenHelper {
    public dbHelper(Context context) {
        super(context, "Atrack.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("CREATE TABLE tblUser (" +
                "username TEXT PRIMARY KEY," +
                "password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int il) {

        DB.execSQL("DROP TABLE IF EXISTS tblUser");

    }

    //SELECT user
    public Cursor checkuser(String username, String password){
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("SELECT * FROM tblUser WHERE username=? AND password=?", new String[]{username, password});
        return cursor;
    }

    //insert seminar
    public Boolean insertSeminar(Context context, String Title, String DFrom, String DTo){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

       try{
            Date dFrom = sdf1.parse(DFrom);
            Date dTo = sdf1.parse(DTo);

            DFrom = DateFormat.format("yyyy-MM-dd", dFrom).toString();
            DTo= DateFormat.format("yyyy-MM-dd", dTo).toString();

        contentValues.put("Title", Title);
        contentValues.put("DFrom", DFrom);
        contentValues.put("DTo", DTo);
        contentValues.put("AddedBy", statVariables.cUser);

        DB.insert("tblSeminar", null, contentValues);
        return true;
       } catch (Exception ee){
           Toast.makeText(context, ee.getMessage().toString(), Toast.LENGTH_LONG).show();
           return false;
       }
    }

    //SELECT Participant
    public Cursor selParticipant(String EmpID){
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("SELECT FirstName || ' ' || substr(MiddleName, 1,1) || '. ' || LastName AS NAME, Position FROM tblEmployee WHERE EmpID=?", new String[]{EmpID});
        return cursor;
    }

    public Boolean updatelUser(String username, String password){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        long result = DB.update("tblUser", contentValues, "username=?", new String[]{username});
        return result != -1;
    }

    public Boolean deletetblUser(String username, String password){
        SQLiteDatabase DB = this.getWritableDatabase();
        long result = DB.delete("tblUser", "username=?", new String[]{username});
        return result != -1;
    }

}
