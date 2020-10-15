package com.nobias.view.activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.nobias.R;
import com.nobias.businesslogic.interfaces.OnClickNavHeader;
import com.nobias.businesslogic.viewmodel.activities.ViewModelMain;
import com.nobias.databinding.ActivityMainBinding;
import com.nobias.utils.Utils;
import com.nobias.view.fragments.FragmentConsultants;
import com.nobias.view.fragments.FragmentHome;

public class ActivityMain extends ActivityBase implements OnClickNavHeader {
    //private ActionBarDrawerToggle mDrawerToggle;
    private ActivityMainBinding mBinding;
    private ViewModelMain mVMMain;
    public boolean mIsFromStripe = false;
    public HeadsetPlugReceiver headsetPlugReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mVMMain = new ViewModelMain(mApplication);
        mBinding.setVmMain(mVMMain);
        navigateToHome();
       // mBinding.setOnClickNavHeader(this);
       // mBinding.setOnClickTabItem(mAddOnTabSelectedListerner);
       // mVMMain.setRequestOptionCentreProfilePic(mApplication.glideCenterCircle());
       setToolbar();
       mVMMain.networkCallData();

        headsetPlugReceiver = new HeadsetPlugReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.HEADSET_PLUG");
        registerReceiver(headsetPlugReceiver, intentFilter);
    }

    private void setToolbar() {
        setSupportActionBar(mBinding.toolbar.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    public void navigateToConsultants(){
        Fragment fragment = new FragmentConsultants();
        addFragment(fragment, R.string.tagFragmentConsultants, R.string.tagFragmentConsultants);
    }

    public void navigateToHome() {
        Fragment fragment = new FragmentHome();
        addFragment(fragment, R.string.tagFragmentHome, R.string.tagFragmentHome);
    }
    private void setAppTitle(String title) {
        mBinding.toolbar.toolbar.setTitle(title);
    }

    public void setAppTitle(int title) {
        mBinding.toolbar.toolbar.setTitle(getString(title));
    }

    public void visibilityRestrictiveProgressBar(boolean isToShow) {
        mVMMain.observerProgressBar.set(isToShow);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //getSupportFragmentManager().addOnBackStackChangedListener(mBackStackChangedListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
      //  getSupportFragmentManager().removeOnBackStackChangedListener(mBackStackChangedListener);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onClickNavHeader() {
        //addFragment(new FragmentProfile(), R.string.header_profile, R.string.tagProfile);
    }

    public void addFragment(Fragment fragment, int title, int tag) {
        addFragment(fragment, getString(title), getString(tag));
    }

    public void addFragment(Fragment fragment, String title, String tag) {
        getSupportFragmentManager().beginTransaction().add(R.id.frame_container, fragment, tag)
                .addToBackStack(tag).commit();
       /* if (!TextUtils.isEmpty(title)) {
            setAppTitle(title);
        }*/
    }

    private void removeBackStack() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
            fragmentManager.popBackStack();
        }
    }

    @Override
    protected void onDestroy() {
        if (headsetPlugReceiver != null) {
            unregisterReceiver(headsetPlugReceiver);
            headsetPlugReceiver = null;
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                fragmentBackPressed();
            } else {
                showExitDialog();
            }
    }

    private void showExitDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(getString(R.string.message_exitapp));

        alertDialogBuilder.setPositiveButton(getString(R.string.message_yes), (arg0, arg1) ->
                finish());

        alertDialogBuilder.setNegativeButton(getString(R.string.message_no), (dialog, which) ->
                dialog.dismiss());
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /**
     * Fragment back stack on device bck pressed button
     */
    private void fragmentBackPressed() {
        View view = getCurrentFragment().getActivity().getCurrentFocus();
        if (view != null) {
            Utils.hideKeyboard(this);
        }
        if(mIsFromStripe)
        {
            getSupportFragmentManager().popBackStack();
            mIsFromStripe = false;
        }
        super.onBackPressed();
    }

    private Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.frame_container);
    }

    public void logOut() {
        String deviceToken = mPreferences.getString(R.string.prefGcmToken);
        String email = mPreferences.getString(R.string.prefUserEmail);
        String password = mPreferences.getString(R.string.prefUserPassword);
        mPreferences.clearAllPreferences();
        mPreferences.setString(R.string.prefGcmToken, deviceToken);
        mPreferences.setString(R.string.prefUserEmail, email);
        mPreferences.setString(R.string.prefUserPassword, password);
        startActivity(new Intent(ActivityMain.this, ActivityLogin.class));
        overridePendingTransition(0, 0);
        finish();
    }

    public class HeadsetPlugReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
                return;
            }
            Intent i = new Intent(mApplication.getResources().getString(R.string.broadcastCameraPermissionTwilio));
            i.putExtra("isFromPermission", false);
            mApplication.getAppComponent().getLocalBroadcastManager().sendBroadcast(i);
        }
    }
}
