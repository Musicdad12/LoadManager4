package com.jrschugel.loadmanager;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class JSONAdapterFS extends BaseAdapter implements ListAdapter {

    private final Activity activity;
    private final JSONArray jsonArray;
    public JSONAdapterFS(Activity activity, JSONArray jsonArray) {
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
            convertView = activity.getLayoutInflater().inflate(R.layout.listview_fuelstops_layout, null);

        ImageView FSIcon = convertView.findViewById((R.id.imgTruckstopIcon));
        TextView FSName = convertView.findViewById(R.id.tvTruckstopName);
        TextView FSCityState = convertView.findViewById(R.id.tvCityState);
        TextView FSLocation = convertView.findViewById(R.id.tvLocation);
        TextView FSPhone = convertView.findViewById(R.id.tvPhone);
        TextView FSFax = convertView.findViewById(R.id.tvFax);
        TextView FSParking = convertView.findViewById(R.id.tvParking);
        TextView FSShowers = convertView.findViewById(R.id.tvShowers);
        TextView FSScale = convertView.findViewById(R.id.tvScale);
        TextView FSRepair = convertView.findViewById(R.id.tvRepair);
        TextView FSDealer = convertView.findViewById(R.id.tvDealer);
        TextView FSLimit = convertView.findViewById(R.id.tvFuelQty);
        TextView FSRestaurant = convertView.findViewById(R.id.tvRestaurant);
        TextView FSLongitude = convertView.findViewById(R.id.tvLongitude);
        TextView FSLatitude = convertView.findViewById(R.id.tvLatitude);


        JSONObject json_data = getItem(position);
        if(null!=json_data ){
            String Icon= null;
            String Name=null;
            String CityState=null;
            String Location=null;
            String Phone=null;
            String Fax= null;
            String Parking=null;
            String Showers=null;
            String Scale=null;
            String Repair=null;
            String Dealer=null;
            String Limit=null;
            String Restaurant=null;
            String Longitude=null;
            String Latitude=null;
            try {
                Icon = json_data.getString("IconID");
                Name = json_data.getString("Name");
                CityState = json_data.getString("CityState");
                Location = json_data.getString("Location");
                Phone = json_data.getString("Phone");
                Fax = json_data.getString("Fax");
                Parking = json_data.getString("Parking");
                Showers = json_data.getString("Showers");
                Scale = json_data.getString("Scale");
                Repair = json_data.getString("Repair");
                Dealer = json_data.getString("Dealer");
                Limit = json_data.getString("FuelLimit");
                Restaurant = json_data.getString("Restaurant");
                Longitude = json_data.getString("Longitude");
                Latitude = json_data.getString("Latitude");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            switch (Icon) {
                case ("Loves") :
                FSIcon.setImageResource(R.drawable.loves3);
                    break;
                case ("Pilot") :
                FSIcon.setImageResource(R.drawable.pilot);
                    break;
                case ("TA"):
                FSIcon.setImageResource(R.drawable.t_a);
                    break;
                case ("FlyingJ"):
                FSIcon.setImageResource(R.drawable.flying_j) ;
                    break;
                case ("Petro"):
                FSIcon.setImageResource(R.drawable.port_petro_bg);
                    break;
                case ("KwikTrip"):
                FSIcon.setImageResource(R.drawable.kwiktrip);
                    break;
                default:
                FSIcon.setImageResource(R.drawable.icon_fuel);
            }
            FSName.setText(Name);
            FSCityState.setText(CityState);
            FSLocation.setText(Location);
            FSPhone.setText(Phone);
            FSFax.setText(Fax);
            FSParking.setText(Parking);
            FSShowers.setText(Showers);
            FSScale.setText(Scale);
            FSRepair.setText(Repair);
            FSDealer.setText(Dealer);
            FSLimit.setText(Limit);
            FSRestaurant.setText(Restaurant);
            FSLongitude.setText(Longitude);
            FSLatitude.setText(Latitude);
        }

        return convertView;
    }
}