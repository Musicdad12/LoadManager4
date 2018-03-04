package com.jrschugel.loadmanager;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sean McCallis on 2/22/2017.
 * Copyright 2017. All rights reserved.
 */

class FuelStopRequest extends StringRequest{
    private static final String STOPS_REQUEST_URL = "http://truckersean.com/android/getFuelStops.php";
    private Map<String, String> params;

    FuelStopRequest(String Filter, String State, Response.Listener<String> listener){
        super(Method.POST, STOPS_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("Filter", Filter);
        params.put("State", State);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
