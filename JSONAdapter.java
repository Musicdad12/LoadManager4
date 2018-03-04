package com.jrschugel.loadmanager;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class JSONAdapter extends BaseAdapter implements ListAdapter {

    private final Activity activity;
    private final JSONArray jsonArray;
    JSONAdapter(Activity activity, JSONArray jsonArray) {
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
            convertView = activity.getLayoutInflater().inflate(R.layout.listview_settlement1_layout, null);

        TextView LoadNumber = convertView.findViewById(R.id.LoadNumber);
        TextView DispatchDate = convertView.findViewById(R.id.DispatchDate);
        TextView EmptyDate = convertView.findViewById(R.id.EmptyDate);
        TextView TypePay = convertView.findViewById(R.id.TypePay);
        TextView Hours = convertView.findViewById(R.id.Hours);

        JSONObject json_data = getItem(position);
        if(null!=json_data ){
            String loadnumber= null;
            String dispatchdate=null;
            String emptydate=null;
            String paytype=null;
            String hours=null;
            try {
                loadnumber = json_data.getString("LoadNumber");
                dispatchdate = json_data.getString("DispatchDate");
                emptydate = json_data.getString("EmptyDate");
                paytype = json_data.getString("PayType");
                hours = json_data.getString("Hours");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            LoadNumber.setText(loadnumber);
            DispatchDate.setText(dispatchdate);
            EmptyDate.setText(emptydate);
            TypePay.setText(paytype);
            Hours.setText(hours);
        }

        return convertView;
    }
}