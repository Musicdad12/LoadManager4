package com.jrschugel.loadmanager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;
import java.util.HashMap;

/**
 * Created by owner on 2/22/2017.
 * Copyright 2017. All rights reserved.
 */

class LoginRequest extends StringRequest{
    private static final String LOGIN_REQUEST_URL = "http://truckersean.com/android/Login.php";
    private Map<String, String> params;

    LoginRequest(String username, String password, Response.Listener<String> listener){
        super(Request.Method.POST, LOGIN_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
