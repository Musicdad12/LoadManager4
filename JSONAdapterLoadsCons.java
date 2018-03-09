package com.jrschugel.loadmanager;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
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

class JSONAdapterLoadsCons extends BaseAdapter implements ListAdapter {

    private final Activity activity;
    private final JSONArray jsonArray;
    JSONAdapterLoadsCons(Activity activity, JSONArray jsonArray) {
        assert activity != null;
        assert jsonArray != null;

        this.jsonArray = jsonArray;
        this.activity = activity;
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


    @Override public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = activity.getLayoutInflater().inflate(R.layout.listview_loads_cons_layout, null);

        ImageView butMapIt = convertView.findViewById(R.id.imageMapIt);
        final TextView tvShipperName = convertView.findViewById(R.id.textViewShipName);
        final TextView tvShipperAddr = convertView.findViewById(R.id.textViewShipAddr);
        TextView tvShipperAddr2 = convertView.findViewById(R.id.textViewShipAddr2);
        final TextView tvShipperCity = convertView.findViewById(R.id.textViewShipCity);
        final TextView tvShipperState = convertView.findViewById(R.id.textViewShipState);
        TextView tvShipperPhone = convertView.findViewById(R.id.textViewShipPhone);
        TextView tvShipperContact = convertView.findViewById(R.id.textViewShipContact);
        TextView tvShipperEarly = convertView.findViewById(R.id.textViewShipEarly);
        TextView tvShipperLate = convertView.findViewById(R.id.textViewShipLate);
        butMapIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // Search for Company and address selected
                String location = tvShipperName.getText() + " " + tvShipperAddr.getText() + " " + tvShipperCity.getText() + " " + tvShipperState.getText();
                String url = null;
                try {
                    url = "geo:0,0?q=" + URLEncoder.encode(location, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Uri gmmIntentUri = Uri.parse(url);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                activity.startActivity(mapIntent);
            }
        });

        JSONObject json_data = getItem(position);
        if(null!=json_data ){

            String ShipName=null;
            String ShipAddr=null;
            String ShipAddr2=null;
            String ShipCity=null;
            String ShipState= null;
            String ShipPhone=null;
            String ShipContact=null;
            String ShipEarlyDate = null;
            String ShipEarlyTime = null;
            String ShipLateDate = null;
            String ShipLateTime = null;

            try {
                ShipName = json_data.getString(Config.TAG_STOPCUSTNAME);
                ShipAddr = json_data.getString(Config.TAG_STOPCUSTADDR);
                ShipAddr2 = json_data.getString(Config.TAG_STOPCUSTADDR2);
                ShipCity = json_data.getString(Config.TAG_STOPCUSTCITY);
                ShipState = json_data.getString(Config.TAG_STOPCUSTSTATE);
                ShipPhone = json_data.getString(Config.TAG_STOPCUSTPHONE);
                ShipContact = json_data.getString(Config.TAG_STOPCUSTCONTACT);
                ShipEarlyDate = json_data.getString(Config.TAG_STOPEARLYDATE);
                ShipEarlyTime = json_data.getString(Config.TAG_STOPEARLYTIME);
                ShipLateDate = json_data.getString(Config.TAG_STOPLATEDATE);
                ShipLateTime = json_data.getString(Config.TAG_STOPLATETIME);
                SimpleDateFormat spfDate =new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                Date newEarlyDate=spfDate.parse(ShipEarlyDate);
                Date newLateDate=spfDate.parse(ShipLateDate);
                spfDate = new SimpleDateFormat("M/d/yy", Locale.US);
                ShipEarlyDate = spfDate.format(newEarlyDate);
                ShipLateDate = spfDate.format(newLateDate);
                SimpleDateFormat spfTime =new SimpleDateFormat("HH:mm:ss", Locale.US);
                Date newEarlyTime=spfTime.parse(ShipEarlyTime);
                Date newLateTime=spfTime.parse(ShipLateTime);
                spfTime = new SimpleDateFormat("h:mm a", Locale.US);
                ShipEarlyTime = spfTime.format(newEarlyTime);
                ShipLateTime = spfTime.format(newLateTime);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                Log.e("Error", e.toString());
            }

            tvShipperName.setText(ShipName);
            tvShipperAddr.setText(ShipAddr);
            tvShipperAddr2.setText(ShipAddr2);
            tvShipperCity.setText(ShipCity);
            tvShipperState.setText(ShipState);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tvShipperPhone.setText(PhoneNumberUtils.formatNumber(ShipPhone, "US"));
            }
            tvShipperContact.setText(ShipContact);
            String ShipEarly = "Early Date/Time: " + ShipEarlyDate + "  -  " + ShipEarlyTime;
            String ShipLate = "Late Date/Time:  " + ShipLateDate + "  -  " + ShipLateTime;
            tvShipperEarly.setText(ShipEarly);
            tvShipperLate.setText(ShipLate);
        }

        return convertView;
    }
}