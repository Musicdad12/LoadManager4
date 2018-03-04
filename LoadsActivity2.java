package com.jrschugel.loadmanager;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.HashMap;

import me.leolin.shortcutbadger.ShortcutBadger;

public class LoadsActivity2 extends AppCompatActivity {

    DatabaseHelper myDb;
    DatabaseHelperStops myDb2;
    DatabaseHelperExpense myDb3;

    // Session Manager Class
    SessionManager session;

    ListView lstLoads;
    static String stLoadNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loads2);

        myDb = new DatabaseHelper(this);
        myDb2 = new DatabaseHelperStops(this);
        myDb3 = new DatabaseHelperExpense(this);
        lstLoads = findViewById(R.id.listview_Loads);

        showList();

        //Handle what happens when a load is selected from the list
        lstLoads.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //your code here
                TextView tvLoadNumber = view.findViewById(R.id.tvLoadNumber);
                Intent LoadViewIntent = new Intent(LoadsActivity2.this, LoadsActivity.class);
                LoadViewIntent.putExtra(Config.TAG_LOADNUMBER, tvLoadNumber.getText().toString());
                stLoadNumber = tvLoadNumber.getText().toString();
                myDb.updateViewedLoad(Integer.parseInt(stLoadNumber));
                Integer NewLoads = myDb.NotificationLoadCount();
                ShortcutBadger.applyCount(LoadsActivity2.this, NewLoads); //for 1.1.4+
                startActivity(LoadViewIntent);
            }
        });
    }

    private void showList() {

        JSONArray jsonArray = myDb2.getAllLoadsList(getApplicationContext());

        ListView LoadsList = findViewById(R.id.listview_Loads);

        JSONAdapterLoads jSONAdapter = new JSONAdapterLoads (this, LoadsActivity2.this,jsonArray);//jArray is your json array

        //Set the above adapter as the adapter of choice for our list
        LoadsList.setAdapter(jSONAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_loads_actions, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public void CallPhone(String PhoneNumber) {
        int checkPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE);
        if (checkPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{android.Manifest.permission.CALL_PHONE},
                    1);
        } else {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + PhoneNumber));
            startActivity(callIntent);
        }
    }
    /**
     * On selecting action bar icons
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_call_dispatcher:
                // call current dispatcher action
                HashMap<String, String> user = session.getUserDetails();
                final String dispphone = user.get(SessionManager.KEY_DISP_PHONE);
                CallPhone(dispphone);
                return true;
            case R.id.action_call_schugel:
                // call main Schugel Number
                CallPhone("8003592900");
                return true;
            case R.id.action_logout:
                // refresh
                session.logoutUser();
                return true;
            case R.id.action_settings:
                // settings action
                Intent intent = new Intent(LoadsActivity2.this, Preferences.class);
                startActivity(intent);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
