package com.jrschugel.loadmanager;
/*
  Created by seanm on 8/5/2017.
  Copyright 2017. All rights reserved.
 */

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;

/**
 * @author ton1n8o - antoniocarlos.dev@gmail.com on 6/13/16.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    @SuppressLint("HardwareIds")
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        String TAG = "Registration";
        //Save refreashed token to mysql database
        SessionManager session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        final String username = user.get(SessionManager.KEY_USERNAME);
        if (username != null) {
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    System.out.println("Response: " + response);
                }
            };
            final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(TELEPHONY_SERVICE);

            final String tmDevice;
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            tmDevice = "" + tm.getDeviceId();
            MyFirebaseUpdateRequest updateRequest = new MyFirebaseUpdateRequest(username, refreshedToken, tmDevice, responseListener);
            RequestQueue queue = Volley.newRequestQueue(MyFirebaseInstanceIDService.this);
            queue.add(updateRequest);
        }

        System.out.println("Registration.onTokenRefresh TOKEN: " + refreshedToken );
    }
}