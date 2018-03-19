package com.jrschugel.loadmanager;

import android.app.DatePickerDialog;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class Loads_Entry_Fragment extends Fragment implements AddCustomerDialog.AddCustomerListener{

    private EditText etLoadNumber, etPUEarlyDate, etPUEarlyTime, etPULateDate, etPULateTime;
    private EditText etPUNumber, etWeight, etPieces, etBOLNumber, etTrlrNumber;
    private Spinner spinShipperName, spinConsigneeName;
    private EditText etDelEarlyDate, etDelEarlyTime, etDelLateDate, etDelLateTime;
    private EditText etLoadedMiles, etEmptyMiles, etTempLow, etTempHigh, etComments;
    private TextView tvShipperAddr1, tvShipperAddr2, tvShipperCity, tvShipperState, tvShipperPhone, tvShipperContact;
    private TextView tvConsigneeAddr1, tvConsigneeAddr2, tvConsigneeCity, tvConsigneeState, tvConsigneePhone, tvConsigneeContact;
    private RequestQueue requestQueue;
    private String ShipperID, ConsigneeID;
    Calendar dateSelected = Calendar.getInstance();
    Calendar timeSelected = Calendar.getInstance();
    private DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormatter;
    SimpleDateFormat timeFormatter;
    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.activity_load_entry, container, false);
        context = getActivity();

        assert context != null;
        requestQueue = Volley.newRequestQueue(context);
        dateFormatter = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        timeFormatter = new SimpleDateFormat("HH:mm a", Locale.US);

        etLoadNumber = rootView.findViewById(R.id.etLoadNumber);
        spinShipperName = rootView.findViewById(R.id.spinShipper);
        Button butAddCustomer = rootView.findViewById(R.id.butAddCustomer);
        tvShipperAddr1 = rootView.findViewById(R.id.tvShipperAddr1);
        tvShipperAddr2 = rootView.findViewById(R.id.tvShipperAddr2);
        tvShipperCity = rootView.findViewById(R.id.tvShipperCity);
        tvShipperState = rootView.findViewById(R.id.tvShipperState);
        tvShipperPhone = rootView.findViewById(R.id.tvShipperPhone);
        tvShipperContact = rootView.findViewById(R.id.tvShipperContact);
        etPUEarlyDate = rootView.findViewById(R.id.etLoadEarlyDate);
        etPUEarlyTime = rootView.findViewById(R.id.etLoadEarlyTime);
        etPULateDate = rootView.findViewById(R.id.etLoadLateDate);
        etPULateTime = rootView.findViewById(R.id.etLoadLateTime);
        etPUNumber = rootView.findViewById(R.id.etPUNumber);
        etWeight = rootView.findViewById(R.id.etWeight);
        etPieces = rootView.findViewById(R.id.etPieces);
        etBOLNumber = rootView.findViewById(R.id.etBOLNumber);
        etTrlrNumber = rootView.findViewById(R.id.etTrailer);
        spinConsigneeName = rootView.findViewById(R.id.spinConsignee);
        tvConsigneeAddr1 = rootView.findViewById(R.id.tvConsigneeAddr1);
        tvConsigneeAddr2 = rootView.findViewById(R.id.tvConsigneeAddr2);
        tvConsigneeCity = rootView.findViewById(R.id.tvConsigneeCity);
        tvConsigneeState = rootView.findViewById(R.id.tvConsigneeState);
        tvConsigneePhone = rootView.findViewById(R.id.tvConsigneePhone);
        tvConsigneeContact = rootView.findViewById(R.id.tvConsigneeContact);
        etDelEarlyDate = rootView.findViewById(R.id.etDelEarlyDate);
        etDelEarlyTime = rootView.findViewById(R.id.etDelEarlyTime);
        etDelLateDate = rootView.findViewById(R.id.etDelLateDate);
        etDelLateTime = rootView.findViewById(R.id.etDelLateTime);
        etLoadedMiles = rootView.findViewById(R.id.etLoaded);
        etEmptyMiles = rootView.findViewById(R.id.etEmpty);
        etTempLow = rootView.findViewById(R.id.etTempLow);
        etTempHigh = rootView.findViewById(R.id.etTempHigh);
        etComments = rootView.findViewById(R.id.etComments);
        Button butSave = rootView.findViewById(R.id.butSave);

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

                                    final boolean DrvLoad = ((CheckBox) rootView.findViewById(R.id.chkDriverLoad)).isChecked();
                                    final boolean DrvUnload = ((CheckBox) rootView.findViewById(R.id.chkDriverUnload)).isChecked();
                                    final boolean Preload = ((CheckBox) rootView.findViewById(R.id.chkPreload)).isChecked();
                                    final boolean DropHook = ((CheckBox) rootView.findViewById(R.id.chkDropHook)).isChecked();
                                    Log.d("ShipperID", ShipperID);
                                    Log.d("ConsigneeID", ConsigneeID);
                                    StringRequest stringRequestCustomers = new StringRequest(Request.Method.POST, Config.DATA_SAVE_LOAD,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    Toast.makeText(context, "Load saved successfully", Toast.LENGTH_LONG).show();
                                                    //Intent intent = new Intent(Loads_Entry_Fragment.this, MainActivity.class);
                                                    //Loads_Entry_Fragment.this.startActivity(intent);
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
        return rootView;
    }

    private void setPUEarlyDateField() {
        Calendar newCalendar = dateSelected;

        datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

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

        datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

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

        datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

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

        datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateSelected.set(year, monthOfYear, dayOfMonth, 0, 0);
                etDelLateDate.setText(dateFormatter.format(dateSelected.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        etDelLateDate.setText(dateFormatter.format(dateSelected.getTime()));
        datePickerDialog.show();
    }

    private void setPUEarlyTimeField() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
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
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
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
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
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
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
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
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        AddCustomerDialog custDialog = new AddCustomerDialog();
        custDialog.show(ft, "New Customer");
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
                    new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, items);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) ;

            spinShipperName.setAdapter(adapter);
            spinConsigneeName.setAdapter(adapter);

        }
    }

    private void getCustomer(JSONArray j, int TypeCust) {
        //Traversing through all the items in the json array
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
