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
    private static final String TABLE_MESSAGES = "Messages";
    private static final String TABLE_ACCIDENTS = "Accidents";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Config.TAG_LOADNUMBER + " INTEGER," +
                Config.TAG_CUSTPO + " TEXT," +
                Config.TAG_WEIGHT + " INTEGER," +
                Config.TAG_PIECES + " INTEGER," +
                Config.TAG_BOLNUMBER + " TEXT," +
                Config.TAG_TRLRNUMBER + " TEXT," +
                Config.TAG_DRVLOAD + " TEXT," +
                Config.TAG_DRVUNLOAD + " TEXT," +
                Config.TAG_LOADED + " INTEGER," +
                Config.TAG_EMPTY + " INTEGER," +
                Config.TAG_TEMPLOW + " INTEGER," +
                Config.TAG_TEMPHIGH + " INTEGER," +
                Config.TAG_PRELOADED + " TEXT," +
                Config.TAG_DROPHOOK + " TEXT," +
                Config.TAG_COMMENTS + " TEXT," +
                Config.TAG_STATUS + " TEXT," +
                Config.TAG_VIEWEDLOAD + " INTEGER);");

        db.execSQL("create table " + TABLE_STOPS + " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
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

        db.execSQL("create table " + TABLE_EXPENSES + " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Config.TAG_EXPLOAD + " INTEGER," +
                Config.TAG_EXPDESC + " TEXT, " +
                Config.TAG_EXPTYPE + " TEXT, " +
                Config.TAG_EXPPONUM + " INTEGER, " +
                Config.TAG_EXPGALLON + " DOUBLE, " +
                Config.TAG_EXPAMOUNT + " DOUBLE); ");

        db.execSQL("create table " + TABLE_SCANS + " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Config.TAG_SCANLOAD + " INTEGER," +
                Config.TAG_SCANDESC + " TEXT, " +
                Config.TAG_SCANURI + " TEXT) ; ");

        db.execSQL("create table " + TABLE_MESSAGES + " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Config.TAG_MESSTIMEDATE + " TEXT," +
                Config.TAG_MESSSENDER + " TEXT, " +
                Config.TAG_MESSTEXT + " TEXT) ; ");

        db.execSQL("create table " + TABLE_ACCIDENTS + " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Config.TAG_EXON_SIGNATURE + " BLOB," +
                Config.TAG_EXON_WITNESS + " BLOB," +
                Config.TAG_EXON_ADDRESS + " TEXT," +
                Config.TAG_ACCDRVRNAME + " TEXT," +
                Config.TAG_ACCTRUCKNUMBER + " TEXT," +
                Config.TAG_ACCTRLRNUMBER + " TEXT," +
                Config.TAG_ACCLOADNUMBER + " TEXT," +
                Config.TAG_ACCINJURED1NAME + " TEXT," +
                Config.TAG_ACCINJURED1ADDRESS + " TEXT," +
                Config.TAG_ACCINJURED1PHONE + " TEXT," +
                Config.TAG_ACCINJURED1AGE + " TEXT," +
                Config.TAG_ACCINJURED2NAME + " TEXT," +
                Config.TAG_ACCINJURED2ADDRESS + " TEXT," +
                Config.TAG_ACCINJURED2PHONE + " TEXT," +
                Config.TAG_ACCINJURED2AGE + " TEXT," +
                Config.TAG_ACCINJURED3NAME + " TEXT," +
                Config.TAG_ACCINJURED3ADDRESS + " TEXT," +
                Config.TAG_ACCINJURED3PHONE + " TEXT," +
                Config.TAG_ACCINJURED3AGE + " TEXT," +
                Config.TAG_ACCINJURED4NAME + " TEXT," +
                Config.TAG_ACCINJURED4ADDRESS + " TEXT," +
                Config.TAG_ACCINJURED4PHONE + " TEXT," +
                Config.TAG_ACCINJURED4AGE + " TEXT," +
                Config.TAG_ACCPROPNAME + " TEXT," +
                Config.TAG_ACCPROPADDRESS + " TEXT," +
                Config.TAG_ACCPROPDESC + " TEXT," +
                Config.TAG_ACCWITNESSNAME1 + " TEXT," +
                Config.TAG_ACCWITNESSADDR1 + " TEXT," +
                Config.TAG_ACCWITNESSPHONE1 + " TEXT," +
                Config.TAG_ACCWITNESSNAME2 + " TEXT," +
                Config.TAG_ACCWITNESSADDR2 + " TEXT," +
                Config.TAG_ACCWITNESSPHONE2 + " TEXT," +
                Config.TAG_ACCWITNESSNAME3 + " TEXT," +
                Config.TAG_ACCWITNESSADDR3 + " TEXT," +
                Config.TAG_ACCWITNESSPHONE3 + " TEXT," +
                Config.TAG_ACCACCIDENTDATE + " TEXT," +
                Config.TAG_ACCACCIDENTTIME + " TEXT," +
                Config.TAG_ACCACCIDENTLOCATION + " TEXT," +
                Config.TAG_ACCDRV1NAME + " TEXT," +
                Config.TAG_ACCDRV1LICENSE + " TEXT," +
                Config.TAG_ACCDRV1ADDRESS + " TEXT," +
                Config.TAG_ACCDRV1PLATE + " TEXT," +
                Config.TAG_ACCDRV1YRMAKE + " TEXT," +
                Config.TAG_ACCDRV1OWNER + " TEXT," +
                Config.TAG_ACCDRV1OWNERADDRESS + " TEXT," +
                Config.TAG_ACCDRV1OWNERPHONE + " TEXT," +
                Config.TAG_ACCDRV1INSURANCENAME + " TEXT," +
                Config.TAG_ACCDRV1INSPOLICYNUMBER + " TEXT," +
                Config.TAG_ACCDRV2NAME + " TEXT," +
                Config.TAG_ACCDRV2LICENSE + " TEXT," +
                Config.TAG_ACCDRV2ADDRESS + " TEXT," +
                Config.TAG_ACCDRV2PLATE + " TEXT," +
                Config.TAG_ACCDRV2YRMAKE + " TEXT," +
                Config.TAG_ACCDRV2OWNER + " TEXT," +
                Config.TAG_ACCDRV2OWNERADDRESS + " TEXT," +
                Config.TAG_ACCDRV2OWNERPHONE + " TEXT," +
                Config.TAG_ACCDRV2INSURANCENAME + " TEXT," +
                Config.TAG_ACCDRV2INSPOLICYNUMBER + " TEXT," +
                Config.TAG_ACCPOLICEDEPT + " TEXT," +
                Config.TAG_ACCOFFICERNAME + " TEXT," +
                Config.TAG_ACCOFFICERBADGE + " TEXT," +
                Config.TAG_ACCOFFICERPHONE + " TEXT," +
                Config.TAG_ACCCITATION + " TEXT," +
                Config.TAG_ACCREPORTMADE + " INTEGER," +
                Config.TAG_ACCREPORTNUMBER + " TEXT," +
                Config.TAG_ACCDRAWING + " BLOB," +
                Config.TAG_ACCWEATHER + " TEXT," +
                Config.TAG_ACCNARRATIVE + " TEXT) ; ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STOPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCANS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCIDENTS);
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

    void saveMessage (String DateTime, String Sender, String Message) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.TAG_MESSTIMEDATE, DateTime);
        contentValues.put(Config.TAG_MESSSENDER, Sender);
        contentValues.put(Config.TAG_MESSTEXT, Message);
        db.insert(TABLE_MESSAGES, null, contentValues);
    }

    Cursor getMessages () {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_MESSAGES + " ORDER BY " + Config.TAG_MESSTIMEDATE + " ASC LIMIT 20",null);
    }

    void DeleteMessage (Long MessageID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MESSAGES, "_id = ?", new String[]{Long.toString(MessageID)});
    }

    long saveAccident () {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.TAG_ACCDRVRNAME, " ");
        return db.insert(TABLE_ACCIDENTS, null, contentValues);
    }

    long saveAccidentPage2 (Long AccidentID, byte[] Signature, byte[] Witness, String Address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.TAG_EXON_SIGNATURE, Signature);
        contentValues.put(Config.TAG_EXON_WITNESS, Witness);
        contentValues.put(Config.TAG_EXON_ADDRESS, Address);
        return db.update(TABLE_ACCIDENTS,  contentValues, "_ID = ?", new String[] {AccidentID.toString()});
    }
    long saveAccidentPage3 (Long AccidentID, String DrvName, String TruckNumber, String TrailerNumber, String LoadNumber, 
                            String InjuredName1, String InjuredAddress1, String InjuredPhone1, String InjuredAge1,
                            String InjuredName2, String InjuredAddress2, String InjuredPhone2, String InjuredAge2,
                            String InjuredName3, String InjuredAddress3, String InjuredPhone3, String InjuredAge3,
                            String InjuredName4, String InjuredAddress4, String InjuredPhone4, String InjuredAge4) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.TAG_ACCDRVRNAME, DrvName);
        contentValues.put(Config.TAG_ACCTRUCKNUMBER, TruckNumber);
        contentValues.put(Config.TAG_ACCTRLRNUMBER, TrailerNumber);
        contentValues.put(Config.TAG_ACCLOADNUMBER, LoadNumber);
        contentValues.put(Config.TAG_ACCINJURED1NAME, InjuredName1);
        contentValues.put(Config.TAG_ACCINJURED1ADDRESS, InjuredAddress1);
        contentValues.put(Config.TAG_ACCINJURED1PHONE, InjuredPhone1);
        contentValues.put(Config.TAG_ACCINJURED1AGE, InjuredAge1);
        contentValues.put(Config.TAG_ACCINJURED2NAME, InjuredName2);
        contentValues.put(Config.TAG_ACCINJURED2ADDRESS, InjuredAddress2);
        contentValues.put(Config.TAG_ACCINJURED2PHONE, InjuredPhone2);
        contentValues.put(Config.TAG_ACCINJURED2AGE, InjuredAge2);
        contentValues.put(Config.TAG_ACCINJURED3NAME, InjuredName3);
        contentValues.put(Config.TAG_ACCINJURED3ADDRESS, InjuredAddress3);
        contentValues.put(Config.TAG_ACCINJURED3PHONE, InjuredPhone3);
        contentValues.put(Config.TAG_ACCINJURED3AGE, InjuredAge3);
        contentValues.put(Config.TAG_ACCINJURED4NAME, InjuredName4);
        contentValues.put(Config.TAG_ACCINJURED4ADDRESS, InjuredAddress4);
        contentValues.put(Config.TAG_ACCINJURED4PHONE, InjuredPhone4);
        contentValues.put(Config.TAG_ACCINJURED4AGE, InjuredAge4);
        return db.update(TABLE_ACCIDENTS, contentValues, "_id = ?", new String[] {AccidentID.toString()});
    }
    long saveAccidentPage4 (Long AccidentID, String propName, String propAddr, String propDamaged, String WitName1, String WitAddr1, String WitPhone1,
                            String WitName2, String WitAddr2, String WitPhone2, String WitName3, String WitAddr3, String WitPhone3) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.TAG_ACCPROPNAME, propName);
        contentValues.put(Config.TAG_ACCPROPADDRESS, propAddr);
        contentValues.put(Config.TAG_ACCPROPDESC, propDamaged);
        contentValues.put(Config.TAG_ACCWITNESSNAME1, WitName1);
        contentValues.put(Config.TAG_ACCWITNESSADDR1, WitAddr1);
        contentValues.put(Config.TAG_ACCWITNESSPHONE1, WitPhone1);
        contentValues.put(Config.TAG_ACCWITNESSNAME2, WitName2);
        contentValues.put(Config.TAG_ACCWITNESSADDR2, WitAddr2);
        contentValues.put(Config.TAG_ACCWITNESSPHONE2, WitPhone2);
        contentValues.put(Config.TAG_ACCWITNESSNAME3, WitName3);
        contentValues.put(Config.TAG_ACCWITNESSADDR3, WitAddr3);
        contentValues.put(Config.TAG_ACCWITNESSPHONE3, WitPhone3);
        return db.update(TABLE_ACCIDENTS, contentValues, "_id = ?", new String[] {AccidentID.toString()});
    }
    long saveAccidentPage5 (Long AccidentID, String accDate, String accTime, String accLocation, String Drvr2License, String Drvr2Name, String Drvr2Addr,
                            String Drvr2Plate, String Drvr2YrMake, String Drvr2Owner, String Drvr2OwnerAddr, String Drvr2OwnerPhone, 
                            String Drvr2InsName, String Drvr2InsPolicy, String Drvr3Name, String Drvr3License, String Drvr3Addr, String Drvr3Plate, String Drvr3YrMake,
                            String Drvr3Owner, String Drvr3OwnerAddr, String Drvr3OwnerPhone, String Drvr3InsName, String Drvr3InsPolicy) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.TAG_ACCACCIDENTDATE, accDate);
        contentValues.put(Config.TAG_ACCACCIDENTTIME, accTime);
        contentValues.put(Config.TAG_ACCACCIDENTLOCATION, accLocation);
        contentValues.put(Config.TAG_ACCDRV1NAME, Drvr2Name);
        contentValues.put(Config.TAG_ACCDRV1LICENSE, Drvr2License);
        contentValues.put(Config.TAG_ACCDRV1ADDRESS, Drvr2Addr);
        contentValues.put(Config.TAG_ACCDRV1PLATE, Drvr2Plate);
        contentValues.put(Config.TAG_ACCDRV1YRMAKE, Drvr2YrMake);
        contentValues.put(Config.TAG_ACCDRV1OWNER, Drvr2Owner);
        contentValues.put(Config.TAG_ACCDRV1OWNERADDRESS, Drvr2OwnerAddr);
        contentValues.put(Config.TAG_ACCDRV1OWNERPHONE, Drvr2OwnerPhone);
        contentValues.put(Config.TAG_ACCDRV1INSURANCENAME, Drvr2InsName);
        contentValues.put(Config.TAG_ACCDRV1INSPOLICYNUMBER, Drvr2InsPolicy);
        contentValues.put(Config.TAG_ACCDRV2NAME, Drvr3Name);
        contentValues.put(Config.TAG_ACCDRV2LICENSE, Drvr3License);
        contentValues.put(Config.TAG_ACCDRV2ADDRESS, Drvr3Addr);
        contentValues.put(Config.TAG_ACCDRV2PLATE, Drvr3Plate);
        contentValues.put(Config.TAG_ACCDRV2YRMAKE, Drvr3YrMake);
        contentValues.put(Config.TAG_ACCDRV2OWNER, Drvr3Owner);
        contentValues.put(Config.TAG_ACCDRV2OWNERADDRESS, Drvr3OwnerAddr);
        contentValues.put(Config.TAG_ACCDRV2OWNERPHONE, Drvr3OwnerPhone);
        contentValues.put(Config.TAG_ACCDRV2INSURANCENAME, Drvr3InsName);
        contentValues.put(Config.TAG_ACCDRV2INSPOLICYNUMBER, Drvr3InsPolicy);
        
        return db.update(TABLE_ACCIDENTS, contentValues, "_id = ?", new String[] {AccidentID.toString()});
    }
    long saveAccidentPage6 (Long AccidentID, String police, String offName, String offBadge, String offPhone, String citation, Integer reportYN, 
                            String reportNo, byte[] sketch) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.TAG_ACCPOLICEDEPT, police);
        contentValues.put(Config.TAG_ACCOFFICERNAME, police);
        contentValues.put(Config.TAG_ACCOFFICERBADGE, police);
        contentValues.put(Config.TAG_ACCOFFICERPHONE, police);
        contentValues.put(Config.TAG_ACCCITATION, police);
        contentValues.put(Config.TAG_ACCREPORTMADE, police);
        contentValues.put(Config.TAG_ACCREPORTNUMBER, police);
        contentValues.put(Config.TAG_ACCDRAWING, police);
        return db.update(TABLE_ACCIDENTS, contentValues, "_id = ?", new String[] {AccidentID.toString()});
    }
    long saveAccidentPage7 (Long AccidentID, String weather, String narrative) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.TAG_ACCWEATHER, weather);
        contentValues.put(Config.TAG_ACCNARRATIVE, narrative);
        return db.update(TABLE_ACCIDENTS, contentValues, "_id = ?", new String[] {AccidentID.toString()});
    }

    Cursor getAccidents (Long AccidentID) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_ACCIDENTS + " WHERE _id = " + AccidentID,null);
    }

    Cursor getAllAccidents () {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_ACCIDENTS ,null);
    }

    void DeleteAccident (long AccidentID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ACCIDENTS, "_id = ?", new String[]{Long.toString(AccidentID)});
    }
}