package com.nobias.view.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.nobias.R;
import com.nobias.businesslogic.viewmodel.activities.ViewModelSecurityInfo;
import com.nobias.databinding.ActivitySecurityInfoBinding;
import com.nobias.utils.Utils;

public class ActivitySecurityInfo extends ActivityBase {
    private ViewModelSecurityInfo mViewModelSecurityInfo;
    private String mUserType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySecurityInfoBinding mBinding = DataBindingUtil.setContentView(this, R.layout.activity_security_info);
        mViewModelSecurityInfo = new ViewModelSecurityInfo(mApplication);
        mBinding.setVmSecurityInfo(mViewModelSecurityInfo);
        mUserType = getIntent().getStringExtra("user_type");
        observerEvents();
    }

    /**
     * Observes events provided by live data single events from login and forgot password view
     * models
     */
    private void observerEvents() {
        mViewModelSecurityInfo.getLiveEventSecurityInfo().observe(this, areContentsValid -> {
            Utils.hideKeyboard(ActivitySecurityInfo.this);
            if (areContentsValid) {
                Intent intent = new Intent(ActivitySecurityInfo.this, ActivityPersonalInfo.class);
                intent.putExtra("password", mViewModelSecurityInfo.observerPassword.getTrimmed());
                intent.putExtra("confirm_password", mViewModelSecurityInfo.observerConfirmPassword.getTrimmed());
                intent.putExtra("user_type", mUserType);
                startActivity(intent);
                //overridePendingTransition(0, 0);
                //finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
