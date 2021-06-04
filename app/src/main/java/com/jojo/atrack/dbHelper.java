package com.jojo.atrack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.text.format.DateFormat;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class dbHelper extends SQLiteOpenHelper {
    String DB_PATH = null;
    private static String DB_NAME = "Atrack";
    private SQLiteDatabase myDataBase;
    private final Context myContext;

    private static final String KEY_semID = "SeminarID", KEY_Title = "Title", KEY_dFrom = "DFrom", KEY_dTo = "DTo";

    public dbHelper(Context context) {
        super(context, DB_NAME, null, 10);
        this.myContext = context;
        this.DB_PATH = "/data/data/" + context.getPackageName() + "/" + "databases/";
        Log.e("Path 1", DB_PATH);
    }


    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (dbExist) {
        } else {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    private void copyDataBase() throws IOException {
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[10];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDataBase() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }

    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion)
            try {
                copyDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    //run a Query
    public Boolean rQry(String Query, String[] Arg) {
        try {
            SQLiteDatabase DB = this.getWritableDatabase();
            DB.rawQuery(Query, Arg);
            return true;
        } catch (Exception ee) {
            ee.printStackTrace();
            return false;
        }
    }

    //SELECT user
    public Cursor checkuser(String username, String password) {
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("SELECT * FROM tblUser WHERE username=? AND password=?", new String[]{username, password});
        return cursor;
    }

    //insert seminar
    public Boolean insertSeminar(Context context, String Title, String DFrom, String DTo) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date dFrom = sdf1.parse(DFrom);
            Date dTo = sdf1.parse(DTo);

            DFrom = DateFormat.format("yyyy-MM-dd", dFrom).toString();
            DTo = DateFormat.format("yyyy-MM-dd", dTo).toString();

            contentValues.put("Title", Title);
            contentValues.put("DFrom", DFrom);
            contentValues.put("DTo", DTo);
            contentValues.put("AddedBy", cls.Uname);

            DB.insert("tblSeminar", null, contentValues);
            return true;
        } catch (Exception ee) {
            Toast.makeText(context, ee.getMessage().toString(), Toast.LENGTH_LONG).show();
            return false;
        }
    }

    //SELECT Participant
    public Cursor selParticipant(String EmpID) {
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("SELECT FirstName || ' ' || substr(MiddleName, 1,1) || '. ' || LastName AS NAME, Position FROM tblEmployee WHERE EmpID=?", new String[]{EmpID});
        return cursor;
    }

    //record participant
    public Boolean insParticipant(Context context, String EmpID, String SeminarID) {
        try {
            //check if empID & seminar exist
            SQLiteDatabase DB = this.getReadableDatabase();
            Cursor cursor = DB.rawQuery("SELECT ParticipantsID FROM tblParticipants WHERE EmpID=? AND SeminarID=?", new String[]{EmpID, SeminarID});
            SQLiteDatabase DBw = this.getWritableDatabase();
            ContentValues cVal = new ContentValues();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            if (cursor.getCount() == 0) {
                //insert new in tblPaticipants
                cVal.put("SeminarID", SeminarID);
                cVal.put("EmpID", EmpID);
                cVal.put("In_1", sdf.format(new Date()));
                DBw.insert("tblParticipants", null, cVal);
            } else {
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
        } catch (Exception ee) {
            Toast.makeText(context, ee.getMessage().toString(), Toast.LENGTH_LONG).show();
            return false;
        }
    }

    //get last inserted ID of tblSeminar
    public String getSeminarID(Context context) {
        try {
            SQLiteDatabase DB = this.getReadableDatabase();
            Cursor cursor = DB.rawQuery("SELECT last_insert_rowid() AS semID FROM tblSeminar;", null);
            cursor.moveToNext();

            return cursor.getString(0);
        } catch (Exception ee) {
            Toast.makeText(context, ee.getMessage().toString(), Toast.LENGTH_LONG).show();
            return null;
        }
    }

    //getLog record
    public Cursor getLogRecord(Context context, String SeminarID, String EmpID) {
        String[] Rlog = null;
        try {
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
    public Cursor getData(Context context, String Query, String[] Arg) {
        try {
            SQLiteDatabase DB = this.getReadableDatabase();
            Cursor cursor = DB.rawQuery(Query, Arg);
            return cursor;
        } catch (Exception ee) {
            Toast.makeText(context, ee.getMessage().toString(), Toast.LENGTH_LONG).show();
            return null;
        }
    }

    //getSeminars
    public SimpleCursorAdapter getSeminars() {
        SQLiteDatabase DB = this.getReadableDatabase();
        String columns[] = {dbHelper.KEY_semID, dbHelper.KEY_Title, dbHelper.KEY_dFrom, dbHelper.KEY_dTo};
        Cursor cursor = DB.rawQuery("SELECT SeminarID, Title, DFrom, DTo FROM tblSeminar WHERE AddedBy = ? ORDER BY DTo DESC;", new String[]{cls.Uname});
        String[] fromFieldNames = new String[]{dbHelper.KEY_semID, dbHelper.KEY_Title, dbHelper.KEY_dFrom, dbHelper.KEY_dTo};
        int[] toViewIDs = new int[]{R.id.dSeminarID, R.id.dTitle, R.id.dDFrom, R.id.dDTo};
        SimpleCursorAdapter SCA = new SimpleCursorAdapter(
                myContext,
                R.layout.single_item,
                cursor,
                fromFieldNames,
                toViewIDs
        );
        return SCA;
    }

    public Boolean updateEmployee(Context context, String EmpID, String LastName, String FirstName, String MiddleName, String Position){
        try{
            SQLiteDatabase DB = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("LastName", LastName);
            contentValues.put("FirstName", FirstName);
            contentValues.put("MiddleName", MiddleName);
            contentValues.put("Position", Position);
            DB.update("tblEmployee", contentValues, "EmpID=?", new String[]{EmpID});
            return true;
        }catch (Exception e){
            Toast.makeText(context, e.getMessage().toString(), Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public Boolean updatelUser(String username, String password) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        long result = DB.update("tblUser", contentValues, "username=?", new String[]{username});
        return result != -1;
    }

    public Boolean deletetblUser(String username, String password) {
        SQLiteDatabase DB = this.getWritableDatabase();
        long result = DB.delete("tblUser", "username=?", new String[]{username});
        return result != -1;
    }
}