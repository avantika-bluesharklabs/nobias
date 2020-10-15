package com.nobias.view.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.nobias.view.fragments.FragmentNobias;
import com.nobias.view.fragments.FragmentScheduleTab;
import com.nobias.view.fragments.FragmentSettings;

/**
 * Created by Brij Dholakia on Jul, 31 2018 18:10.
 * <p>
 * Adapter for grievance pager
 */
public class AdapterHomePager extends FragmentPagerAdapter {
    private Fragment[] childFragments;
    private String[] mChildTitle;

    public AdapterHomePager(FragmentManager fm) {
        super(fm);

        childFragments = new Fragment[]{
                new FragmentScheduleTab(), //0
                new FragmentNobias(), //1
                new FragmentSettings() //2
        };
        mChildTitle = new String[]{"Schedule", "", "Settings"};
    }

    @Override
    public Fragment getItem(int position) {
        return childFragments[position];
    }

    @Override
    public int getCount() {
        return childFragments.length; //3 items
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mChildTitle[position];
    }
}
