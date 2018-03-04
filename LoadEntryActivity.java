package com.jrschugel.loadmanager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LoadEntryActivity extends AppCompatActivity implements AddCustomerDialog.AddCustomerListener{
    private EditText etLoadNumber, etPUEarlyDate, etPUEarlyTime, etPULateDate, etPULateTime;
    private EditText etPUNumber, etWeight, etPieces, etBOLNumber, etTrlrNumber;
    private Spinner spinShipperName, spinConsigneeName;
    private EditText etDelEarlyDate, etDelEarlyTime, etDelLateDate, etDelLateTime;
    private EditText etLoadedMiles, etEmptyMiles, etTempLow, etTempHigh, etComments;
    private TextView tvShipperAddr1, tvShipperAddr2, tvShipperCity, tvShipperState, tvShipperPhone, tvShipperContact, tvShipperComment;
    private TextView tvConsigneeAddr1, tvConsigneeAddr2, tvConsigneeCity, tvConsigneeState, tvConsigneePhone, tvConsigneeContact, tvConsigneeComment;
    private CheckBox chkDrvLoad, chkDrvUnload, chkPreload, chkDropHook;
    private Button butSave, butAddCustomer;
    private RequestQueue requestQueue;
    private String ShipperID, ConsigneeID;
    Calendar dateSelected = Calendar.getInstance();
    Calendar timeSelected = Calendar.getInstance();
    private DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormatter;
    SimpleDateFormat timeFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_entry);

        requestQueue = Volley.newRequestQueue(this);
        dateFormatter = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        timeFormatter = new SimpleDateFormat("HH:mm a", Locale.US);

        etLoadNumber = findViewById(R.id.etLoadNumber);
        spinShipperName = findViewById(R.id.spinShipper);
        butAddCustomer = findViewById(R.id.butAddCustomer);
        tvShipperAddr1 = findViewById(R.id.tvShipperAddr1);
        tvShipperAddr2 = findViewById(R.id.tvShipperAddr2);
        tvShipperCity = findViewById(R.id.tvShipperCity);
        tvShipperState = findViewById(R.id.tvShipperState);
        tvShipperPhone = findViewById(R.id.tvShipperPhone);
        tvShipperContact = findViewById(R.id.tvShipperContact);
        etPUEarlyDate = findViewById(R.id.etLoadEarlyDate);
        etPUEarlyTime = findViewById(R.id.etLoadEarlyTime);
        etPULateDate = findViewById(R.id.etLoadLateDate);
        etPULateTime = findViewById(R.id.etLoadLateTime);
        etPUNumber = findViewById(R.id.etPUNumber);
        etWeight = findViewById(R.id.etWeight);
        etPieces = findViewById(R.id.etPieces);
        etBOLNumber = findViewById(R.id.etBOLNumber);
        etTrlrNumber = findViewById(R.id.etTrailer);
        spinConsigneeName = findViewById(R.id.spinConsignee);
        tvConsigneeAddr1 = findViewById(R.id.tvConsigneeAddr1);
        tvConsigneeAddr2 = findViewById(R.id.tvConsigneeAddr2);
        tvConsigneeCity = findViewById(R.id.tvConsigneeCity);
        tvConsigneeState = findViewById(R.id.tvConsigneeState);
        tvConsigneePhone = findViewById(R.id.tvConsigneePhone);
        tvConsigneeContact = findViewById(R.id.tvConsigneeContact);
        etDelEarlyDate = findViewById(R.id.etDelEarlyDate);
        etDelEarlyTime = findViewById(R.id.etDelEarlyTime);
        etDelLateDate = findViewById(R.id.etDelLateDate);
        etDelLateTime = findViewById(R.id.etDelLateTime);
        chkDrvLoad = findViewById(R.id.chkDriverLoad);
        chkDrvUnload = findViewById(R.id.chkDriverUnload);
        etLoadedMiles = findViewById(R.id.etLoaded);
        etEmptyMiles = findViewById(R.id.etEmpty);
        etTempLow = findViewById(R.id.etTempLow);
        etTempHigh = findViewById(R.id.etTempHigh);
        chkPreload = findViewById(R.id.chkPreload);
        chkDropHook = findViewById(R.id.chkDropHook);
        etComments = findViewById(R.id.etComments);
        butSave = findViewById(R.id.butSave);

        //Fill spinners
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.DATA_GET_CUSTOMERS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //Parsing the fetched Json String to JSON Object
                            JSONObject j = new JSONObject(response);
                            //Storing the Array of JSON String to our JSON Array
                            JSONArray result = j.getJSONArray(Config.JSON_ARRAY_CUSTOMERS);
                            //Calling method getSettlements to get the Settlement Dates from the JSON Array
                            fillSpinners(result);
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

                return parameters;
            }
        };
        requestQueue.add(stringRequest);

        butAddCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenDialog();
            }
        });

        spinShipperName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.DATA_GET_CUSTOMERS,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    //Parsing the fetched Json String to JSON Object
                                    JSONObject j = new JSONObject(response);
                                    //Storing the Array of JSON String to our JSON Array
                                    JSONArray result = j.getJSONArray(Config.JSON_ARRAY_CUSTOMERS);
                                    //Calling method getSettlements to get the Settlement Dates from the JSON Array
                                    getCustomer(result, 1);
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

                        return parameters;
                    }
                };
                requestQueue.add(stringRequest);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinConsigneeName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.DATA_GET_CUSTOMERS,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    //Parsing the fetched Json String to JSON Object
                                    JSONObject j = new JSONObject(response);
                                    //Storing the Array of JSON String to our JSON Array
                                    JSONArray result = j.getJSONArray(Config.JSON_ARRAY_CUSTOMERS);
                                    //Calling method getSettlements to get the Settlement Dates from the JSON Array
                                    getCustomer(result, 2);
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

                        return parameters;
                    }
                };
                requestQueue.add(stringRequest);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        etPUEarlyDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPUEarlyDateField();
            }
        });

        etPULateDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPULateDateField();
            }
        });

        etDelEarlyDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDelEarlyDateField();
            }
        });

        etDelLateDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDelLateDateField();
            }
        });

        etPUEarlyTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPUEarlyTimeField();
            }
        });

        etPULateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPULateTimeField();
            }
        });

        etDelEarlyTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDelEarlyTimeField();
            }
        });

        etDelLateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDelLateTimeField();
            }
        });

        butSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etLoadNumber != null && etPUEarlyDate != null && etPUEarlyTime != null && etDelEarlyDate != null && etDelEarlyTime != null &&
                        etPULateDate != null && etPULateTime != null && etDelLateDate != null && etDelLateTime != null) {
                    StringRequest stringRequestShipper = new StringRequest(Request.Method.POST, Config.DATA_GET_CUSTID,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        //Parsing the fetched Json String to JSON Object
                                        JSONObject j = new JSONObject(response);
                                        //Storing the Array of JSON String to our JSON Array
                                        JSONArray result = j.getJSONArray(Config.JSON_ARRAY_CUSTOMERS);
                                        //Calling method getSettlements to get the Settlement Dates from the JSON Array
                                        JSONObject json = result.getJSONObject(0);
                                        ShipperID = json.getString(Config.TAG_SHIPPERID);
                                        Log.d("ShipperID", ShipperID);
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
                            parameters.put("CustomerName", spinShipperName.getSelectedItem().toString());
                            return parameters;
                        }
                    };
                    StringRequest stringRequestConsignee = new StringRequest(Request.Method.POST, Config.DATA_GET_CUSTID,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        //Parsing the fetched Json String to JSON Object
                                        JSONObject j = new JSONObject(response);
                                        //Storing the Array of JSON String to our JSON Array
                                        JSONArray result = j.getJSONArray(Config.JSON_ARRAY_CUSTOMERS);
                                        //Calling method getSettlements to get the Settlement Dates from the JSON Array
                                        JSONObject json = result.getJSONObject(0);
                                        ConsigneeID = json.getString(Config.TAG_SHIPPERID);
                                        Log.d("ConsigneeID", ConsigneeID);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    final boolean DrvLoad = ((CheckBox) findViewById(R.id.chkDriverLoad)).isChecked();
                                    final boolean DrvUnload = ((CheckBox) findViewById(R.id.chkDriverUnload)).isChecked();
                                    final boolean Preload = ((CheckBox) findViewById(R.id.chkPreload)).isChecked();
                                    final boolean DropHook = ((CheckBox) findViewById(R.id.chkDropHook)).isChecked();
                                    Log.d("ShipperID", ShipperID);
                                    Log.d("ConsigneeID", ConsigneeID);
                                    StringRequest stringRequestCustomers = new StringRequest(Request.Method.POST, Config.DATA_SAVE_LOAD,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    Toast.makeText(LoadEntryActivity.this, "Load saved successfully", Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent(LoadEntryActivity.this, MainActivity.class);
                                                    LoadEntryActivity.this.startActivity(intent);
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
                                            parameters.put("LoadID", etLoadNumber.getText().toString());
                                            parameters.put("CustPUNumber", etPUNumber.getText().toString());
                                            parameters.put("Weight", etWeight.getText().toString());
                                            parameters.put("Pieces", etPieces.getText().toString());
                                            parameters.put("BLNumber", etBOLNumber.getText().toString());
                                            parameters.put("TrlNumber", etTrlrNumber.getText().toString());
                                            parameters.put("DrvLoad", getBooleantoString(DrvLoad));
                                            parameters.put("DrvUnload", getBooleantoString(DrvUnload));
                                            parameters.put("LoadedMiles", etLoadedMiles.getText().toString());
                                            parameters.put("EmptyMiles", etEmptyMiles.getText().toString());
                                            parameters.put("TempLow", etTempLow.getText().toString());
                                            parameters.put("TempHigh", etTempHigh.getText().toString());
                                            parameters.put("Preloaded", getBooleantoString(Preload));
                                            parameters.put("DropHook", getBooleantoString(DropHook));
                                            parameters.put("Comments", etComments.getText().toString());

                                            parameters.put("Shipper", ShipperID);
                                            String EarlyDate = etPUEarlyDate.getText().toString();
                                            parameters.put("PUEarlyDate", convertDate(etPUEarlyDate.getText().toString()));
                                            parameters.put("PUEarlyTime", etPUEarlyTime.getText().toString());
                                            parameters.put("PULateDate", convertDate(etPULateDate.getText().toString()));
                                            parameters.put("PULateTime", etPULateTime.getText().toString());

                                            parameters.put("Consignee", ConsigneeID);
                                            parameters.put("DelEarlyDate", convertDate(etDelEarlyDate.getText().toString()));
                                            parameters.put("DelEarlyTime", etDelEarlyTime.getText().toString());
                                            parameters.put("DelLateDate", convertDate(etDelLateDate.getText().toString()));
                                            parameters.put("DelLateTime", etDelLateTime.getText().toString());

                                            return parameters;
                                        }
                                    };
                                    requestQueue.add(stringRequestCustomers);
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
                            parameters.put("CustomerName", spinConsigneeName.getSelectedItem().toString());
                            return parameters;
                        }
                    };
                    requestQueue.add(stringRequestShipper);
                    requestQueue.add(stringRequestConsignee);



                }
            }
        });
    }

    private void setPUEarlyDateField() {
        Calendar newCalendar = dateSelected;

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateSelected.set(year, monthOfYear, dayOfMonth, 0, 0);
                etPUEarlyDate.setText(dateFormatter.format(dateSelected.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        etPUEarlyDate.setText(dateFormatter.format(dateSelected.getTime()));
        datePickerDialog.show();
    }

    private void setPULateDateField() {
        Calendar newCalendar = dateSelected;

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateSelected.set(year, monthOfYear, dayOfMonth, 0, 0);
                etPULateDate.setText(dateFormatter.format(dateSelected.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        etPULateDate.setText(dateFormatter.format(dateSelected.getTime()));
        datePickerDialog.show();
    }

    private void setDelEarlyDateField() {
        Calendar newCalendar = dateSelected;

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateSelected.set(year, monthOfYear, dayOfMonth, 0, 0);
                etDelEarlyDate.setText(dateFormatter.format(dateSelected.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        etDelEarlyDate.setText(dateFormatter.format(dateSelected.getTime()));
        datePickerDialog.show();
    }

    private void setDelLateDateField() {
        Calendar newCalendar = dateSelected;

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateSelected.set(year, monthOfYear, dayOfMonth, 0, 0);
                etDelLateDate.setText(dateFormatter.format(dateSelected.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        etDelLateDate.setText(dateFormatter.format(dateSelected.getTime()));
        datePickerDialog.show();
    }

    private void setPUEarlyTimeField() {
        final Calendar newCalendar = timeSelected;
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(LoadEntryActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                timeSelected.set(Calendar.YEAR, Calendar.MONTH, Calendar.DATE, selectedHour, selectedMinute);
                etPUEarlyTime.setText( timeFormatter.format(timeSelected.getTime()));
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void setPULateTimeField() {
        final Calendar newCalendar = timeSelected;
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(LoadEntryActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                timeSelected.set(Calendar.YEAR, Calendar.MONTH, Calendar.DATE, selectedHour, selectedMinute);
                etPULateTime.setText( timeFormatter.format(timeSelected.getTime()));
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void setDelEarlyTimeField() {
        final Calendar newCalendar = timeSelected;
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(LoadEntryActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                timeSelected.set(Calendar.YEAR, Calendar.MONTH, Calendar.DATE, selectedHour, selectedMinute);
                etDelEarlyTime.setText( timeFormatter.format(timeSelected.getTime()));
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void setDelLateTimeField() {
        final Calendar newCalendar = timeSelected;
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(LoadEntryActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                timeSelected.set(Calendar.YEAR, Calendar.MONTH, Calendar.DATE, selectedHour, selectedMinute);
                etDelLateTime.setText( timeFormatter.format(timeSelected.getTime()));
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    public void OpenDialog() {
        AddCustomerDialog custDialog = new AddCustomerDialog();
        custDialog.show(getSupportFragmentManager(), "New Customer");
    }

    @Override
    public void PassCustomerInfo(final String CustomerName, final String CustomerAddr1, final String CustomerAddr2, final String CustomerCity, final String CustomerState,
                                 final String CustomerPhone, final String CustomerContact, final String CustomerComment) {
        //Creating a request queue

        // Start syncing remote database to local database
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.DATA_SAVE_CUSTOMER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        tvShipperAddr1.setText(CustomerAddr1);
                        tvShipperAddr2.setText(CustomerAddr2);
                        tvShipperCity.setText(CustomerCity);
                        tvShipperState.setText(CustomerState);
                        tvShipperPhone.setText(CustomerPhone);
                        tvShipperContact.setText(CustomerContact);
                        spinShipperName.invalidate();
                        spinConsigneeName.invalidate();
                        StringRequest stringRequestCustomers = new StringRequest(Request.Method.POST, Config.DATA_GET_CUSTOMERS,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            //Parsing the fetched Json String to JSON Object
                                            JSONObject j = new JSONObject(response);
                                            //Storing the Array of JSON String to our JSON Array
                                            JSONArray result = j.getJSONArray(Config.JSON_ARRAY_CUSTOMERS);
                                            //Calling method getSettlements to get the Settlement Dates from the JSON Array
                                            fillSpinners(result);
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
                                return parameters;
                            }
                        };
                        requestQueue.add(stringRequestCustomers);
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
                parameters.put("CustName", CustomerName);
                parameters.put("CustAddr1", CustomerAddr1);
                parameters.put("CustAddr2", CustomerAddr2);
                parameters.put("CustCity", CustomerCity);
                parameters.put("CustState", CustomerState);
                parameters.put("CustPhone", CustomerPhone);
                parameters.put("CustContact", CustomerContact);
                parameters.put("CustComment", CustomerComment);
                return parameters;
            }
        };
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void fillSpinners(JSONArray j) {
        //Traversing through all the items in the json array
        final String[] items = new String[j.length()];
        for (int i = 0; i < j.length(); i++) {
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);
                items[i]=json.getString(Config.TAG_CUSTNAME);


            } catch (JSONException e) {
                e.printStackTrace();
            }
            ArrayAdapter<String> adapter =
                    new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) ;

            spinShipperName.setAdapter(adapter);
            spinConsigneeName.setAdapter(adapter);

        }
    }

    private void getCustomer(JSONArray j, int TypeCust) {
        //Traversing through all the items in the json array
        final String[] items = new String[j.length()];
        String debugSelected = spinShipperName.getSelectedItem().toString();
        if (TypeCust != 1) {
            debugSelected = spinConsigneeName.getSelectedItem().toString();
        }

        for (int i = 0; i < j.length(); i++) {
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);
                String debugCustName = json.getString(Config.TAG_CUSTNAME);

                if (debugSelected.equals(debugCustName)) {
                   if (TypeCust == 1) {
                        tvShipperAddr1.setText(json.getString(Config.TAG_CUSTADDR1));
                        tvShipperAddr2.setText(json.getString(Config.TAG_CUSTADDR2));
                        tvShipperCity.setText(json.getString(Config.TAG_CUSTCITY));
                        tvShipperState.setText(json.getString(Config.TAG_CUSTSTATE));
                        tvShipperPhone.setText(json.getString(Config.TAG_CUSTPHONE));
                        tvShipperContact.setText(json.getString(Config.TAG_CUSTCONTACT));
                    } else {
                        tvConsigneeAddr1.setText(json.getString(Config.TAG_CUSTADDR1));
                        tvConsigneeAddr2.setText(json.getString(Config.TAG_CUSTADDR2));
                        tvConsigneeCity.setText(json.getString(Config.TAG_CUSTCITY));
                        tvConsigneeState.setText(json.getString(Config.TAG_CUSTSTATE));
                        tvConsigneePhone.setText(json.getString(Config.TAG_CUSTPHONE));
                        tvConsigneeContact.setText(json.getString(Config.TAG_CUSTCONTACT));
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    private String getBooleantoString (Boolean Input) {
        if(Input)
        {
            return "1";
        }
        else
        {
            return "0";
        }
    }

    private String convertDate (String dateToConvert) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        Date newDate = null;
        try {
            newDate = format.parse(dateToConvert);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return format.format(newDate);
    }
}
