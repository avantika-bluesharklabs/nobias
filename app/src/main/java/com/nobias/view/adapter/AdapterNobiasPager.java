package com.nobias.view.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.nobias.view.fragments.FragmentPastConsultant;
import com.nobias.view.fragments.FragmentUpcomingConsultant;

/**
 * Created by Brij Dholakia on Jul, 31 2018 18:10.
 * <p>
 * Adapter for grievance pager
 */
public class AdapterNobiasPager extends FragmentStatePagerAdapter {
    private Fragment[] childFragments;
    private String[] mChildTitle;

    public AdapterNobiasPager(FragmentManager fm, Boolean isConsultant) {
        super(fm);
        childFragments = new Fragment[]{
                new FragmentPastConsultant(), //0
                new FragmentUpcomingConsultant(), //1
        };
        mChildTitle = new String[]{"past consultation", "upcoming consultations"};
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
