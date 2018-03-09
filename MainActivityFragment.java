package com.jrschugel.loadmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivityFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    Context context;
    // Session Manager Class
    SessionManager session;
    // Database Helper classes
    DatabaseHelper myDb;
    DatabaseHelperStops myDb2;
    DatabaseHelperExpense myDb3;
    // Initialize array for safety messages
    String[] mMessageArray;
    ListView lstMessages;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_main_fragment, container, false);
        context = getActivity();

        // Start database helper classes
        myDb = new DatabaseHelper(context);
        myDb2 = new DatabaseHelperStops(context);
        myDb3 = new DatabaseHelperExpense(context);

        // Retrieve string array for safety messages
        mMessageArray = getResources().getStringArray(R.array.message_array);

        //Initializing TextViews
        TextView tvWelcome = rootView.findViewById(R.id.tvWelcome);
        lstMessages = rootView.findViewById(R.id.lvMessages);

        // Session class instance
        session = new SessionManager(context);
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
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        //Adding request to the queue
        requestQueue.add(stringRequest);
        // Display welcome message
        tvWelcome.setText(FullName);

        //Setup check for new loads and send notification
        LoadPreferences();
        updateMessageView(rootView);
        updateTextMessages(rootView);

        // Get new load count
        Integer NewLoads = myDb.NewLoadCount();
        TextView tvNewLoads = rootView.findViewById(R.id.tvNewLoads);
        // Display new load count
        if (NewLoads > 0) {
            String LoadCount = "You have " + NewLoads + " new loads";
            tvNewLoads.setText(LoadCount);
            tvNewLoads.setTextColor(Color.GREEN);
        } else {
            tvNewLoads.setText(R.string.no_new_load);
            tvNewLoads.setTextColor(Color.RED);
        }

        lstMessages.setEmptyView(rootView.findViewById(android.R.id.empty));
        return rootView;
    }

    private void updateMessageView(View v) {
        // Initialize textview for safety messages
        TextView textView = v.findViewById(R.id.randomMessageView);
        // Randomize the messages
        Random random = new Random();

        int maxIndex = mMessageArray.length;
        int generatedIndex = random.nextInt(maxIndex);
        // Display random message
        textView.setText(mMessageArray[generatedIndex]);
    }

    private void updateTextMessages(View v) {
        Cursor curMessages = myDb.getMessages();
        MessageListAdapter messageListAdapter = new MessageListAdapter(getActivity(), curMessages);
        lstMessages.setAdapter(messageListAdapter);
    }

    private void LoadPreferences() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);

        settings.registerOnSharedPreferenceChangeListener(MainActivityFragment.this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        //Creating a string request
        session = new SessionManager(context);

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
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void getLoadStops(final String LoadNumber) {
        //Creating a request queue
        RequestQueue requestQueueStops = Volley.newRequestQueue(context);


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
        DatabaseHelperStops myDb2 = new DatabaseHelperStops(context);
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
        DatabaseHelperExpense myDb3 = new DatabaseHelperExpense(context);
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

