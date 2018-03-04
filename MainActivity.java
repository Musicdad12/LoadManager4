package com.jrschugel.loadmanager;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import me.leolin.shortcutbadger.ShortcutBadger;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    // Session Manager Class
    SessionManager session;
    // Database Helper classes
    DatabaseHelper myDb;
    DatabaseHelperStops myDb2;
    DatabaseHelperExpense myDb3;
    // Initialize array for safety messages
    String[] mMessageArray;
    private NotificationUtils mNotificationUtils;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int MY_STORAGE_REQUEST_CODE = 120;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //cpd.start();

        //Start Google Cloud Messaging for Push Notifications
        System.out.println("MainActivity.OnCreate TOKEN: " + FirebaseInstanceId.getInstance().getToken());

        // Start database helper classes
        myDb = new DatabaseHelper(this);
        myDb2 = new DatabaseHelperStops(this);
        myDb3 = new DatabaseHelperExpense(this);

        mNotificationUtils = new NotificationUtils(this);

        // Retrieve string array for safety messages
        mMessageArray =   getResources().getStringArray(R.array.message_array);

        //Initializing TextViews
        TextView tvWelcome = findViewById(R.id.tvWelcome);

        // Session class instance
        session = new SessionManager(getApplicationContext());
        /*
         * Call this function whenever you want to check user login
         * This will redirect user to LoginActivity is he is not
         * logged in
         * */
        session.checkLogin();
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // Display welcome message and set user variables
        final String FullName = "Welcome " + user.get(SessionManager.KEY_NAME);
        final String TruckNumber = user.get(SessionManager.KEY_TRUCK);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //this code will be executed on devices running ICS or later

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            MY_CAMERA_REQUEST_CODE);
                }

                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_STORAGE_REQUEST_CODE);
                }
            }
        }
        // Start syncing remote database to local database
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.DATA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //Parsing the fetched Json String to JSON Object
                            JSONObject j = new JSONObject(response);
                            //Storing the Array of JSON String to our JSON Array
                            JSONArray result = j.getJSONArray(Config.JSON_ARRAY);
                            //Calling method getSettlements to get the Settlement Dates from the JSON Array
                            getLoadsRemote(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parameters = new HashMap<>();
                parameters.put("TruckNumber", TruckNumber);
                return parameters;
            }
        };

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding request to the queue
        requestQueue.add(stringRequest);
        // Display welcome message
        tvWelcome.setText(FullName);

        //Setup check for new loads and send notification
        LoadPreferences();
        // Initialize buttons
        final Button butLoads = findViewById(R.id.butLoadManager);
        final Button butSettlements = findViewById(R.id.butSettlement);
        final Button butFuelStops = findViewById(R.id.butFuelStops);
        final Button butAlarm = findViewById(R.id.butAlarm);
        final Button butAccident = findViewById(R.id.butAccident);
        final Button butReferral = findViewById(R.id.butReferral);
        final Button butNewsletter = findViewById(R.id.butNewsletter);
        final Button butFuture = findViewById(R.id.butFuture);

        //cpd.stop();

        butLoads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, xFragmentLayout.class);
                MainActivity.this.startActivity(intent);
            }
        });
        butSettlements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettlementActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        butFuelStops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        butAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AlarmActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        butAccident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AccidentActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        butReferral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ReferralActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        butNewsletter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewsletterActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        butFuture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoadEntryActivity.class);
                // SQLite veiwer page
                //Intent intent = new Intent(MainActivity.this, AndroidDatabaseManager.class);
                MainActivity.this.startActivity(intent);
            }
        });

    }

    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == MY_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Storage permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Storage permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }


    // Check if back button is pressed and confirm logout
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        MainActivity.super.onBackPressed();
                        session.logoutUser();
                    }
                }).create().show();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateMessageView();
        // Session class instance
        session = new SessionManager(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // retrieve user variables and display welcome message
        final String TruckNumber = user.get(SessionManager.KEY_TRUCK);
        TextView tvNewLoads = findViewById(R.id.tvNewLoads);
        // Initialize Database Helper class
        myDb = new DatabaseHelper(this);
        // Sync remote database with local database
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.DATA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //Parsing the fetched Json String to JSON Object
                            JSONObject j = new JSONObject(response);
                            //Storing the Array of JSON String to our JSON Array
                            JSONArray result = j.getJSONArray(Config.JSON_ARRAY);
                            //Calling method getSettlements to get the Settlement Dates from the JSON Array
                            getLoadsRemote(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parameters = new HashMap<>();
                parameters.put("TruckNumber", TruckNumber);
                return parameters;
            }
        };

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding request to the queue
        requestQueue.add(stringRequest);
        // Get number of new loads
        Integer NewLoads = myDb.NewLoadCount();
        if (NewLoads > 0) {
            // Display number of new loads found
            String LoadCount = "You have " + NewLoads + " new loads";
            tvNewLoads.setText(LoadCount);
            tvNewLoads.setTextColor(Color.GREEN);
        } else {
            // Display no new loads found
            tvNewLoads.setText(R.string.no_new_load);
            tvNewLoads.setTextColor(Color.RED);
        }
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder nb = mNotificationUtils.
                    getAndroidChannelNotification("New Load Information", "Check Load Manager for details");

            mNotificationUtils.getManager().notify(101, nb.build());
            if (NewLoads == 0) {
                mNotificationUtils.getManager().cancel(101);
            }
        } else {
            ShortcutBadger.applyCount(this, NewLoads); //for 1.1.4+
        }
    }
    private void updateMessageView() {
        // Initialize textview for safety messages
        TextView textView = findViewById(R.id.randomMessageView);
        // Randomize the messages
        Random random = new Random();

        int maxIndex = mMessageArray.length;
        int generatedIndex = random.nextInt(maxIndex);
        // Display random message
        textView.setText(mMessageArray[generatedIndex]);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_loads_actions, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public void CallPhone(String PhoneNumber) {
        int checkPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE);
        if (checkPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{android.Manifest.permission.CALL_PHONE},
                    1);
        } else {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + PhoneNumber));
            startActivity(callIntent);
        }
    }

    /**
     * On selecting action bar icons
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_call_dispatcher:
                // call current dispatcher action
                HashMap<String, String> user = session.getUserDetails();
                final String dispphone = user.get(SessionManager.KEY_DISP_PHONE);
                CallPhone(dispphone);
                return true;
            case R.id.action_call_schugel:
                // call main Schugel Number
                CallPhone("8003592900");
                return true;
            case R.id.action_logout:
                // refresh
                session.logoutUser();
                return true;
            case R.id.action_settings:
                // settings action
                Intent intent = new Intent(MainActivity.this, Preferences.class);
                startActivity(intent);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void LoadPreferences() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        settings.registerOnSharedPreferenceChangeListener(MainActivity.this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        //Creating a string request
        session = new SessionManager(getApplicationContext());

        LoadPreferences();

    }

    private void getLoadsRemote(JSONArray j) {
        //Traversing through all the items in the json array
        for (int i = 0; i < j.length(); i++) {
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                if (!myDb.searchLoad(Integer.parseInt(json.getString(Config.TAG_LOADNUMBER)))) {
                    myDb.insertData(json.getInt(Config.TAG_LOADNUMBER),
                            json.getString(Config.TAG_CUSTPO),
                            json.getString(Config.TAG_WEIGHT),
                            json.getString(Config.TAG_PIECES),
                            json.getString(Config.TAG_BOLNUMBER),
                            json.getString(Config.TAG_TRLRNUMBER),
                            json.getString(Config.TAG_DRVLOAD),
                            json.getString(Config.TAG_DRVUNLOAD),
                            json.getString(Config.TAG_LOADED),
                            json.getString(Config.TAG_EMPTY),
                            json.getString(Config.TAG_TEMPLOW),
                            json.getString(Config.TAG_TEMPHIGH),
                            json.getString(Config.TAG_PRELOADED),
                            json.getString(Config.TAG_DROPHOOK),
                            json.getString(Config.TAG_COMMENTS),
                            0);
                    getLoadStops(json.getString(Config.TAG_LOADNUMBER));

                    // Get new load count
                    Integer NewLoads = myDb.NewLoadCount();
                    TextView tvNewLoads = findViewById(R.id.tvNewLoads);
                    // Display new load count
                    if (NewLoads > 0) {
                        String LoadCount = "You have " + NewLoads + " new loads";
                        tvNewLoads.setText(LoadCount);
                        tvNewLoads.setTextColor(Color.GREEN);
                    } else {
                        tvNewLoads.setText(R.string.no_new_load);
                        tvNewLoads.setTextColor(Color.RED);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void getLoadStops(final String LoadNumber) {
        //Creating a request queue
        RequestQueue requestQueueStops = Volley.newRequestQueue(this);


        StringRequest stringRequestStops = new StringRequest(Request.Method.POST, Config.DATA_SAVE_STOPS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String StopsResponse) {

                        try {
                            //Parsing the fetched Json String to JSON Object
                            JSONObject k = new JSONObject(StopsResponse);
                            //Storing the Array of JSON String to our JSON Array
                            JSONArray result2 = k.getJSONArray(Config.JSON_ARRAY_STOPS);
                            //Calling method getSettlements to get the Settlement Dates from the JSON Array
                            saveLoadStops(result2);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error: ", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> Stopsparameters = new HashMap<>();
                Stopsparameters.put("LoadNumber", LoadNumber);
                return Stopsparameters;
            }
        };
        //Adding request to the queue
        requestQueueStops.add(stringRequestStops);
        StringRequest stringRequestExpenses = new StringRequest(Request.Method.POST, Config.DATA_GET_EXPENSES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ExpenseResponse) {

                        try {
                            //Parsing the fetched Json String to JSON Object
                            JSONObject k = new JSONObject(ExpenseResponse);
                            //Storing the Array of JSON String to our JSON Array
                            JSONArray result2 = k.getJSONArray(Config.JSON_ARRAY_EXPENSES);
                            //Calling method getSettlements to get the Settlement Dates from the JSON Array
                            saveLoadExpenses(result2);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error: ", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> Expenseparameters = new HashMap<>();
                Expenseparameters.put("LoadNumber", LoadNumber);
                return Expenseparameters;
            }
        };
        //Adding request to the queue
        requestQueueStops.add(stringRequestExpenses);
    }

    private void saveLoadStops(JSONArray k) {
        DatabaseHelperStops myDb2 = new DatabaseHelperStops(this);
        //Traversing through all the items in the json array
        for (int i = 0; i < k.length(); i++) {
            try {
                //Getting json object
                JSONObject json = k.getJSONObject(i);

                myDb2.AddStop(json.getInt(Config.TAG_LOADNUMBER),
                            json.getInt(Config.TAG_STOPTYPE),
                            json.getInt(Config.TAG_STOPCUSTID),
                            json.getString(Config.TAG_STOPCUSTNAME),
                            json.getString("StopCustomerAddr"),
                            json.getString("StopCustomerAddr2"),
                            json.getString(Config.TAG_STOPCUSTCITY),
                            json.getString(Config.TAG_STOPCUSTSTATE),
                            json.getString(Config.TAG_STOPCUSTPHONE),
                            json.getString(Config.TAG_STOPCUSTCONTACT),
                            json.getString(Config.TAG_STOPEARLYDATE),
                            json.getString(Config.TAG_STOPEARLYTIME),
                            json.getString(Config.TAG_STOPLATEDATE),
                            json.getString(Config.TAG_STOPLATETIME));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveLoadExpenses(JSONArray k) {
        DatabaseHelperExpense myDb3 = new DatabaseHelperExpense(this);
        //Traversing through all the items in the json array
        for (int i = 0; i < k.length(); i++) {
            try {
                //Getting json object
                JSONObject json = k.getJSONObject(i);

                String mySQLType = null;
                switch (json.getString(Config.TAG_EXPTYPE)) {
                    case "FC":
                        mySQLType = "Fuel Card";
                        break;
                    case "CA":
                        mySQLType = "Cash/Comchek";
                        break;
                    case "OC":
                        mySQLType = "Open Charge";
                        break;
                    case "TF":
                        mySQLType = "Terminal Fuel";
                        break;
                }
                myDb3.AddExpense(json.getInt(Config.TAG_EXPLOAD),
                        json.getString(Config.TAG_EXPDESC),
                        mySQLType,
                        json.getString(Config.TAG_EXPPONUM),
                        json.getString(Config.TAG_EXPGALLON),
                        json.getString(Config.TAG_EXPAMOUNT));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }
}

