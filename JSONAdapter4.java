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

class JSONAdapter4 extends BaseAdapter implements ListAdapter {

    private final Activity activity;
    private final JSONArray jsonArray;
    public JSONAdapter4(Activity activity, JSONArray jsonArray) {
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
            convertView = activity.getLayoutInflater().inflate(R.layout.listview_settlement4_layout, null);

        TextView UnitNumber = convertView.findViewById(R.id.UnitNumber);
        TextView LoadNumber = convertView.findViewById(R.id.LoadNumber);
        TextView DispatchDate = convertView.findViewById(R.id.DispatchDate);
        TextView PerDiem = convertView.findViewById(R.id.PerDiem);
        TextView Total = convertView.findViewById(R.id.Total);

        JSONObject json_data = getItem(position);
        if(null!=json_data ){
            String unitnumber= null;
            String loadnumber=null;
            String dispatchdate=null;
            String perdiem=null;
            String total=null;
            try {
                unitnumber = json_data.getString("UnitNumber");
                loadnumber = json_data.getString("LoadNumber");
                dispatchdate = json_data.getString("DispatchDate");
                perdiem = "PER DIEM";
                total = json_data.getString("PerDiemTotal");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            UnitNumber.setText(unitnumber);
            LoadNumber.setText(loadnumber);
            DispatchDate.setText(dispatchdate);
            PerDiem.setText(perdiem);
            Total.setText(total);
        }

        return convertView;
    }
}