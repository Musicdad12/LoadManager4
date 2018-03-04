package com.jrschugel.loadmanager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by seanm on 8/24/2017.
 * Copyright 2017. All rights reserved.
 */

public class LoadsPagerAdapter extends FragmentStatePagerAdapter {

        private final Bundle fragmentBundle;

        LoadsPagerAdapter(FragmentManager fm, Bundle data) {
            super(fm);
            fragmentBundle = data;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    //tab1.setArguments(this.fragmentBundle);
                    return new Loads_Frag_1();
                case 1:
                    //tab2.setArguments(this.fragmentBundle);
                    return new Loads_Frag_2();
                case 2:
                    //tab3.setArguments(this.fragmentBundle);
                    return new Loads_Frag_3();
                case 3:
                    //tab4.setArguments(this.fragmentBundle);
                    return new Loads_Frag_4();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Load Info";
                case 1:
                    return "Shipper";
                case 2:
                    return "Receiver";
                case 3:
                    return "Expenses";
            }
            return null;
        }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


}