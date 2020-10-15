package com.nobias.view.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.nobias.R;
import com.nobias.businesslogic.viewmodel.fragments.ViewModelHome;
import com.nobias.databinding.FragmentHomeBinding;
import com.nobias.view.adapter.AdapterHomePager;

/**
 * Created by Avantika Gadhiya on Apr, 16 2020 5:30.
 */

public class FragmentHome extends FragmentBase {
    private FragmentHomeBinding mBinding;
    private ViewModelHome mViewModelHome;
    private TabLayout mTabs;
    private ViewPager mViewPager;
    private View mParentView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        mViewModelHome = new ViewModelHome(mApplication);
        mBinding.setViewModel(mViewModelHome);
        mParentView = mBinding.getRoot();
        mBroadcastManager.registerReceiver(mReceiverChangePageIndex, new IntentFilter
                (getResources().getString(R.string.broadcastNobiasChangeIndex)));
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initComponents();
        setEventListener();
    }

    private void initComponents() {
        mViewPager = mParentView.findViewById(R.id.pager);
        mTabs = mParentView.findViewById(R.id.tabs);
        mViewPager.setAdapter(new AdapterHomePager(getChildFragmentManager()));
        mTabs.setupWithViewPager(mViewPager);

        mTabs.getTabAt(1).setIcon(R.drawable.tab_nobias);
        mTabs.getTabAt(2).setIcon(R.drawable.tab_settings);

        View tab = ((ViewGroup) mTabs.getChildAt(0)).getChildAt(0);
        if (mApplication.mIsConsultant) {
            tab.setEnabled(false);
            mTabs.getTabAt(0).setIcon(R.drawable.tab_schedule_disable);
        }
        else {
            tab.setEnabled(true);
            mTabs.getTabAt(0).setIcon(R.drawable.tab_schedule);
        }

        mViewPager.setCurrentItem(1);
    }


    /**
     * Observe for action supported to Profile Data
     */
    private void setEventListener() {

    }

    private final BroadcastReceiver mReceiverChangePageIndex = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            mViewPager.setCurrentItem(1);
            Intent i = new Intent(mApplication.getResources().getString(R
                    .string.broadcastNobiasRefreshList));
            i.putExtra(mApplication.getResources().getString(R.string.bundleIsFromCalendarPermission),false);
            mApplication.getAppComponent().getLocalBroadcastManager().sendBroadcast(i);
        }
    };

    @Override
    public void onDestroyView()
    {
        mBroadcastManager.unregisterReceiver(mReceiverChangePageIndex);
        super.onDestroyView();
    }

}
