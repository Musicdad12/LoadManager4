package com.jrschugel.loadmanager;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by seanm on 8/24/2017.
 * Copyright 2017. All rights reserved.
 */

public class LoadsPagerAdapterFragments extends FragmentStatePagerAdapter {

        private final Bundle fragmentBundle;
        Context context;

        LoadsPagerAdapterFragments(Context context, FragmentManager fm, Bundle data) {
            super(fm);
            fragmentBundle = data;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    Loads_Fragment_Info tab1 = new Loads_Fragment_Info();
                    tab1.setArguments(this.fragmentBundle);
                    return tab1;
                case 1:
                    Loads_Fragment_Shipper tab2 = new Loads_Fragment_Shipper();
                    tab2.setArguments(this.fragmentBundle);
                    return tab2;
                case 2:
                    Loads_Fragment_Consignee tab3 = new Loads_Fragment_Consignee();
                    tab3.setArguments(this.fragmentBundle);
                    return tab3;
                case 3:
                    Loads_Fragment_Expenses tab4 = new Loads_Fragment_Expenses();
                    tab4.setArguments(this.fragmentBundle);
                    return tab4;
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