package com.jrschugel.loadmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ProgrammingKnowledge on 4/3/2015.
 * Copyright 2017. All rights reserved.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Load.db";
    private static final String TABLE_NAME = "LOADS";
    private static final String TABLE_STOPS = "STOPS";
    private static final String TABLE_EXPENSES = "EXPENSES";
    private static final String TABLE_SCANS = "SCANS";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                Config.TAG_LOADNUMBER+" INTEGER,"+
                Config.TAG_CUSTPO+" TEXT," +
                Config.TAG_WEIGHT+" INTEGER," +
                Config.TAG_PIECES+" INTEGER," +
                Config.TAG_BOLNUMBER+" TEXT," +
                Config.TAG_TRLRNUMBER+" TEXT," +
                Config.TAG_DRVLOAD+" TEXT," +
                Config.TAG_DRVUNLOAD+" TEXT," +
                Config.TAG_LOADED+" INTEGER," +
                Config.TAG_EMPTY+" INTEGER," +
                Config.TAG_TEMPLOW+" INTEGER," +
                Config.TAG_TEMPHIGH+" INTEGER," +
                Config.TAG_PRELOADED+" TEXT," +
                Config.TAG_DROPHOOK+" TEXT," +
                Config.TAG_COMMENTS+" TEXT," +
                Config.TAG_STATUS+" TEXT," +
                Config.TAG_VIEWEDLOAD+" INTEGER);");

        db.execSQL("create table " + TABLE_STOPS +" ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                Config.TAG_STOPID + " INTEGER," +
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

        db.execSQL("create table " + TABLE_EXPENSES +" ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                Config.TAG_EXPLOAD + " INTEGER," +
                Config.TAG_EXPDESC + " TEXT, " +
                Config.TAG_EXPTYPE + " TEXT, " +
                Config.TAG_EXPPONUM + " INTEGER, " +
                Config.TAG_EXPGALLON + " DOUBLE, " +
                Config.TAG_EXPAMOUNT + " DOUBLE); " );

        db.execSQL("create table " + TABLE_SCANS +" ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                Config.TAG_SCANLOAD + " INTEGER," +
                Config.TAG_SCANDESC + " TEXT, " +
                Config.TAG_SCANURI + " TEXT) ; " );

        }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STOPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCANS);
        onCreate(db);
    }

    void insertData(Integer LoadNumber, String CustPO, String Weight, String Pieces, String BOLNumber, String TrlrNumber,
                    String DrvLoad, String DrvUnload, String Loaded, String Empty, String TempLow, String TempHigh,
                    String Preloaded, String DropHook, String Comments, Integer ViewedLoad) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.TAG_LOADNUMBER,LoadNumber);
        contentValues.put(Config.TAG_CUSTPO,CustPO);
        contentValues.put(Config.TAG_WEIGHT,Weight);
        contentValues.put(Config.TAG_PIECES,Pieces);
        contentValues.put(Config.TAG_BOLNUMBER,BOLNumber);
        contentValues.put(Config.TAG_TRLRNUMBER,TrlrNumber);
        contentValues.put(Config.TAG_DRVLOAD,DrvLoad);
        contentValues.put(Config.TAG_DRVUNLOAD,DrvUnload);
        contentValues.put(Config.TAG_LOADED,Loaded);
        contentValues.put(Config.TAG_EMPTY,Empty);
        contentValues.put(Config.TAG_TEMPLOW,TempLow);
        contentValues.put(Config.TAG_TEMPHIGH,TempHigh);
        contentValues.put(Config.TAG_PRELOADED,Preloaded);
        contentValues.put(Config.TAG_DROPHOOK,DropHook);
        contentValues.put(Config.TAG_COMMENTS,Comments);
        contentValues.put(Config.TAG_VIEWEDLOAD,ViewedLoad);
        db.insert(TABLE_NAME, null, contentValues);
    }

    Cursor getItems() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from "+TABLE_NAME,null);
    }


    Cursor getLoadData(Integer LoadNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from "+TABLE_NAME+" where "+Config.TAG_LOADNUMBER+" = "+LoadNumber,null);
    }

    String getStatus(String LoadNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select * from " + TABLE_NAME + " WHERE " + Config.TAG_LOADNUMBER + " = " + LoadNumber;
        //String sql = "select * from " + TABLE_NAME ;
        Cursor result = db.rawQuery(sql, null) ;
        result.moveToFirst();
        if (Integer.parseInt(result.getString(17)) == 1) {
            result.close();
            return "Viewed";
        } else {
            result.close();
            return "New";
        }
    }

    void updateViewedLoad(Integer LoadNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(Config.TAG_VIEWEDLOAD, 1);

        db.update(TABLE_NAME, contentValues, "LoadNumber = ?", new String[]{LoadNumber.toString()});
    }

    Boolean searchLoad(Integer LoadNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        Boolean checkLoad = false;
        Cursor cursor;
        String Query ="SELECT * FROM " + TABLE_NAME + " WHERE LoadNumber = "+LoadNumber;
        cursor = db.rawQuery(Query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                checkLoad = true;
            } while (cursor.moveToNext());

            cursor.close();
        }

        db.close();
        return checkLoad;
    }

    Integer NewLoadCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        Integer NewLoadCount =0;
        //String Query ="SELECT * FROM LOADS where ViewedLoad = 0";
        String Query ="SELECT count(*) FROM LOADS WHERE " + Config.TAG_VIEWEDLOAD + " = 0";
        cursor = db.rawQuery(Query, null);
        if (cursor.moveToFirst()) {
            NewLoadCount = cursor.getInt(0);
        }
        cursor.close();

        db.close();
        return NewLoadCount;
    }

    Integer NotificationLoadCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        Integer NewLoadCount =0;
        //String Query ="SELECT * FROM LOADS where ViewedLoad = 0";
        String Query ="SELECT count(*) FROM LOADS WHERE " + Config.TAG_VIEWEDLOAD + " = 2";
        cursor = db.rawQuery(Query, null);
        if (cursor.moveToFirst()) {
            NewLoadCount = cursor.getInt(0);
        }
        cursor.close();

        db.close();
        return NewLoadCount;
    }

    void putScanURI (Integer LoadNumber, String Description, String ScanURI) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.TAG_SCANLOAD,LoadNumber);
        contentValues.put(Config.TAG_SCANDESC,Description);
        contentValues.put(Config.TAG_SCANURI,ScanURI);
        db.insert(TABLE_SCANS, null, contentValues);
    }

    Cursor getScanURI (Integer LoadNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from " + TABLE_SCANS + " where " + Config.TAG_SCANLOAD + " = " + LoadNumber,null);
    }

    void changeTrailerNumber (String Loadnumber, String newTrailerNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = Config.TAG_LOADNUMBER + " = " + Loadnumber;
        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.TAG_TRLRNUMBER, newTrailerNumber);
        db.update(TABLE_NAME, contentValues, whereClause, null);
    }
}