package com.example.faadhil.events;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Faadhil on 11/24/2018.
 */

public class PageAdapter extends FragmentStatePagerAdapter {

    int mNoOfTabs;

    public PageAdapter(FragmentActivity activity, int mNoOfTabs) {
        super(activity.getSupportFragmentManager());
        this.mNoOfTabs = mNoOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                 EventsFragment fragment1 = new EventsFragment();
                return fragment1;
            case 1:
                if (!Main2Activity.userboolean) {
                    LogInFragment logInFragment = new LogInFragment();
                    return logInFragment;
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

