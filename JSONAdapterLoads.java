package com.jrschugel.loadmanager;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

class JSONAdapterLoads extends BaseAdapter implements ListAdapter {

    private final Activity activity;
    private final JSONArray jsonArray;
    private final Context context;
    DatabaseHelper myDb;

    JSONAdapterLoads(Context context, Activity activity, JSONArray jsonArray) {
        assert activity != null;
        assert jsonArray != null;
        assert context != null;

        this.jsonArray = jsonArray;
        this.activity = activity;
        this.context = context;
    }

    @Override public int getCount() {
        return jsonArray.length();
    }

    @Override public JSONObject getItem(int position) {
        return jsonArray.optJSONObject(position);
    }

    @Override public long getItemId(int position) {
        JSONObject jsonObject = getItem(position);

        return jsonObject.optLong("id");
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null)
            convertView = activity.getLayoutInflater().inflate(R.layout.listview_loads_layout, null);
        myDb = new DatabaseHelper(context);

        TextView tvLoadNumber = convertView.findViewById(R.id.tvLoadNumber);
        TextView tvStatus = convertView.findViewById(R.id.tvStatus);
        TextView tvShipperName = convertView.findViewById(R.id.tvShipperName);
        TextView tvShipperCity = convertView.findViewById(R.id.tvShipperCity);
        TextView tvShipperState = convertView.findViewById(R.id.tvShipperState);
        TextView tvConsName = convertView.findViewById(R.id.tvConsName);
        TextView tvConsCity = convertView.findViewById(R.id.tvConsCity);
        TextView tvConsState = convertView.findViewById(R.id.tvConsState);
        TextView tvPickupDate = convertView.findViewById(R.id.tvPUDate);
        TextView tvPickupTime = convertView.findViewById(R.id.tvPUTime);

        JSONObject json_data = getItem(position);
        if(null!=json_data ){
            String LoadNumber= null;
            String Status=null;
            String ShipName=null;
            String ShipCity=null;
            String ShipState=null;
            String ConsName=null;
            String ConsCity= null;
            String ConsState=null;
            String PUDate=null;
            String PUTime=null;

            try {
                LoadNumber = json_data.getString("StopLoadNumber");
                Status = myDb.getStatus(LoadNumber);
                ShipName = json_data.getString("ShipperName");
                ShipCity = json_data.getString("ShipperCity");
                ShipState = json_data.getString("ShipperState");
                ConsName = json_data.getString("ConsName");
                ConsCity = json_data.getString("ConsCity");
                ConsState = json_data.getString("ConsState");
                PUDate = json_data.getString("ShipDate");
                PUTime = json_data.getString("ShipTime");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (LoadNumber == null) {
                return convertView;
            }

            tvLoadNumber.setText(LoadNumber);
            tvStatus.setText(Status);
            tvShipperName.setText(ShipName);
            tvShipperCity.setText(ShipCity);
            tvShipperState.setText(ShipState);
            tvConsName.setText(ConsName);
            tvConsCity.setText(ConsCity);
            tvConsState.setText(ConsState);
            SimpleDateFormat spf=new SimpleDateFormat("yyyy-MM-dd");
            Date newDate= null;
            try {
                newDate = spf.parse(PUDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            spf= new SimpleDateFormat("MM/dd/yy ");
            PUDate = spf.format(newDate);
            spf=new SimpleDateFormat("hh:mm:ss");
            newDate= null;
            try {
                newDate = spf.parse(PUTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            spf= new SimpleDateFormat(" h:mm a");
            PUTime = spf.format(newDate);
            tvPickupDate.setText(PUDate);
            tvPickupTime.setText(PUTime);

        }

        return convertView;
    }

    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }
}