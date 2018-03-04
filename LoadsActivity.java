package com.jrschugel.loadmanager;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.HashMap;

import me.leolin.shortcutbadger.ShortcutBadger;

public class LoadsActivity extends AppCompatActivity {

    DatabaseHelper myDb;

    // Session Manager Class
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loads);

        myDb = new DatabaseHelper(this);

        //Integer LoadNumber = Integer.parseInt(LoadsActivity2.stLoadNumber);
        //String LoadNumber = getIntent().getStringExtra("LoadNumber");
        String LoadNumber = LoadsListAdapter.mCurrentLoadNumber;

        Bundle bundle = new Bundle();
        bundle.putString("LoadNumber", LoadNumber);
        Loads_Frag_1 fragInfo = new Loads_Frag_1();
        fragInfo.setArguments(bundle);

        myDb.updateViewedLoad(Integer.parseInt(LoadNumber));
        Integer NewLoads = myDb.NotificationLoadCount();
        ShortcutBadger.applyCount(this, NewLoads); //for 1.1.4+

       /*
      The {@link android.support.v4.view.PagerAdapter} that will provide
      fragments for each of the sections. We use a
      {@link FragmentPagerAdapter} derivative, which will keep every
      loaded fragment in memory. If this becomes too memory intensive, it
      may be best to switch to a
      {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
        LoadsPagerAdapter mSectionsPagerAdapter = new LoadsPagerAdapter(getSupportFragmentManager(), bundle);

        // Set up the ViewPager with the sections adapter.
        /*
      The {@link ViewPager} that will host the section contents.
     */
        ViewPager mViewPager = findViewById(R.id.container);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

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
                Intent intent = new Intent(LoadsActivity.this, Preferences.class);
                startActivity(intent);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
