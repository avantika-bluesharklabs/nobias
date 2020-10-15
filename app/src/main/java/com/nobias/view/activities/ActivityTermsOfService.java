package com.nobias.view.activities;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.nobias.R;
import com.nobias.businesslogic.viewmodel.activities.ViewModelTermsOfService;
import com.nobias.databinding.ActivityTermsServiceBinding;

public class ActivityTermsOfService extends ActivityBase {
    private ViewModelTermsOfService mViewModelTermsOfService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTermsServiceBinding mBinding = DataBindingUtil.setContentView(this, R.layout.activity_terms_service);
        mViewModelTermsOfService = new ViewModelTermsOfService(mApplication);
        mBinding.setVmTerm(mViewModelTermsOfService);
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
