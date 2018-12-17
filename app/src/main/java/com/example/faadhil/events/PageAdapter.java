package com.example.faadhil.events;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Faadhil on 11/24/2018.
 */

public class PageAdapter extends FragmentStatePagerAdapter {

    Fragment fragment[];
    int mNoOfTabs;

    private String[] tabTitles = new String[]{"Events", "Add Events"};

    public PageAdapter(FragmentManager fm, int mNoOfTabs, Fragment[] fragment) {
        super(fm);
        this.mNoOfTabs = mNoOfTabs;
        this.fragment = fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return fragment[0];
            case 1:
                if (!Main2Activity.userboolean) {
                    return fragment[1];
                } else {
                    return fragment[2];
                }
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }

}

