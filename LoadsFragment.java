package com.jrschugel.loadmanager;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.leolin.shortcutbadger.ShortcutBadger;

public class LoadsFragment extends Fragment {

    DatabaseHelper myDb;
    Context context;
    // Session Manager Class
    SessionManager session;
    View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView != null){
            if(rootView.getParent()!=null)
                ((ViewGroup)rootView.getParent()).removeView(rootView);
            return rootView;
        }
        rootView = inflater.inflate(R.layout.activity_loads, container, false);
        context = getActivity();

        myDb = new DatabaseHelper(context);

        //Integer LoadNumber = Integer.parseInt(LoadsActivity2.stLoadNumber);
        String LoadNumber = LoadsListAdapter.mCurrentLoadNumber;

        Bundle bundle = new Bundle();
        bundle.putString("LoadNumber", LoadNumber);
        Loads_Fragment_Info fragInfo = new Loads_Fragment_Info();
        fragInfo.setArguments(bundle);

        myDb.updateViewedLoad(Integer.parseInt(LoadNumber));
        Integer NewLoads = myDb.NotificationLoadCount();
        ShortcutBadger.applyCount(context, NewLoads); //for 1.1.4+

       /*
      The {@link android.support.v4.view.PagerAdapter} that will provide
      fragments for each of the sections. We use a
      {@link FragmentPagerAdapter} derivative, which will keep every
      loaded fragment in memory. If this becomes too memory intensive, it
      may be best to switch to a
      {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
        LoadsPagerAdapterFragments mSectionsPagerAdapter = new LoadsPagerAdapterFragments(context, getChildFragmentManager(), bundle);

        // Set up the ViewPager with the sections adapter.
        /*
      The {@link ViewPager} that will host the section contents.
     */
        ViewPager mViewPager = rootView.findViewById(R.id.container);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        return rootView;
    }
}
