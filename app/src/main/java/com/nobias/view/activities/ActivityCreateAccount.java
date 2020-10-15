package com.nobias.view.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.nobias.R;
import com.nobias.businesslogic.viewmodel.activities.ViewModelCreateAccount;
import com.nobias.databinding.ActivityCreateAccountBinding;
import com.nobias.utils.Utils;

public class ActivityCreateAccount extends ActivityBase {
    private ViewModelCreateAccount mViewModelCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCreateAccountBinding mBinding = DataBindingUtil.setContentView(this, R.layout.activity_create_account);
        mViewModelCreateAccount = new ViewModelCreateAccount(mApplication);
        mBinding.setVmCreateAccount(mViewModelCreateAccount);
        observerEvents();
    }

    /**
     * Observes events provided by live data single events from login and forgot password view
     * models
     */
    private void observerEvents() {
        mViewModelCreateAccount.getLiveEventUser().observe(this, areContentsValid -> {
            Utils.hideKeyboard(ActivityCreateAccount.this);
            if (areContentsValid) {
                Intent intent = new Intent(ActivityCreateAccount.this, ActivitySecurityInfo.class);
                intent.putExtra("user_type", getResources().getString(R.string.user));
                startActivity(intent);
            }
        });

        mViewModelCreateAccount.getLiveEventConsultant().observe(this, areContentsValid -> {
            Utils.hideKeyboard(ActivityCreateAccount.this);
            if (areContentsValid) {
                Intent intent = new Intent(ActivityCreateAccount.this, ActivitySecurityInfo.class);
                intent.putExtra("user_type", getResources().getString(R.string.consultant));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
