package com.jrschugel.loadmanager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class xLoadsActivityFrag extends Fragment {

    DatabaseHelper myDb;

    // Session Manager Class
    SessionManager session;
    String mCurSelectedLoad;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.xactivity_loads, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        myDb = new DatabaseHelper(getActivity());

        mCurSelectedLoad = getShownLoad();

        Bundle bundle = new Bundle();
        bundle.putString("LoadNumber", LoadsListAdapter.mCurrentLoadNumber );
        Loads_Frag_1 fragInfo = new Loads_Frag_1();
        fragInfo.setArguments(bundle);

        //myDb.updateViewedLoad(LoadNumber, 1);
        //Integer NewLoads = myDb.NotificationLoadCount();
        //ShortcutBadger.applyCount(getActivity(), NewLoads); //for 1.1.4+

        LoadsPagerAdapter mSectionsPagerAdapter = new LoadsPagerAdapter(getFragmentManager(), bundle);

        ViewPager mViewPager = getView().findViewById(R.id.container);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = getView().findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    // Create a DetailsFragment that contains the hero data for the correct index
    public static xLoadsActivityFrag newInstance(String loadnumber) {
        xLoadsActivityFrag f = new xLoadsActivityFrag();

        // Bundles are used to pass data using a key "index" and a value
        Bundle args = new Bundle();
        args.putString("loadnumber", loadnumber);

        // Assign key value to the fragment
        f.setArguments(args);

        return f;
    }

    public int getShownIndex() {

        // Returns the index assigned
        return getArguments().getInt("index", 0);
    }

    public String getShownLoad() {

        // Returns the index assigned
        return LoadsListAdapter.mCurrentLoadNumber;
    }

}
