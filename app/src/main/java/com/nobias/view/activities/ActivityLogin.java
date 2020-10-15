package com.nobias.view.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.nobias.R;
import com.nobias.businesslogic.pojo.PojoCommonResponse;
import com.nobias.businesslogic.viewmodel.activities.ViewModelLogin;
import com.nobias.databinding.ActivityLoginBinding;
import com.nobias.utils.Utils;

import java.util.Arrays;

import static com.nobias.utils.Logger.e;

public class ActivityLogin extends ActivityBase {
    private ViewModelLogin mViewModelLogin;
    private Switch mSwitchRemeberMe;
    private TextView mTxtCreateAccount;
    private CallbackManager mCallBackManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLoginBinding mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        mViewModelLogin = new ViewModelLogin(mApplication);
        mBinding.setVmLogin(mViewModelLogin);

        mViewModelLogin.observerEmail.set(mPreferences.getString(R.string.prefUserEmail));
        mViewModelLogin.observerPassword.set(mPreferences.getString(R.string.prefUserPassword));
        mSwitchRemeberMe = (Switch) findViewById(R.id.switch_remember_me);
        mTxtCreateAccount = (TextView) findViewById(R.id.txtCreateAccount);
        setupFacebookLogin();
        observerEvents();
    }

    /**
     * Observes events provided by live data single events from login and forgot password view
     * models
     */
    private void observerEvents() {
        mSwitchRemeberMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onRememberMeClicked(isChecked);
            }
        });

        mTxtCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityLogin.this, ActivityCreateAccount.class));
                overridePendingTransition(0, 0);
            }
        });

        mViewModelLogin.getLiveEventLogin().observe(this, areContentsValid -> {
            Utils.hideKeyboard(ActivityLogin.this);
            if (areContentsValid) {
                mViewModelLogin.getPostData();
            }
        });

        mViewModelLogin.getLiveEventLoginWithFacebook().observe(this, areContentsValid -> {
            Utils.hideKeyboard(ActivityLogin.this);
            LoginManager.getInstance().logInWithReadPermissions(ActivityLogin.this, Arrays.asList("public_profile"));
        });

        mViewModelLogin.getLiveEventTermsOfService().observe(this, areContentsValid -> {
            Utils.hideKeyboard(ActivityLogin.this);
            startActivity(new Intent(ActivityLogin.this, ActivityTermsOfService.class));
            overridePendingTransition(0, 0);
        });

        mViewModelLogin.getLiveEventResetPassword().observe(this, areContentsValid -> {
            Utils.hideKeyboard(ActivityLogin.this);
            showConfirmDialog();
        });

        mViewModelLogin.getLiveEventPrivacyPolicy().observe(this, areContentsValid -> {
            Utils.hideKeyboard(ActivityLogin.this);
            startActivity(new Intent(ActivityLogin.this, ActivityPrivacyPolicy.class));
            overridePendingTransition(0, 0);
        });
        mViewModelLogin.getLiveEventLoginSuccess().observe(
                this, this::saveUserData);

        mViewModelLogin.getLiveEventResetSuccess().observe(
                this, this::showResetPasswordSuccessDialog);
    }

    private void saveUserData(PojoCommonResponse data) {
        mApplication.saveUserData(data,false);
        onRememberMeClicked(mSwitchRemeberMe.isChecked());
        startActivity(new Intent(ActivityLogin.this, ActivityMain.class));
        overridePendingTransition(0, 0);
        finish();
    }

    private void onRememberMeClicked(Boolean isChecked) {
        if (isChecked) {
            mPreferences.setString(R.string.prefUserEmail, mViewModelLogin.observerEmail.getTrimmed());
            mPreferences.setString(R.string.prefUserPassword, mViewModelLogin.observerPassword.getTrimmed());
        } else {
            mPreferences.setString(R.string.prefUserEmail, "");
            mPreferences.setString(R.string.prefUserPassword, "");
        }
    }

    private void setupFacebookLogin() {
        mCallBackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallBackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Profile currentAccessToken = Profile.getCurrentProfile();
                Log.e("name", currentAccessToken.getFirstName());
            }

            @Override
            public void onCancel() {
                LoginManager.getInstance().logOut();
            }

            @Override
            public void onError(FacebookException error) {
                e("FB Error", error);
            }
        });
    }

    private void showConfirmDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityLogin.this);
        alertDialogBuilder.setMessage(getString(R.string.message_reset_password_confirm));
        alertDialogBuilder.setPositiveButton(getString(R.string.btn_continue), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                mViewModelLogin.networkCallData();
            }
        });
        alertDialogBuilder.setNegativeButton(getString(R.string.message_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setTitle(getString(R.string.title_reset_password));
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void showResetPasswordSuccessDialog(Boolean success) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityLogin.this);
        alertDialogBuilder.setMessage(getString(R.string.message_password_reset_success));
        alertDialogBuilder.setPositiveButton(getString(R.string.message_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setTitle(getString(R.string.title_reset_password_success));
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallBackManager.onActivityResult(requestCode, resultCode, data);
    }
}
