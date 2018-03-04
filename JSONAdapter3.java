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

class JSONAdapter3 extends BaseAdapter implements ListAdapter {

    private final Activity activity;
    private final JSONArray jsonArray;
    public JSONAdapter3(Activity activity, JSONArray jsonArray) {
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
            convertView = activity.getLayoutInflater().inflate(R.layout.listview_settlement3_layout, null);

        TextView Federal = convertView.findViewById(R.id.Federal);
        TextView FICA = convertView.findViewById(R.id.FICA);
        TextView State = convertView.findViewById(R.id.State);
        TextView Local = convertView.findViewById(R.id.Local);
        TextView Total = convertView.findViewById(R.id.Total);

        JSONObject json_data = getItem(position);
        if(null!=json_data ){
            String federal= null;
            String fica=null;
            String state=null;
            String local=null;
            String total=null;
            try {
                federal = json_data.getString("Federal");
                fica = json_data.getString("FICA");
                state = json_data.getString("State");
                local = json_data.getString("Local");
                total = json_data.getString("Total");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Federal.setText(federal);
            FICA.setText(fica);
            State.setText(state);
            Local.setText(local);
            Total.setText(total);
        }

        return convertView;
    }
}