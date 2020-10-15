package com.nobias.view.activities;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.nobias.R;
import com.nobias.businesslogic.viewmodel.activities.ViewModelPrivacyPolicy;
import com.nobias.databinding.ActivityPrivacyPolicyBinding;

public class ActivityPrivacyPolicy extends ActivityBase {
    private ViewModelPrivacyPolicy mViewModelPrivacyPolicy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPrivacyPolicyBinding mBinding = DataBindingUtil.setContentView(this, R.layout.activity_privacy_policy);
        mViewModelPrivacyPolicy = new ViewModelPrivacyPolicy(mApplication);
        mBinding.setVmPrivacyPolicy(mViewModelPrivacyPolicy);
        observerEvents();
    }

    /**
     * Observes events provided by live data single events from login and forgot password view
     * models
     */
    private void observerEvents() {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
