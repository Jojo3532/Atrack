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
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int il) {
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
        contentValues.put("AddedBy", cls.Uname);

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

    //record participant
    public Boolean insParticipant(Context context, String EmpID, String SeminarID){
        try{
            //check if empID & seminar exist
            SQLiteDatabase DB = this.getReadableDatabase();
            Cursor cursor = DB.rawQuery("SELECT ParticipantsID FROM tblParticipants WHERE EmpID=? AND SeminarID=?", new String[]{EmpID, SeminarID});
            SQLiteDatabase DBw = this.getWritableDatabase();
            ContentValues cVal = new ContentValues();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            if (cursor.getCount() == 0){
                //insert new in tblPaticipants
                cVal.put("SeminarID", SeminarID);
                cVal.put("EmpID", EmpID);
                cVal.put("In_1", sdf.format(new Date()));
                DBw.insert("tblParticipants", null, cVal);
            }else {
                //update Log In or out
                ContentValues cUp = new ContentValues();
                cUp.put("Out_2", sdf.format(new Date()));
                DB.update("tblParticipants", cUp, "EmpID=? AND SeminarID=? AND In_2 IS NOT NULL AND Out_2 IS NULL", new String[]{EmpID, SeminarID});

                cUp = new ContentValues();
                cUp.put("In_2", sdf.format(new Date()));
                DB.update("tblParticipants", cUp, "EmpID=? AND SeminarID=? AND Out_1 IS NOT NULL AND In_2 IS NULL", new String[]{EmpID, SeminarID});

                cUp = new ContentValues();
                cUp.put("Out_1", sdf.format(new Date()));
                DB.update("tblParticipants", cUp, "EmpID=? AND SeminarID=? AND In_1 IS NOT NULL AND Out_1 IS NULL", new String[]{EmpID, SeminarID});
            }
            return true;
        } catch (Exception ee){
            Toast.makeText(context, ee.getMessage().toString(), Toast.LENGTH_LONG).show();
            return false;
        }
    }

    //get last inserted ID of tblSeminar
    public String getSeminarID(Context context){
        try {
            SQLiteDatabase DB = this.getReadableDatabase();
            Cursor cursor = DB.rawQuery("SELECT last_insert_rowid() AS semID FROM tblSeminar;", null);
            cursor.moveToNext();

            return cursor.getString(0);
        } catch (Exception ee){
            Toast.makeText(context, ee.getMessage().toString(), Toast.LENGTH_LONG).show();
            return null;
        }
    }

    //getLog record
    public Cursor getLogRecord(Context context, String SeminarID, String EmpID){
        String[] Rlog = null;
        try{
            SQLiteDatabase DB = this.getReadableDatabase();
            ContentValues CV = new ContentValues();
            CV.put("EmpID", EmpID);
            CV.put("SeminarID", SeminarID);
            Cursor cursor = DB.rawQuery("SELECT In_1, Out_1, In_2, Out_2 FROM tblParticipants WHERE EmpID=? AND SeminarID=?;", new String[]{EmpID, SeminarID});
            cursor.moveToNext();
            return cursor;
        } catch (Exception ee) {
            Toast.makeText(context, ee.getMessage().toString(), Toast.LENGTH_LONG).show();
            return null;
        }
    }

    //get Data
    public Cursor getData (Context context, String Query, String[] Arg){
        try{
            SQLiteDatabase DB = this.getReadableDatabase();
            Cursor cursor = DB.rawQuery(Query, Arg);
            return cursor;
        } catch (Exception ee){
            Toast.makeText(context, ee.getMessage().toString(), Toast.LENGTH_LONG).show();
            return null;
        }

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
