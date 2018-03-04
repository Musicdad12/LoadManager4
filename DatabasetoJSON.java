package com.jrschugel.loadmanager;

import android.content.Context;
import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by seanm on 9/27/2017.
 * Copyright 2017. All rights reserved.
 */

class DatabasetoJSON {
    DatabaseHelper myDb;

    DatabasetoJSON(Context context) {
        myDb = new DatabaseHelper(context);
    }

    JSONObject getJSON() throws JSONException {
        Cursor item;
        JSONObject pl = new JSONObject();
        item = myDb.getItems();
        item.moveToFirst();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < item.getCount(); i++) {
            JSONObject val = new JSONObject();
            for (int k = 0; k < item.getColumnCount(); k++) {
                try {
                    val.put(item.getColumnName(i), item.getString(i));
                    jsonArray.put(val);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                pl.put(String.valueOf(i), jsonArray);
            }

            if (jsonArray.length() < 1) {
                pl.put(String.valueOf(i), new JSONArray());
            }

        }

        JSONObject result = new JSONObject();
        result.put("data", pl);
        return result;
    }
}
