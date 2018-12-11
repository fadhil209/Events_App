package com.example.faadhil.events;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Faadhil on 11/24/2018.
 */

public class PageAdapter extends FragmentStatePagerAdapter {

    int mNoOfTabs;

    public PageAdapter(FragmentManager fm, int mNoOfTabs) {
        super(fm);
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
                }else{
                    AddEventsFragment fragment2 = new AddEventsFragment();
                    return fragment2;
                }
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }



//    public void clearAll() //Clear all page
//    {
//        for(int i=0; i<getCount(); i++)
//            this.destroyItem(null, i, this.getItem(i).getHost());
//    }
}

