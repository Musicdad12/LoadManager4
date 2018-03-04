package com.jrschugel.loadmanager;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

public class SessionManager {
    // Shared Preferences
    private SharedPreferences pref;

    // Editor for Shared preferences
    private Editor editor;

    // Context
    private Context _context;

    // Sharedpref file name
    private static final String PREF_NAME = "JRSchugelPref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    static final String KEY_NAME = "name";

    // Truck Number (make variable public to access from outside)
    static final String KEY_TRUCK = "truck";
    private static final String KEY_DISP_NAME = "dname";
    static final String KEY_DISP_PHONE = "dphone";
    static final String KEY_USERNAME = "username";
    private static final String KEY_SAVED_RESP = "loads";
    private static final String KEY_SAVED_FUELSTOPS = "fuelstops";

    private final String KEY_UNLIMITED = "filterUnlimited";
    private final String KEY_LIMITED = "filterLimited";
    private final String KEY_REEFER = "filterReefer";
    private final String KEY_LOVES = "filterLoves";
    private final String KEY_PILOT = "filterPilot";
    private final String KEY_FLYINGJ = "filterFlyingJ";
    private final String KEY_TA = "filterTA";
    private final String KEY_PETRO = "filterPetro";
    private final String KEY_KWIKTRIP = "filterKwikTrip";
    private final String KEY_QUIKTRIP = "filterQuikTrip";
    private final String KEY_INDEPENDENT = "filterIndependent";

    // Constructor
    public SessionManager(Context context){
        this._context = context;
        int PRIVATE_MODE = 0;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.apply();
    }

    /**
     * Create login session
     * */
    void createLoginSession(String name, String truck, String dname, String dphone, String username){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing info in pref
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_TRUCK, truck);
        editor.putString(KEY_DISP_NAME, dname);
        editor.putString(KEY_DISP_PHONE, dphone);
        editor.putString(KEY_USERNAME, username);

        // commit changes
        editor.commit();
    }

    void UpdateFuelStops(String response){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing info in pref
        editor.putString(KEY_SAVED_FUELSTOPS, response);

        // commit changes
        editor.commit();
    }

    void UpdateMapFilterSettings (String FilterKey, boolean Value) {

        editor.putString(FilterKey, String.valueOf(Value));

        // commit changes
        editor.commit();
    }
    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }

    /**
     * Get stored session data
     * */
    HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<>();
        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_TRUCK, pref.getString(KEY_TRUCK, null));
        user.put(KEY_DISP_NAME, pref.getString(KEY_DISP_NAME, null));
        user.put(KEY_DISP_PHONE, pref.getString(KEY_DISP_PHONE, null));
        user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));
        user.put(KEY_UNLIMITED, pref.getString(KEY_UNLIMITED, "True"));
        user.put(KEY_LIMITED, pref.getString(KEY_LIMITED, "True"));
        user.put(KEY_REEFER, pref.getString(KEY_REEFER, "True"));
        user.put(KEY_LOVES, pref.getString(KEY_LOVES, "True"));
        user.put(KEY_PILOT, pref.getString(KEY_PILOT, "True"));
        user.put(KEY_FLYINGJ, pref.getString(KEY_FLYINGJ, "True"));
        user.put(KEY_TA, pref.getString(KEY_TA, "True"));
        user.put(KEY_PETRO, pref.getString(KEY_PETRO, "True"));
        user.put(KEY_KWIKTRIP, pref.getString(KEY_KWIKTRIP, "True"));
        user.put(KEY_QUIKTRIP, pref.getString(KEY_QUIKTRIP, "True"));
        user.put(KEY_INDEPENDENT, pref.getString(KEY_INDEPENDENT, "True"));


        // return user
        return user;
    }

    /**
     * Clear session details
     * */
    void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Login Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}