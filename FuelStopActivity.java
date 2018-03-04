package com.jrschugel.loadmanager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class FuelStopActivity extends AppCompatActivity implements Spinner.OnItemSelectedListener {

    //JSON Array
    private JSONArray result;

    // Session Manager Class
    SessionManager session;


    //An ArrayList for Spinner Items
    private ArrayList<String> States;

    //Declaring an Spinner
    private Spinner spinState;
    public String FuelStopFilter;
    public String FuelStopState;

    GridView lstFuelStops;
    JSONAdapterFS jSONAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_stop);

        //Initializing the ArrayList
        States = new ArrayList<>();

        GridView lvFuelStops = findViewById(R.id.listview_FuelStops);

        session = new SessionManager(getApplicationContext());

        //Initializing Spinner
        spinState = findViewById(R.id.spinFuelStopState);
        //Spinner spinFilter1 = findViewById(R.id.spinFuelStopFilter);
        //Adding an Item Selected Listener to our Spinner
        //As we have implemented the class Spinner.OnItemSelectedListener to this class iteself we are passing this to setOnItemSelectedListener
        //spinFilter1.setOnItemSelectedListener(this);

        //This method will fetch the data from the URL
        getStates();
        final Spinner spinFilter = findViewById(R.id.spinFuelStopFilter);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.filter_fuelstops, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
        spinFilter.setAdapter(adapter2); // Apply the adapter to the spinner
        spinFilter.setOnItemSelectedListener(this);

        lvFuelStops.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /* Parameters
            parent:     The AdapterView where the click happened.
            view:       The view within the AdapterView that was clicked (this will be a view provided by the adapter)
            position:   The position of the view in the adapter.
            id:         The row id of the item that was clicked. */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //your code here
                TextView tvlongitude = view.findViewById(R.id.tvLongitude);
                TextView tvlatitude = view.findViewById(R.id.tvLatitude);
                TextView tvaddress = view.findViewById(R.id.tvTruckstopName);
                String longitude = tvlongitude.getText().toString();
                String latitude = tvlatitude.getText().toString();
                String address = tvaddress.getText().toString();
                String Coord = "geo:"+longitude+","+latitude+"?z=12&q="+address;
                Uri gmmIntentUri = Uri.parse(Coord);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        });
    }


    // Handle onselectitemlisteners for both spinners

    private void getStates() {
        //Creating a string request

        StringRequest stringRequest = new StringRequest(Config.DATA_STATES_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //Parsing the fetched Json String to JSON Object
                            JSONObject j = new JSONObject(response);
                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray(Config.JSON_ARRAY_STATE);
                            //Calling method getSettlements to get the Settlement Dates from the JSON Array
                            getStateSpinner(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void getStateSpinner(JSONArray j) {
        //Traversing through all the items in the json array
        for (int i = 0; i < j.length(); i++) {
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the Date of the settlement to array list
                States.add(json.getString(Config.TAG_STATE));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Setting adapter to show the items in the spinner
        spinState.setAdapter(new ArrayAdapter<>(FuelStopActivity.this, android.R.layout.simple_spinner_dropdown_item, States));
        spinState.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()){
            case R.id.spinFuelStopFilter:
                FuelStopFilter = parent.getSelectedItem().toString();
                //Toast.makeText(FuelStopActivity.this, FuelStopFilter + " " + FuelStopState, Toast.LENGTH_LONG).show();
                break;
            case R.id.spinFuelStopState:
                FuelStopState = parent.getSelectedItem().toString();
                //Toast.makeText(FuelStopActivity.this, FuelStopFilter + " " + FuelStopState, Toast.LENGTH_LONG).show();
                break;
        }

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonResponse = new JSONArray(response);
                    session = new SessionManager(getApplicationContext());
                    session.UpdateFuelStops(response);
                    //Initialize ListView
                    lstFuelStops= findViewById(R.id.listview_FuelStops);

                    jSONAdapter = new JSONAdapterFS (FuelStopActivity.this,jsonResponse);//jArray is your json array

                    //Set the above adapter as the adapter of choice for our list
                    lstFuelStops.setAdapter(jSONAdapter );

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        FuelStopRequest fuelstopRequest = new FuelStopRequest(FuelStopFilter, FuelStopState, responseListener);
        RequestQueue queue = Volley.newRequestQueue(FuelStopActivity.this);
        queue.add(fuelstopRequest);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(this, "You selected nothing", Toast.LENGTH_LONG).show();
    }

}
