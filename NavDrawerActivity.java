package com.jrschugel.loadmanager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.HashMap;

public class NavDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, INavActivity, INavActivity2 {

    public static final String CURRENT_FRAGMENT1 = "CURRENT_FRAGMENT";
    public static final String CURRENT_LOADNUMBER1 = "CURRENT_LOADNUMBER";
    Context context;
    SessionManager session;
    public static String CURRENT_FRAGMENT;
    public static int CURRENT_LOADNUMBER;
    private static final String TAG = "NavDrawerActivity";

    private static final int MY_PHONE_REQUEST_CODE = 140;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name_short);
        setSupportActionBar(toolbar);
        context = this;
        session = new SessionManager(context);
        Log.i(TAG, "onCreate: ");

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        MainActivityFragment fragment = new MainActivityFragment();
        FragmentTransaction fragmentTrans = getSupportFragmentManager().beginTransaction();
        fragmentTrans.replace(R.id.fragment_container, fragment);
        fragmentTrans.commit();

    }

    @Override
    public void onBackPressed() {
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        if (backStackEntryCount == 0) {
            android.util.Log.d("TAG", "onBackPressed: a");

        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_loads_actions, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public void CallPhone(String PhoneNumber) {
        int checkPermission = ContextCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE);
        if (checkPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    NavDrawerActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PHONE_REQUEST_CODE);
        } else {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + PhoneNumber));
            startActivity(callIntent);
        }
    }

    /**
     * On selecting action bar icons
     */
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
                Intent intent = new Intent(context, Preferences.class);
                startActivity(intent);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PHONE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "phone call permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "phone call permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState: ");
        outState.putString(CURRENT_FRAGMENT1, CURRENT_FRAGMENT);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "onRestoreInstanceState: ");
        if (savedInstanceState.getString(CURRENT_FRAGMENT1) != null) {
            Fragment fragment;
            FragmentTransaction fragmentTrans;
            switch (savedInstanceState.getString(CURRENT_FRAGMENT1)) {
                case "Main":
                    fragment = new MainActivityFragment();
                    fragmentTrans = getSupportFragmentManager().beginTransaction();
                    fragmentTrans.replace(R.id.fragment_container, fragment);
                    CURRENT_FRAGMENT = "Main";
                    fragmentTrans.commit();
                    break;
                case "Loads":
                    setTitle("Load Manager");
                    fragment = new Loads_Fragment();
                    fragmentTrans = getSupportFragmentManager().beginTransaction();
                    fragmentTrans.replace(R.id.fragment_container, fragment);
                    CURRENT_FRAGMENT = "Loads";
                    fragmentTrans.commit();
                    if (LoadsListAdapter.mCurrentLoadNumber != null && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        Loads_Details_Fragment details = Loads_Details_Fragment.newInstance(LoadsListAdapter.mCurrentLoadNumber);
                        fragmentTrans = getSupportFragmentManager().beginTransaction();
                                // Replace any other Fragment with our new Details Fragment with the right data
                        fragmentTrans.replace(R.id.details, details)
                                .addToBackStack(null)
                                // TRANSIT_FRAGMENT_FADE calls for the Fragment to fade away
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .commit();
                    }
                    break;
                case "Fuelstops":
                    setTitle("Fuel Stops");
                    fragment = new MapFragment();
                    fragmentTrans = getSupportFragmentManager().beginTransaction();
                    fragmentTrans.replace(R.id.fragment_container, fragment);
                    CURRENT_FRAGMENT = "Fuelstops";
                    fragmentTrans.commit();
                    break;
                case "Settlements":
                    setTitle("Settlements");
                    fragment = new SettlementFragment();
                    fragmentTrans = getSupportFragmentManager().beginTransaction();
                    fragmentTrans.replace(R.id.fragment_container, fragment);
                    CURRENT_FRAGMENT = "Settlements";
                    fragmentTrans.commit();
                    break;
                case "Newsletter":
                    setTitle("Newsletter");
                    fragment = new NewsletterFragment();
                    fragmentTrans = getSupportFragmentManager().beginTransaction();
                    fragmentTrans.replace(R.id.fragment_container, fragment);
                    CURRENT_FRAGMENT = "Newsletter";
                    fragmentTrans.commit();
                    break;
                case "Alarmclock":
                    setTitle("Alarm Clock");
                    fragment = new AlarmActivity();
                    fragmentTrans = getSupportFragmentManager().beginTransaction();
                    fragmentTrans.replace(R.id.fragment_container, fragment);
                    CURRENT_FRAGMENT = "Alarmclock";
                    fragmentTrans.commit();
                    break;
                case "Referral":
                    setTitle("Driver Referral");
                    fragment = new ReferralFragment();
                    fragmentTrans = getSupportFragmentManager().beginTransaction();
                    fragmentTrans.replace(R.id.fragment_container, fragment);
                    CURRENT_FRAGMENT = "Referral";
                    fragmentTrans.commit();
                    break;
                case "Accident":
                    setTitle("Accident Report");
                    fragment = new AccidentFragment();
                    fragmentTrans = getSupportFragmentManager().beginTransaction();
                    fragmentTrans.replace(R.id.fragment_container, fragment);
                    CURRENT_FRAGMENT = "Accident";
                    fragmentTrans.commit();
                    break;
                case "Enterload":
                    setTitle("Enter New Load");
                    fragment = new Loads_Entry_Fragment();
                    fragmentTrans = getSupportFragmentManager().beginTransaction();
                    fragmentTrans.replace(R.id.fragment_container, fragment);
                    CURRENT_FRAGMENT = "Enterload";
                    fragmentTrans.commit();
                    break;
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_main) {
            setTitle("J & R Schugel");
            MainActivityFragment fragment = new MainActivityFragment();
            FragmentTransaction fragmentTrans = getSupportFragmentManager().beginTransaction();
            fragmentTrans.replace(R.id.fragment_container, fragment);
            CURRENT_FRAGMENT = "main";
            fragmentTrans.commit();
        } else if (id == R.id.nav_loads) {
            setTitle("Load Manager");
            Loads_Fragment fragment = new Loads_Fragment();
            FragmentTransaction fragmentTrans = getSupportFragmentManager().beginTransaction();
            fragmentTrans.replace(R.id.fragment_container, fragment);
            CURRENT_FRAGMENT = "Loads";
            fragmentTrans.commit();
        } else if (id == R.id.nav_fuelstops) {
            setTitle("Fuel Stops");
            MapFragment fragment = new MapFragment();
            FragmentTransaction fragmentTrans = getSupportFragmentManager().beginTransaction();
            fragmentTrans.replace(R.id.fragment_container, fragment);
            CURRENT_FRAGMENT = "Fuelstops";
            fragmentTrans.commit();
        } else if (id == R.id.nav_settlements) {
            setTitle("Settlements");
            SettlementFragment fragment = new SettlementFragment();
            FragmentTransaction fragmentTrans = getSupportFragmentManager().beginTransaction();
            fragmentTrans.replace(R.id.fragment_container, fragment);
            CURRENT_FRAGMENT = "Settlements";
            fragmentTrans.commit();
        } else if (id == R.id.nav_newsletter) {
            NewsletterFragment fragment = new NewsletterFragment();
            FragmentTransaction fragmentTrans = getSupportFragmentManager().beginTransaction();
            fragmentTrans.replace(R.id.fragment_container, fragment);
            CURRENT_FRAGMENT = "Newsletter";
            fragmentTrans.commit();
        } else if (id == R.id.nav_alarm_clock) {
            AlarmActivity fragment = new AlarmActivity();
            FragmentTransaction fragmentTrans = getSupportFragmentManager().beginTransaction();
            fragmentTrans.replace(R.id.fragment_container, fragment);
            CURRENT_FRAGMENT = "Alarmclock";
            fragmentTrans.commit();
        } else if (id == R.id.nav_referral) {
            ReferralFragment fragment = new ReferralFragment();
            FragmentTransaction fragmentTrans = getSupportFragmentManager().beginTransaction();
            fragmentTrans.replace(R.id.fragment_container, fragment);
            CURRENT_FRAGMENT = "Referral";
            fragmentTrans.commit();
        } else if (id == R.id.nav_accident) {
            AccidentFragment fragment = new AccidentFragment();
            FragmentTransaction fragmentTrans = getSupportFragmentManager().beginTransaction();
            fragmentTrans.replace(R.id.fragment_container, fragment);
            CURRENT_FRAGMENT = "Accident";
            fragmentTrans.commit();
        } else if (id == R.id.nav_loads_enter) {
            Loads_Entry_Fragment fragment = new Loads_Entry_Fragment();
            FragmentTransaction fragmentTrans = getSupportFragmentManager().beginTransaction();
            fragmentTrans.replace(R.id.fragment_container, fragment);
            CURRENT_FRAGMENT = "Enterload";
            fragmentTrans.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openLoadDetailsFragment (Fragment fragment, String LoadNumber) {
        Bundle bundle = new Bundle();
        bundle.putString("frag_load_number", LoadNumber);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
        .addToBackStack(null)
        .replace(R.id.fragment_container, fragment)
        .commit();
    }

    private void openFragment (Fragment fragment, Bundle bundle, String fragmentTag) {
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragment_container, fragment, fragmentTag)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();

    }

    @Override
    public void inflateFragment(String fragmentTag, String LoadNumber) {
        Loads_Details_Fragment fragment = new Loads_Details_Fragment();
        openLoadDetailsFragment(fragment, LoadNumber);
    }


    @Override
    public void inflateFragment(Fragment fragment, String fragmentTag, Bundle bundle) {
        openFragment(fragment, bundle, fragmentTag);
    }

    @Override
    public void sendEmail(Intent intent) {
        this.startActivity(intent);
    }


}
