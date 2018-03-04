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

/**
 * Created by seanm on 8/13/2017.
 * Copyright 2017. All rights reserved.
 */

class DatabaseHelperStops extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Load.db";
    private static final String TABLE_STOPS = "STOPS";
    private static final String TABLE_NAME = "LOADS";

    DatabaseHelperStops(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_STOPS +" ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                Config.TAG_STOPLOAD + " INTEGER," +
                Config.TAG_STOPTYPE + " INTEGER, " +
                Config.TAG_STOPCUSTID + " INTEGER, " +
                Config.TAG_STOPCUSTNAME + " TEXT, " +
                Config.TAG_STOPCUSTADDR + " TEXT, " +
                Config.TAG_STOPCUSTADDR2 + " TEXT, " +
                Config.TAG_STOPCUSTCITY + " TEXT, " +
                Config.TAG_STOPCUSTSTATE + " TEXT, " +
                Config.TAG_STOPCUSTPHONE + " TEXT, " +
                Config.TAG_STOPCUSTCONTACT + " TEXT, " +
                Config.TAG_STOPEARLYDATE + " TEXT, " +
                Config.TAG_STOPEARLYTIME + " TEXT, " +
                Config.TAG_STOPLATEDATE + " TEXT, " +
                Config.TAG_STOPLATETIME + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_STOPS);
        onCreate(db);
    }

    void AddStop(Integer LoadNumber, Integer StopType, Integer CustomerID, String CustomerName,
                 String CustomerAddress, String CustomerAddress2, String CustomerCity, String CustomerState,
                 String CustomerPhone, String CustomerContact, String StopEarlyDate, String StopEarlyTime,
                 String StopLateDate, String StopLateTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.TAG_STOPLOAD, LoadNumber);
        contentValues.put(Config.TAG_STOPTYPE, StopType);
        contentValues.put(Config.TAG_STOPCUSTID, CustomerID);
        contentValues.put(Config.TAG_STOPCUSTNAME, CustomerName);
        contentValues.put(Config.TAG_STOPCUSTADDR, CustomerAddress);
        contentValues.put(Config.TAG_STOPCUSTADDR2, CustomerAddress2);
        contentValues.put(Config.TAG_STOPCUSTCITY, CustomerCity);
        contentValues.put(Config.TAG_STOPCUSTSTATE, CustomerState);
        contentValues.put(Config.TAG_STOPCUSTPHONE, CustomerPhone);
        contentValues.put(Config.TAG_STOPCUSTCONTACT, CustomerContact);
        contentValues.put(Config.TAG_STOPEARLYDATE, StopEarlyDate);
        contentValues.put(Config.TAG_STOPEARLYTIME, StopEarlyTime);
        contentValues.put(Config.TAG_STOPLATEDATE, StopLateDate);
        contentValues.put(Config.TAG_STOPLATETIME, StopLateTime);
        db.insert(TABLE_STOPS, null, contentValues);
    }

    JSONArray getLoadsDataJSON(Context context, Integer StopType, Integer LoadNumber) {

        String myPath = context.getDatabasePath("Load.db").toString();// Set path to your database

        //or you can use `context.getDatabasePath("my_db_test.db")`

        SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        String searchQuery = "select * from " + TABLE_STOPS +
                " WHERE " + Config.TAG_STOPTYPE + " = " + StopType + " AND " + Config.TAG_STOPLOAD + " = " + LoadNumber +
                " ORDER BY " + Config.TAG_STOPEARLYDATE + " ASC, " + Config.TAG_STOPEARLYTIME + " ASC;";
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

    JSONArray getAllLoadsList (Context context) {
        String SQLLoads = "SELECT " + Config.TAG_LOADNUMBER + " FROM " + TABLE_NAME + " ORDER BY " + Config.TAG_LOADNUMBER + " DESC";

        String myPath = context.getDatabasePath("Load.db").toString();// Set path to your database

        //or you can use `context.getDatabasePath("my_db_test.db")`

        SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        Cursor cursorLoads = myDataBase.rawQuery(SQLLoads, null);

        JSONArray resultSet     = new JSONArray();

        cursorLoads.moveToFirst();

        while (!cursorLoads.isAfterLast()) {

            String LoadNumber = cursorLoads.getString(0);

            String SQLShipper = "SELECT " + Config.TAG_STOPLOAD + ", " + Config.TAG_STOPEARLYDATE + " as ShipDate, " +
                    Config.TAG_STOPEARLYTIME + " as ShipTime, " + Config.TAG_STOPCUSTNAME + " as ShipperName, " +
                    Config.TAG_STOPCUSTCITY + " as ShipperCity, " + Config.TAG_STOPCUSTSTATE+" as ShipperState " +
                    "FROM " + TABLE_STOPS + " WHERE " + Config.TAG_STOPTYPE + " = 1 AND " + Config.TAG_STOPLOAD + " = " + LoadNumber +
                    " ORDER BY " + Config.TAG_STOPEARLYDATE + " ASC, " + Config.TAG_STOPEARLYTIME + " ASC LIMIT 1;";

            String SQLConsignee = "SELECT " + Config.TAG_STOPLOAD + ", " + Config.TAG_STOPEARLYDATE + " as ConsDate, " +
                    Config.TAG_STOPEARLYTIME + " as ConsTime, " + Config.TAG_STOPCUSTNAME + " as ConsName, " +
                    Config.TAG_STOPCUSTCITY + " as ConsCity, " + Config.TAG_STOPCUSTSTATE+" as ConsState " +
                    "FROM " + TABLE_STOPS + " WHERE " + Config.TAG_STOPTYPE + " = 2 AND " + Config.TAG_STOPLOAD + " = " + LoadNumber +
                    " ORDER BY " + Config.TAG_STOPEARLYDATE + " DESC, " + Config.TAG_STOPEARLYTIME + " DESC LIMIT 1;";

            Cursor cursorShip = myDataBase.rawQuery(SQLShipper, null);
            Cursor cursorCons = myDataBase.rawQuery(SQLConsignee, null);

            cursorShip.moveToFirst();
            cursorCons.moveToFirst();

            int totalColumn = cursorShip.getColumnCount();
            JSONObject rowObject = new JSONObject();

            for (int i = 0; i < totalColumn; i++) {
                if (cursorShip.getColumnName(i) != null) {
                    try {
                        if (cursorShip.getString(i) != null) {
                            rowObject.put(cursorShip.getColumnName(i), cursorShip.getString(i));
                        } else {
                            rowObject.put(cursorShip.getColumnName(i), "");
                        }
                        if (cursorCons.getString(i) != null) {
                            rowObject.put(cursorCons.getColumnName(i), cursorCons.getString(i));
                        } else {
                            rowObject.put(cursorCons.getColumnName(i), "");
                        }
                    } catch (Exception e) {
                        Log.e("Error", e.getMessage());
                    }
                }
            }
            resultSet.put(rowObject);

            cursorLoads.moveToNext();
            cursorShip.close();
            cursorCons.close();
        }
        cursorLoads.close();
        return resultSet;
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

    public String getShipper(String LoadNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Shipper = null;
        String Query ="SELECT * " +
                "FROM " + TABLE_STOPS + " WHERE " + Config.TAG_STOPLOAD + " = " + LoadNumber + " AND " + Config.TAG_STOPTYPE + " = 1 " +
                "ORDER BY " + Config.TAG_STOPEARLYDATE + " ASC, " + Config.TAG_STOPEARLYTIME + " ASC;";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.moveToFirst()) {
            Shipper = cursor.getString(5) + " - " + cursor.getString(8) + " - " + cursor.getString(9);
        }
        cursor.close();
        db.close();
        return Shipper;
    }

    public String getConsignee(String LoadNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Consignee = null;
        String Query ="SELECT * " +
                "FROM " + TABLE_STOPS + " WHERE " + Config.TAG_STOPLOAD + " = " + LoadNumber + " AND " + Config.TAG_STOPTYPE + " = 2 " +
                "ORDER BY " + Config.TAG_STOPEARLYDATE + " DESC, " + Config.TAG_STOPEARLYTIME + " DESC;";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.moveToFirst()) {
            Consignee = cursor.getString(5) + " - " + cursor.getString(8) + " - " + cursor.getString(9);
        }
        cursor.close();
        db.close();
        return Consignee;
    }

    Cursor getFirstShipperData(Integer LoadNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT " + Config.TAG_STOPLOAD + ", " + Config.TAG_STOPEARLYDATE + ", " + Config.TAG_STOPEARLYTIME + ", " +
                Config.TAG_STOPCUSTNAME + ", " + Config.TAG_STOPCUSTCITY + ", " + Config.TAG_STOPCUSTSTATE + " " +
                "FROM " + TABLE_STOPS + " WHERE " + Config.TAG_STOPTYPE + " = 1 AND " + Config.TAG_STOPLOAD + " = " + LoadNumber +
                " ORDER BY " + Config.TAG_STOPEARLYDATE + " ASC, " + Config.TAG_STOPEARLYTIME + " ASC LIMIT 1;",null);
    }

    Cursor getLastConsigneeData(Integer LoadNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT " + Config.TAG_STOPLOAD + ", " + Config.TAG_STOPEARLYDATE + ", " + Config.TAG_STOPEARLYTIME + ", " +
                Config.TAG_STOPCUSTNAME + ", " + Config.TAG_STOPCUSTCITY + ", " + Config.TAG_STOPCUSTSTATE + " " +
                "FROM " + TABLE_STOPS + " WHERE " + Config.TAG_STOPTYPE + " = 2 AND " + Config.TAG_STOPLOAD + " = " + LoadNumber +
                " ORDER BY " + Config.TAG_STOPEARLYDATE + " DESC, " + Config.TAG_STOPEARLYTIME + " DESC LIMIT 1;",null);
    }
}