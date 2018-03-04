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

public class Loads_Details_Fragment extends Fragment {

    DatabaseHelper myDb;
    private INavActivity mINavActivity;

    // Session Manager Class
    SessionManager session;
    String LoadNumber;
    Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mINavActivity = (INavActivity) getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            LoadNumber = bundle.getString("frag_load_number");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_loads_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);

        context = getActivity();
        myDb = new DatabaseHelper(context);

        Bundle bundle = new Bundle();
        bundle.putString("LoadNumber", LoadNumber);
        Loads_Fragment_Info fragInfo = new Loads_Fragment_Info();
        fragInfo.setArguments(bundle);

        myDb.updateViewedLoad(Integer.parseInt(LoadNumber));
        Integer NewLoads = myDb.NotificationLoadCount();
        ShortcutBadger.applyCount(context, NewLoads);

        LoadsPagerAdapterFragments mSectionsPagerAdapter = new LoadsPagerAdapterFragments(getActivity(), getChildFragmentManager(), bundle);

        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = rootView.findViewById(R.id.container);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }



    // Create a DetailsFragment that contains the hero data for the correct index
    public static Loads_Details_Fragment newInstance(String loadnumber) {
        Loads_Details_Fragment f = new Loads_Details_Fragment();

        // Bundles are used to pass data using a key "index" and a value
        Bundle args = new Bundle();
        args.putString("frag_load_number", loadnumber);

        // Assign key value to the fragment
        f.setArguments(args);

        return f;
    }
}
