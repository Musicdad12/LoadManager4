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

class JSONAdapter2 extends BaseAdapter implements ListAdapter {

    private final Activity activity;
    private final JSONArray jsonArray;
    JSONAdapter2(Activity activity, JSONArray jsonArray) {
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
            convertView = activity.getLayoutInflater().inflate(R.layout.listview_settlement2_layout, null);

        TextView LoadedMiles = convertView.findViewById(R.id.LoadedMiles);
        TextView EmptyMiles = convertView.findViewById(R.id.EmptyMiles);
        TextView PayQty = convertView.findViewById(R.id.PayQty);
        TextView Rate = convertView.findViewById(R.id.Rate);
        TextView TaxavleEarnings = convertView.findViewById(R.id.TaxableEarnings);

        JSONObject json_data = getItem(position);
        if(null!=json_data ){
            String loadedmiles= null;
            String emptymiles=null;
            String payqty=null;
            String rate=null;
            String taxable=null;
            try {
                loadedmiles = json_data.getString("LoadedMiles");
                emptymiles = json_data.getString("EmptyMiles");
                payqty = json_data.getString("PayQty");
                rate = json_data.getString("Rate");
                taxable = json_data.getString("TaxableEarnings");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            LoadedMiles.setText(loadedmiles);
            EmptyMiles.setText(emptymiles);
            PayQty.setText(payqty);
            Rate.setText(rate);
            TaxavleEarnings.setText(taxable);
        }

        return convertView;
    }
}