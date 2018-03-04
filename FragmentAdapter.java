package com.jrschugel.loadmanager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

class FragmentAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;

    public FragmentAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new Loads_Frag_1();
            case 1:
                return new Loads_Frag_2();
            case 2:
                return new Loads_Frag_3();
            case 3:
                return new Loads_Frag_4();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}