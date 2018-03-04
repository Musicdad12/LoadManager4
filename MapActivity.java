package com.jrschugel.loadmanager;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, FuelStopDialog.FilterListener {

    private GoogleMap mMap;
    private SessionManager session;
    private HashMap<Marker, Integer> mHashMap = new HashMap<>();
    private static final String TAG = "MapActivity";

    private static final String FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = android.Manifest.permission.ACCESS_COARSE_LOCATION;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 8f;

    private final String KEY_UNLIMITED = "filterUnlimited";
    private final String KEY_LIMITED = "filterLimited";
    private final String KEY_REEFER = "filterReefer";
    private final String KEY_LOVES = "filterLoves";
    private final String KEY_PILOT = "filterPilot";
    private final String KEY_FLYINGJ = "filterFlyingJ";
    private final String KEY_TA = "filterTA";
    private final String KEY_PETRO = "filterPetro";
    private final String KEY_KWIKTRIP = "filterKwikTrip";
    private final String KEY_QUIKTRIP = "filterQuikTrip";
    private final String KEY_INDEPENDENT = "filterIndependent";

    //vars
    private Boolean mLocationPermissionsGranted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ImageView butFilter = findViewById(R.id.ic_filter);

        butFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenDialog();
            }
        });

    }

    public void OpenDialog() {
        FuelStopDialog filterDialog = new FuelStopDialog();
        filterDialog.show(getSupportFragmentManager(), "Map Filter Settings");
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getLocationPermission();
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                int pos = mHashMap.get(marker);
                Log.i("Position of arraylist", pos+"");
                mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapActivity.this));
                return false;
            }
        });

        try {
            JSONArray m_jArry = new JSONArray(loadJSONFromAsset());

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                Log.d("Details-->", jo_inside.getString("Lat") + ", " + jo_inside.getString("Long"));
                Double Long = jo_inside.getDouble("Lat");
                Double Lat = jo_inside.getDouble("Long");
                String Name = jo_inside.getString("Name");
                String Address = jo_inside.getString("Address");
                String City = jo_inside.getString("City");
                String State = jo_inside.getString("State");
                String Zip = jo_inside.getString("Zip");
                String Phone = jo_inside.getString("Phone");
                String Showers = jo_inside.getString("Showers");
                String Parking = jo_inside.getString("Parking");
                String Scale = jo_inside.getString("CatScale");
                String Food = jo_inside.getString("Food");
                String Limit = jo_inside.getString("Limit");
                BitmapDescriptor Icon = getTruckstopIcon(Name, Limit);

                if (Icon != null) {
                    String snippet = Address + "\n" +
                            City + ", " + State + " " + Zip +
                            "\nPhone: " + Phone +
                            "\n" + Showers + "  " + Parking +
                            "\n" + Scale + "  Food: " + Food +
                            "\n" + Limit;
                    Log.d("       -->", snippet);
                    Marker mMarker = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Lat, Long))
                            .anchor(0.5f, 0.5f)
                            .title(Name)
                            .snippet(snippet)
                            .icon(Icon));

                    mHashMap.put(mMarker, i);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
            getDeviceLocation();

    }

    public String loadJSONFromAsset() {
        String json;
        try {
            InputStream is = this.getAssets().open("FuelStops.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(mLocationPermissionsGranted){

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();
                            mMap.setMyLocationEnabled(true);
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM);

                        }else{
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(MapActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }

    private void moveCamera(LatLng latLng, float zoom){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                //initMap();
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for (int grantResult : grantResults) {
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    //initMap();
                }
            }
        }
    }

    private BitmapDescriptor getTruckstopIcon (String Name, String Limit) {
        session = new SessionManager(this);
        HashMap<String, String> user = session.getUserDetails();
        if (Name.contains("Schugel")) {
            return BitmapDescriptorFactory.fromResource(R.drawable.red_marker_j);
        }
        if (Limit.contains("No Limit") && Boolean.valueOf(user.get(KEY_UNLIMITED))) {
            if (Name.contains("Love") && Boolean.valueOf(user.get(KEY_LOVES))) {
                return BitmapDescriptorFactory.fromResource(R.drawable.green_marker_loves);
            }
            if (Name.contains("Pilot") && Boolean.valueOf(user.get(KEY_PILOT))) {
                return BitmapDescriptorFactory.fromResource(R.drawable.green_marker_pilot);
            }
            if (Name.contains("Flying") && Boolean.valueOf(user.get(KEY_FLYINGJ))) {
                return BitmapDescriptorFactory.fromResource(R.drawable.green_marker_flyingj);
            }
            if (Name.contains("Petro") && Boolean.valueOf(user.get(KEY_PETRO))) {
                return BitmapDescriptorFactory.fromResource(R.drawable.green_marker_ta);
            }
            if (Name.contains("T. A.") && Boolean.valueOf(user.get(KEY_TA))) {
                return BitmapDescriptorFactory.fromResource(R.drawable.green_marker_ta);
            }
            if (Name.contains("Kwik") && Boolean.valueOf(user.get(KEY_KWIKTRIP))) {
                return BitmapDescriptorFactory.fromResource(R.drawable.green_marker_kwiktrip);
            }
            if (Name.contains("Quik") && Boolean.valueOf(user.get(KEY_QUIKTRIP))) {
                return BitmapDescriptorFactory.fromResource(R.drawable.green_marker_quiktrip);
            }
            if (!Name.contains("Love") && !Name.contains("Pilot") && !Name.contains("Flying") && !Name.contains("Petro") && !Name.contains("T. A.") &&
                    !Name.contains("Kwik") && !Name.contains("Quik") && Boolean.valueOf(user.get(KEY_INDEPENDENT))) {
                return BitmapDescriptorFactory.fromResource(R.drawable.green_marker_ind);
            }
        } else if (Limit.contains("Reefer Only") && Boolean.valueOf(user.get(KEY_REEFER))) {
            if (Name.contains("Love") && Boolean.valueOf(user.get(KEY_LOVES))) {
                return BitmapDescriptorFactory.fromResource(R.drawable.blue_marker_loves);
            }
            if (Name.contains("Pilot") && Boolean.valueOf(user.get(KEY_PILOT))) {
                return BitmapDescriptorFactory.fromResource(R.drawable.blue_marker_pilot);
            }
            if (Name.contains("Flying") && Boolean.valueOf(user.get(KEY_FLYINGJ))) {
                return BitmapDescriptorFactory.fromResource(R.drawable.blue_marker_flyingj);
            }
            if (Name.contains("Petro") && Boolean.valueOf(user.get(KEY_PETRO))) {
                return BitmapDescriptorFactory.fromResource(R.drawable.blue_marker_ta);
            }
            if (Name.contains("T. A.") && Boolean.valueOf(user.get(KEY_TA))) {
                return BitmapDescriptorFactory.fromResource(R.drawable.blue_marker_ta);
            }
            if (Name.contains("Kwik") && Boolean.valueOf(user.get(KEY_KWIKTRIP))) {
                return BitmapDescriptorFactory.fromResource(R.drawable.blue_marker_kwiktrip);
            }
            if (Name.contains("Quik") && Boolean.valueOf(user.get(KEY_QUIKTRIP))) {
                return BitmapDescriptorFactory.fromResource(R.drawable.blue_marker_quiktrip);
            }
            if (!Name.contains("Love") && !Name.contains("Pilot") && !Name.contains("Flying") && !Name.contains("Petro") && !Name.contains("T. A.") &&
                    !Name.contains("Kwik") && !Name.contains("Quik") && Boolean.valueOf(user.get(KEY_INDEPENDENT))) {
                return BitmapDescriptorFactory.fromResource(R.drawable.blue_marker_ind);
            }
        } else {
            if (Boolean.valueOf(user.get(KEY_LIMITED))) {
                if (Name.contains("Love") && Boolean.valueOf(user.get(KEY_LOVES))) {
                    return BitmapDescriptorFactory.fromResource(R.drawable.yellow_marker_loves);
                }
                if (Name.contains("Pilot") && Boolean.valueOf(user.get(KEY_PILOT))) {
                    return BitmapDescriptorFactory.fromResource(R.drawable.yellow_marker_pilot);
                }
                if (Name.contains("Flying") && Boolean.valueOf(user.get(KEY_FLYINGJ))) {
                    return BitmapDescriptorFactory.fromResource(R.drawable.yellow_marker_flyingj);
                }
                if (Name.contains("Petro") && Boolean.valueOf(user.get(KEY_PETRO))) {
                    return BitmapDescriptorFactory.fromResource(R.drawable.yellow_marker_ta);
                }
                if (Name.contains("T. A.") && Boolean.valueOf(user.get(KEY_TA))) {
                    return BitmapDescriptorFactory.fromResource(R.drawable.yellow_marker_ta);
                }
                if (Name.contains("Kwik") && Boolean.valueOf(user.get(KEY_KWIKTRIP))) {
                    return BitmapDescriptorFactory.fromResource(R.drawable.yellow_marker_kwiktrip);
                }
                if (Name.contains("Quik") && Boolean.valueOf(user.get(KEY_QUIKTRIP))) {
                    return BitmapDescriptorFactory.fromResource(R.drawable.yellow_marker_quiktrip);
                }
                if (!Name.contains("Love") && !Name.contains("Pilot") && !Name.contains("Flying") && !Name.contains("Petro") && !Name.contains("T. A.") &&
                        !Name.contains("Kwik") && !Name.contains("Quik") && Boolean.valueOf(user.get(KEY_INDEPENDENT))) {
                    return BitmapDescriptorFactory.fromResource(R.drawable.yellow_marker_ind);
                }
            }
        }
        return null;
    }

    @Override
    public void PassFilterInfo(Boolean booUnlimited, Boolean booLimited, Boolean booReefer, Boolean booLoves, Boolean booPilot, Boolean booFlyingJ,
                               Boolean booTA, Boolean booPetro, Boolean booKwikTrip, Boolean booQuikTrip, Boolean booInd) {
        session = new SessionManager(this);
        session.UpdateMapFilterSettings(KEY_UNLIMITED, booUnlimited);
        session.UpdateMapFilterSettings(KEY_LIMITED, booLimited);
        session.UpdateMapFilterSettings(KEY_REEFER, booReefer);
        session.UpdateMapFilterSettings(KEY_LOVES, booLoves);
        session.UpdateMapFilterSettings(KEY_PILOT, booPilot);
        session.UpdateMapFilterSettings(KEY_FLYINGJ, booFlyingJ);
        session.UpdateMapFilterSettings(KEY_TA, booTA);
        session.UpdateMapFilterSettings(KEY_PETRO, booPetro);
        session.UpdateMapFilterSettings(KEY_KWIKTRIP, booKwikTrip);
        session.UpdateMapFilterSettings(KEY_QUIKTRIP, booQuikTrip);
        session.UpdateMapFilterSettings(KEY_INDEPENDENT, booInd);

        mMap.clear();

        try {
            JSONArray m_jArry = new JSONArray(loadJSONFromAsset());

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                Log.d("Details-->", jo_inside.getString("Lat") + ", " + jo_inside.getString("Long"));
                Double Long = jo_inside.getDouble("Lat");
                Double Lat = jo_inside.getDouble("Long");
                String Name = jo_inside.getString("Name");
                String Address = jo_inside.getString("Address");
                String City = jo_inside.getString("City");
                String State = jo_inside.getString("State");
                String Zip = jo_inside.getString("Zip");
                String Phone = jo_inside.getString("Phone");
                String Showers = jo_inside.getString("Showers");
                String Parking = jo_inside.getString("Parking");
                String Scale = jo_inside.getString("CatScale");
                String Food = jo_inside.getString("Food");
                String Limit = jo_inside.getString("Limit");
                BitmapDescriptor Icon = getTruckstopIcon(Name, Limit);

                if (Icon != null) {
                    String snippet = Address + "\n" +
                            City + ", " + State + " " + Zip +
                            "\nPhone: " + Phone +
                            "\n" + Showers + "  " + Parking +
                            "\n" + Scale + "  Food: " + Food +
                            "\n" + Limit;
                    Log.d("       -->", snippet);
                    Marker mMarker = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Lat, Long))
                            .anchor(0.5f, 0.5f)
                            .title(Name)
                            .snippet(snippet)
                            .icon(Icon));

                    mHashMap.put(mMarker, i);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getDeviceLocation();
    }
}