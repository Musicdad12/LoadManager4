package com.jrschugel.loadmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by seanm on 8/13/2017.
 * Copyright 2017. All rights reserved.
 */

class DatabaseHelperExpense extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Load.db";
    private static final String TABLE_EXPENSES = "EXPENSES";
    private static final String TABLE_STOPS = "STOPS";
    private static final String TABLE_NAME = "LOADS";

    DatabaseHelperExpense(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_EXPENSES +" ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                Config.TAG_EXPLOAD + " INTEGER," +
                Config.TAG_EXPDESC + " TEXT, " +
                Config.TAG_EXPTYPE + " TEXT, " +
                Config.TAG_EXPPONUM + " TEXT, " +
                Config.TAG_EXPGALLON + " TEXT, " +
                Config.TAG_EXPAMOUNT + " TEXT); " );
        Log.d(TAG, "onCreate: Expense Table Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_EXPENSES);
        onCreate(db);
    }

    void AddExpense(Integer LoadNumber, String Description, String Type, String PONumber,
                 String Gallons, String Amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.TAG_EXPLOAD, LoadNumber);
        contentValues.put(Config.TAG_EXPDESC, Description);
        contentValues.put(Config.TAG_EXPTYPE, Type);
        contentValues.put(Config.TAG_EXPPONUM, PONumber);
        contentValues.put(Config.TAG_EXPGALLON, Gallons);
        contentValues.put(Config.TAG_EXPAMOUNT, Amount);
        db.insert(TABLE_EXPENSES, null, contentValues);
    }

    JSONArray getExpenseDataJSON(Context context, Integer LoadNumber) {

        String myPath = context.getDatabasePath("Load.db").toString();// Set path to your database

        //or you can use `context.getDatabasePath("my_db_test.db")`

        SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        String searchQuery = "select * from " + TABLE_EXPENSES +
                " WHERE " + Config.TAG_EXPLOAD + " = " + LoadNumber ;
        Cursor cursor = myDataBase.rawQuery(searchQuery, null );

        JSONArray resultSet     = new JSONArray();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();

            for( int i=0 ;  i< totalColumn ; i++ )
            {
                if( cursor.getColumnName(i) != null )
                {
                    try
                    {
                        if( cursor.getString(i) != null )
                        {
                            rowObject.put(cursor.getColumnName(i) ,  cursor.getString(i) );
                        }
                        else
                        {
                            rowObject.put( cursor.getColumnName(i) ,  "" );
                        }
                    }
                    catch( Exception e )
                    {
                        Log.e("Error", e.getMessage()  );
                    }
                }
            }
            resultSet.put(rowObject);
            cursor.moveToNext();
        }
        cursor.close();
        return resultSet;
    }

    Boolean DeleteExpensefromListview (Long expenseID) {
        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(TABLE_EXPENSES, "_id" + "=" + expenseID, null);
        if (result == 0 ) {
            return false;
        }
        return true;
    }

    Boolean copyDeleteExpensefromListview (Integer LoadNumber, String Description, String PaymentType, String PONumber, String Gallons, String Amount) {
        SQLiteDatabase db = this.getWritableDatabase();

        String Query = "SELECT * FROM " + TABLE_EXPENSES + " WHERE " + Config.TAG_EXPLOAD + " = " + LoadNumber + " AND " +
                        Config.TAG_EXPDESC + " = '" + Description + "' AND " + Config.TAG_EXPTYPE + " = '" + PaymentType + "' AND " +
                        Config.TAG_EXPPONUM + " = '" + PONumber + "' AND " + Config.TAG_EXPGALLON + " = '" + Gallons + "' AND " +
                        Config.TAG_EXPAMOUNT + " = '" + Amount + "';";
        Integer DeleteID = 0;
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.moveToFirst()) {
            DeleteID = cursor.getInt(0);
            Log.d("Delete ID", DeleteID.toString());
            if (DeleteID == 0) {
                return false;
            }
        } else {
            return false;
        }
        int result = db.delete(TABLE_EXPENSES, "_id" + "=" + DeleteID, null);
        if (result == 0 ) {
            return false;
        }
        cursor.close();
    return true;
    }

    Integer GetStopCount(Integer LoadNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        Integer StopCount = 0;
        String Query ="SELECT count(*) FROM " + TABLE_STOPS + " WHERE " + Config.TAG_STOPLOAD + " = " + LoadNumber;
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.moveToFirst()) {
            StopCount = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return StopCount - 2;
    }

    ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "message" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try{
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(Query, null);

            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {

                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(Exception ex){
            Log.e("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }
    }

    Cursor getExpenseData(Integer LoadNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from " + TABLE_EXPENSES +
                " WHERE " + Config.TAG_EXPLOAD + " = " + LoadNumber,null);
    }
}