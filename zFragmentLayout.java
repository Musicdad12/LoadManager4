package com.jrschugel.loadmanager;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import me.leolin.shortcutbadger.ShortcutBadger;

public class zFragmentLayout extends AppCompatActivity {

    public static String stLoadNumber;
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the layout for fragment_layout.xml
        setContentView(R.layout.zfragment_layout);

        stLoadNumber = "1829979";

        myDb = new DatabaseHelper(this);

        Integer LoadNumber = Integer.parseInt(stLoadNumber);

        Bundle bundle = new Bundle();
        bundle.putString("LoadNumber", LoadNumber.toString() );
        Loads_Frag_1 fragInfo = new Loads_Frag_1();
        fragInfo.setArguments(bundle);

        myDb.updateViewedLoad(LoadNumber);
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

}