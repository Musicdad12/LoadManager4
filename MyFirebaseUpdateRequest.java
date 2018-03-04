package com.jrschugel.loadmanager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by seanm on 8/6/2017.
 * Copyright 2017. All rights reserved.
 */

class MyFirebaseUpdateRequest extends StringRequest {
        private static final String TOKEN_REFRESH_URL = "http://truckersean.com/android/UpdateRegistrationId.php";
        private Map<String, String> params;

        MyFirebaseUpdateRequest(String username, String RegistrationToken, String tmDevice, Response.Listener<String> listener){
            super(Request.Method.POST, TOKEN_REFRESH_URL, listener, null);
            params = new HashMap<>();
            params.put("username", username);
            params.put("regid", RegistrationToken);
            params.put("device", tmDevice);
        }

        @Override
        public Map<String, String> getParams() {
            return params;
        }
    }