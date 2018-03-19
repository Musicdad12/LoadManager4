package com.jrschugel.loadmanager;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class JSONAdapterLoadsExpense extends BaseAdapter implements ListAdapter {

    private final Activity activity;
    private final JSONArray jsonArray;
    public JSONAdapterLoadsExpense(Activity activity, JSONArray jsonArray) {
        assert activity != null;
        assert jsonArray != null;

        this.jsonArray = jsonArray;
        this.activity = activity;
    }

    @Override public int getCount() {
        if(null==jsonArray)
            return 0;
        else
            return jsonArray.length();
    }

    @Override public JSONObject getItem(int position) {
        if(null==jsonArray) return null;
        else
            return jsonArray.optJSONObject(position);
    }

    @Override public long getItemId(int position) {
        JSONObject jsonObject = getItem(position);

        return jsonObject.optLong("id");
    }


    @Override public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = activity.getLayoutInflater().inflate(R.layout.listview_loads_expense_layout, null);

        final TextView tvLoadNumber = convertView.findViewById(R.id.textViewLoadNumber);
        final TextView tvDescription = convertView.findViewById(R.id.textViewDescription);
        final TextView tvPaymentType = convertView.findViewById(R.id.textViewPaymentType);
        final TextView tvPONumber = convertView.findViewById(R.id.textViewPONumber);
        final TextView tvGallons = convertView.findViewById(R.id.textViewGallons);
        final TextView tvAmount = convertView.findViewById(R.id.textViewAmount);

        JSONObject json_data = getItem(position);
        if(null!=json_data ){

            String ExpLoadNumber=null;
            String ExpDesc=null;
            String ExpType=null;
            String ExpPONum=null;
            String ExpGallons=null;
            String ExpAmount= null;

            try {
                ExpLoadNumber = json_data.getString(Config.TAG_EXPLOAD);
                ExpDesc = json_data.getString(Config.TAG_EXPDESC);
                ExpType = json_data.getString(Config.TAG_EXPTYPE);
                if (!json_data.getString(Config.TAG_EXPPONUM).equals("")) {
                    ExpPONum = "P.O. # " + json_data.getString(Config.TAG_EXPPONUM);
                }
                if (!json_data.getString(Config.TAG_EXPGALLON).equals("") && !json_data.getString(Config.TAG_EXPGALLON).equals("0")) {
                    ExpGallons = json_data.getString(Config.TAG_EXPGALLON) + " Gallons";
                }
                ExpAmount = "$ " + json_data.getString(Config.TAG_EXPAMOUNT);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            tvLoadNumber.setText(ExpLoadNumber);
            tvDescription.setText(ExpDesc);
            tvPaymentType.setText(ExpType);
            tvPONumber.setText(ExpPONum);
            tvGallons.setText(ExpGallons);
            tvAmount.setText(ExpAmount);
        }

        return convertView;
    }
}