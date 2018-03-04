package com.jrschugel.loadmanager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SettlementActivity extends Activity implements Spinner.OnItemSelectedListener {


    //Declaring an Spinner
    private Spinner spinSettlements;

    //An ArrayList for Spinner Items
    private ArrayList<String> Settlements;

    //JSON Array
    private JSONArray result;

    // Session Manager Class
    SessionManager session;

    /** Called when the activity is first created. */

    ListView lstTest;
    ListView lstTest2;
    ListView lstTest3;
    ListView lstTest4;
    ListView lstTest5;
    ListView lstTest6;

    //Array Adapter that will hold our ArrayList and display the items on the ListView
    JSONAdapter jSONAdapter ;
    JSONAdapter2 jSONAdapter2 ;
    JSONAdapter3 jSONAdapter3 ;
    JSONAdapter4 jSONAdapter4 ;
    JSONAdapter5 jSONAdapter5 ;
    JSONAdapter6 jSONAdapter6 ;

    private TextView textViewUsername;
    private TextView textViewName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settlement);

        textViewUsername = findViewById(R.id.username);
        textViewName = findViewById(R.id.name);

        //Initializing the ArrayList
        Settlements = new ArrayList<>();

        //Initializing Spinner
        spinSettlements = findViewById(R.id.spinSettlements);

        // Session class instance
        session = new SessionManager(getApplicationContext());
        /*
         * Call this function whenever you want to check user login
         * This will redirect user to LoginActivity is he is not
         * logged in
         */
        session.checkLogin();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // Put session data in variables

        final String username = user.get(SessionManager.KEY_USERNAME);
        final String name = user.get(SessionManager.KEY_NAME);

        textViewUsername.setText(username);
        textViewName.setText(name);

        //Adding an Item Selected Listener to our Spinner
        //As we have implemented the class Spinner.OnItemSelectedListener to this class iteself we are passing this to setOnItemSelectedListener
        spinSettlements.setOnItemSelectedListener(this);

        //This method will fetch the data from the URL
        getData(username);
    }

    private void getData(final String username) {
        //Creating a string request

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.DATA_SETTLEMENTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //Parsing the fetched Json String to JSON Object
                            JSONObject j = new JSONObject(response);
                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray(Config.JSON_ARRAY_SETTLEMENT);
                            //Calling method getSettlements to get the Settlement Dates from the JSON Array
                            getSettlements(result);
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
                parameters.put("username", username);
                return parameters;
            }};


        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void getSettlements(JSONArray j) {
        //Traversing through all the items in the json array
        for (int i = 0; i < j.length(); i++) {
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the Date of the settlement to array list
                Settlements.add(json.getString(Config.TAG_SETTLEMENTS));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Setting adapter to show the items in the spinner
        spinSettlements.setAdapter(new ArrayAdapter<>(SettlementActivity.this, R.layout.my_spinner, Settlements));

    }

    //this method will execute when we pic an item from the spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        final String SettleDate = parent.getItemAtPosition(position).toString();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonResponse = new JSONArray(response);

                    //Initialize ListView
                    lstTest= findViewById(R.id.listView_Settlement_1);

                    jSONAdapter = new JSONAdapter (SettlementActivity.this,jsonResponse);//jArray is your json array

                    //Set the above adapter as the adapter of choice for our list
                    lstTest.setAdapter(jSONAdapter );

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Response.Listener<String> responseListener2 = new Response.Listener<String>() {
            @Override
            public void onResponse(String response2) {
                try {
                    JSONArray jsonResponse2 = new JSONArray(response2);

                    //Initialize ListView
                    lstTest2= findViewById(R.id.listView_Settlement_2);

                    jSONAdapter2 = new JSONAdapter2 (SettlementActivity.this,jsonResponse2);//jArray is your json array

                    //Set the above adapter as the adapter of choice for our list
                    lstTest2.setAdapter(jSONAdapter2 );

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Response.Listener<String> responseListener3 = new Response.Listener<String>() {
            @Override
            public void onResponse(String response3) {
                try {
                    JSONArray jsonResponse3 = new JSONArray(response3);

                    //Initialize ListView
                    lstTest3= findViewById(R.id.listView_Settlement_3);

                    jSONAdapter3 = new JSONAdapter3 (SettlementActivity.this,jsonResponse3);//jArray is your json array

                    //Set the above adapter as the adapter of choice for our list
                    lstTest3.setAdapter(jSONAdapter3 );

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Response.Listener<String> responseListener4 = new Response.Listener<String>() {
            @Override
            public void onResponse(String response4) {
                try {
                    JSONArray jsonResponse4 = new JSONArray(response4);

                    //Initialize ListView
                    lstTest4= findViewById(R.id.listView_Settlement_4);

                    jSONAdapter4 = new JSONAdapter4 (SettlementActivity.this,jsonResponse4);//jArray is your json array

                    //Set the above adapter as the adapter of choice for our list
                    lstTest4.setAdapter(jSONAdapter4 );

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Response.Listener<String> responseListener5 = new Response.Listener<String>() {
            @Override
            public void onResponse(String response5) {
                try {
                    JSONArray jsonResponse5 = new JSONArray(response5);

                    //Initialize ListView
                    lstTest5= findViewById(R.id.listView_Settlement_5);

                    jSONAdapter5 = new JSONAdapter5 (SettlementActivity.this,jsonResponse5);//jArray is your json array

                    //Set the above adapter as the adapter of choice for our list
                    lstTest5.setAdapter(jSONAdapter5 );

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Response.Listener<String> responseListener6 = new Response.Listener<String>() {
            @Override
            public void onResponse(String response6) {
                try {
                    JSONArray jsonResponse6 = new JSONArray(response6);

                    //Initialize ListView
                    lstTest6= findViewById(R.id.listView_Settlement_6);

                    jSONAdapter6 = new JSONAdapter6 (SettlementActivity.this,jsonResponse6);//jArray is your json array

                    //Set the above adapter as the adapter of choice for our list
                    lstTest6.setAdapter(jSONAdapter6 );

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        SettlementRequest settlementRequest = new SettlementRequest(SettleDate, responseListener);
        RequestQueue queue = Volley.newRequestQueue(SettlementActivity.this);
        queue.add(settlementRequest);
        SettlementRequest2 settlementRequest2 = new SettlementRequest2(SettleDate, responseListener2);
        queue.add(settlementRequest2);
        SettlementRequest3 settlementRequest3 = new SettlementRequest3(SettleDate, responseListener3);
        queue.add(settlementRequest3);
        SettlementRequest settlementRequest4 = new SettlementRequest(SettleDate, responseListener4);
        queue.add(settlementRequest4);
        SettlementRequest5 settlementRequest5 = new SettlementRequest5(SettleDate, responseListener5);
        queue.add(settlementRequest5);
        SettlementRequest6 settlementRequest6 = new SettlementRequest6(SettleDate, responseListener6);
        queue.add(settlementRequest6);
    }

    //When no item is selected this method would execute
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

