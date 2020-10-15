package com.nobias.view.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.nobias.R;
import com.nobias.businesslogic.viewmodel.fragments.ViewModelSettings;
import com.nobias.databinding.FragmentSettingsBinding;
import com.nobias.utils.Utils;

/**
 * Created by Smit Shah on Aug 22 2018, 11:38 AM
 * <p>
 * Fragment for attendance log
 */
public class FragmentSettings extends FragmentBase {

    private ViewModelSettings mVMSettings;
    private View mParentView;
    private Switch mSwitchAllowEmailNotification;
    private Switch mSwitchSetAvailableNow;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentSettingsBinding binding = DataBindingUtil.inflate(inflater, R.layout
                .fragment_settings, container, false);
        mVMSettings = new ViewModelSettings(mApplication);
        binding.setVmSettings(mVMSettings);
        mParentView = binding.getRoot();
        mVMSettings.observableIsConsultant.set(mApplication.mIsConsultant);
        mSwitchAllowEmailNotification = (Switch) mParentView.findViewById(R.id.switch_allow_email);
        mSwitchSetAvailableNow = (Switch) mParentView.findViewById(R.id.switch_available_now);
        if(mApplication.getAppSharedPreferences().getBoolean(R.string.prefReceivesEmailNotification))
            mSwitchAllowEmailNotification.setChecked(true);
        else
            mSwitchAllowEmailNotification.setChecked(false);

        if(mApplication.getAppSharedPreferences().getBoolean(R.string.prefAvailableNow))
            mSwitchSetAvailableNow.setChecked(true);
        else
            mSwitchSetAvailableNow.setChecked(false);
        if(!TextUtils.isEmpty(mApplication.getAppSharedPreferences().getString(R.string.prefConsultantCode)))
            mVMSettings.observerConsultantCode.set(mApplication.getAppSharedPreferences().getString(R.string.prefConsultantCode));
        observerEvents();
        return binding.getRoot();
    }

    /**
     * Observes events provided by live data single events from login and forgot password view
     * models
     */
    private void observerEvents() {
        mVMSettings.getLiveEventLogout().observe(this, areContentsValid -> {
            Utils.hideKeyboard(mMainActivity);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mMainActivity);
            alertDialogBuilder.setMessage(getString(R.string.text_logoutconfirm));

            alertDialogBuilder.setPositiveButton(getString(R.string.message_yes),
                    (arg0, arg1) -> mMainActivity.logOut());

            alertDialogBuilder.setNegativeButton(getString(R.string.message_no),
                    (dialog, which) -> dialog.dismiss());

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        });

        mVMSettings.getLiveEventSuccess().observe(this, areContentsValid -> {
            Utils.hideKeyboard(mMainActivity);
            showSuccessDialog(areContentsValid);
        });

        mVMSettings.getLiveEventSetSpeciality().observe(this, areContentsValid -> {
            Utils.hideKeyboard(mMainActivity);
            Fragment fragment = new FragmentSetSpecialities();
            mMainActivity.addFragment(fragment, R.string.tagFragmentSetSpeciality, R.string.tagFragmentSetSpeciality);
        });

        mVMSettings.getLiveEventSetHours().observe(this, areContentsValid -> {
            Utils.hideKeyboard(mMainActivity);
            Fragment fragment = new FragmentSetHours();
            mMainActivity.addFragment(fragment, R.string.tagFragmentSetHours, R.string.tagFragmentSetHours);
        });

        mVMSettings.getLiveEventUpdateInfo().observe(this, areContentsValid -> {
            Utils.hideKeyboard(mMainActivity);
            Fragment fragment = new FragmentUpdateInfo();
            mMainActivity.addFragment(fragment, R.string.tagFragmentUpdateInfo, R.string.tagFragmentUpdateInfo);
        });

        mVMSettings.getLiveEventChangePassword().observe(this, areContentsValid -> {
            Utils.hideKeyboard(mMainActivity);
            Fragment fragment = new FragmentChangePassword();
            mMainActivity.addFragment(fragment, R.string.tagFragmentChangePassword, R.string.tagFragmentChangePassword);
        });

        mSwitchSetAvailableNow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    mVMSettings.availableNowStatus = "1";
                else
                    mVMSettings.availableNowStatus = "0";
                mVMSettings.networkCallType = "availableNow";
                mVMSettings.networkCallData();
            }
        });

        mSwitchAllowEmailNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    mVMSettings.emailNotificationStatus = "1";
                else
                    mVMSettings.emailNotificationStatus = "0";
                mVMSettings.networkCallType = "emailNotification";
                mVMSettings.networkCallData();
            }
        });
    }

    private void showSuccessDialog(String message) {
        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(mMainActivity);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton(getString(R.string.message_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setTitle(getString(R.string.title_user_update));
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
