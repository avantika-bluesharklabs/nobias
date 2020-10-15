package com.nobias.view.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.nobias.R;
import com.nobias.businesslogic.viewmodel.fragments.ViewModelNobias;
import com.nobias.databinding.FragmentNobiasBinding;
import com.nobias.view.adapter.AdapterNobiasPager;

/**
 * Created by Smit Shah on Aug 22 2018, 11:38 AM
 * <p>
 * Fragment for attendance log
 */
public class FragmentNobias extends FragmentBase{
    private ViewModelNobias mVMNobias;
    private TabLayout mConsultantTabs;
    private ViewPager mViewPager;
    private View mParentView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentNobiasBinding binding = DataBindingUtil.inflate(inflater, R.layout
                .fragment_nobias, container, false);
        mVMNobias = new ViewModelNobias(mApplication);
        binding.setVmNobias(mVMNobias);
        mParentView = binding.getRoot();
        mBroadcastManager.registerReceiver(mReceiverRefreshUserData, new IntentFilter
                (getResources().getString(R.string.broadcastUpdateUserInfo)));
        initComponents();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView()
    {
        mBroadcastManager.unregisterReceiver(mReceiverRefreshUserData);
        super.onDestroyView();
    }

    private void initComponents() {
        showUserDetails();

        mConsultantTabs = mParentView.findViewById(R.id.tabsConsultant);
        mViewPager = mParentView.findViewById(R.id.nobiasPager);
        mViewPager.setAdapter(new AdapterNobiasPager(mMainActivity.getSupportFragmentManager(), mApplication.mIsConsultant));
        mConsultantTabs.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(1);
    }

    private void showUserDetails() {
        if(!TextUtils.isEmpty(mPreferences.getString(R.string.prefName)))
            mVMNobias.observerUserName.set(mPreferences.getString(R.string.prefName));

        if(!TextUtils.isEmpty(mPreferences.getString(R.string.prefProfileThumbPath)))
            mVMNobias.observerPic.set(mPreferences.getString(R.string.prefProfileThumbPath));

        if(!TextUtils.isEmpty(mPreferences.getString(R.string.prefTitle)))
            mVMNobias.observerTitle.set(mPreferences.getString(R.string.prefTitle));
    }

    private final BroadcastReceiver mReceiverRefreshUserData = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            showUserDetails();
        }
    };
}
