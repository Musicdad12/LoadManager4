package com.jrschugel.loadmanager;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by owner on 2/22/2017.
 * Copyright 2017. All rights reserved.
 */

class SettlementRequest5 extends StringRequest{
    private static final String SETTLEMENT_REQUEST_URL = "http://truckersean.com/android/getSettlement5.php";
    private Map<String, String> params;

    SettlementRequest5(String Settlement, Response.Listener<String> listener){
        super(Method.POST, SETTLEMENT_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("SettleDate", Settlement);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
