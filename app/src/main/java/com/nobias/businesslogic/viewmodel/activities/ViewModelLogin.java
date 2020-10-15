package com.nobias.businesslogic.viewmodel.activities;

import android.text.TextUtils;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;

import com.nobias.BuildConfig;
import com.nobias.MyApplication;
import com.nobias.R;
import com.nobias.businesslogic.interactors.ObservableString;
import com.nobias.businesslogic.interactors.SingleLiveEvent;
import com.nobias.businesslogic.pojo.PojoCommonResponse;
import com.nobias.businesslogic.viewmodel.ViewModelCommon;


/**
 * Created by Brij Dholakia on Jul, 10 2018 18:40.
 * <p>
 * View model for login
 */
public class ViewModelLogin extends ViewModelCommon<PojoCommonResponse> {
    private boolean mIsResetPassword = false;
    public ObservableString observerEmail = new ObservableString("");
    public ObservableInt observerErrorEmail = new ObservableInt();
    public ObservableBoolean observerErrorEnabledEmail = new ObservableBoolean(false);

    public ObservableString observerPassword = new ObservableString("");
    public ObservableInt observerErrorPassword = new ObservableInt();
    public ObservableBoolean observerErrorEnabledPassword = new ObservableBoolean(false);

    private SingleLiveEvent<Boolean> liveEventLogin = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> liveEventTermsOfService = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> liveEventResetPassword = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> liveEventPrivacyPolicy = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> liveEventLoginWithFacebook = new SingleLiveEvent<>();
    private SingleLiveEvent<PojoCommonResponse> liveEventLoginSuccess = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> liveEventResetSuccess = new SingleLiveEvent();

    public ViewModelLogin(MyApplication application) {
        super(application, true);
    }

    public SingleLiveEvent<Boolean> getLiveEventLogin() {
        return liveEventLogin;
    }
    public SingleLiveEvent<Boolean> getLiveEventLoginWithFacebook() {
        return liveEventLoginWithFacebook;
    }
    public SingleLiveEvent<Boolean> getLiveEventTermsOfService() {
        return liveEventTermsOfService;
    }
    public SingleLiveEvent<Boolean> getLiveEventResetPassword() {
        return liveEventResetPassword;
    }
    public SingleLiveEvent<Boolean> getLiveEventPrivacyPolicy() {
        return liveEventPrivacyPolicy;
    }

    public SingleLiveEvent<PojoCommonResponse> getLiveEventLoginSuccess() {
        return liveEventLoginSuccess;
    }

    public SingleLiveEvent<Boolean> getLiveEventResetSuccess() {
        return liveEventResetSuccess;
    }

    private boolean validateUserData() {
        if (TextUtils.isEmpty(observerEmail.getTrimmed())) {
            observerErrorEnabledEmail.set(true);
            observerErrorEmail.set(R.string.error_blank_email);
            return false;
        } else {
            observerErrorEnabledEmail.set(false);
            return true;
        }
    }

    private boolean validatePassword() {
        if (TextUtils.isEmpty(observerPassword.getTrimmed())) {
            observerErrorEnabledPassword.set(true);
            observerErrorPassword.set(R.string.error_blank_password);
            return false;
        } /*else if (!Utils.isValidPassword(observerPassword.getTrimmed())) {
            observerErrorEnabledPassword.set(true);
            observerErrorPassword.set(R.string.error_invalid_password);
            return false;
        }*/ else {
            observerErrorEnabledPassword.set(false);
            return true;
        }
    }



    public void onLoginWithFacebookClick() {
        liveEventLoginWithFacebook.call();
    }

    public void onLoginClick() {
        mIsResetPassword = false;
        boolean validateEmail = validateUserData();
        boolean validatePassword = validatePassword();
        liveEventLogin.setValue(validateEmail && validatePassword);
        // liveEventLoginSuccess.setValue(null);
    }

    public void onTermsOfServiceClick(){
        liveEventTermsOfService.call();
    }

    public void onPrivacyPolicyClick(){
        liveEventPrivacyPolicy.call();
    }

    public void onResetPasswordClick(){
        if(validateUserData()) {
            mIsResetPassword = true;
            liveEventResetPassword.call();
        }
    }

    @Override
    public void networkCallData() {
        setIsToShowErrors(true);
        if(mIsResetPassword)
        {
            mApplication.getRetroFitInterface().resetPassword(
                    mApplication.getString(R.string.bearer) + " " + BuildConfig.APP_TOKEN,
                    BuildConfig.APP_TOKEN,
                    observerEmail.getTrimmed()).enqueue(mCallbackData);
        }else {
            mApplication.getRetroFitInterface().login(BuildConfig.APP_TOKEN,
                    observerEmail.getTrimmed(),
                    observerPassword.getTrimmed(),
                    mPreferences.getString(R.string.prefGcmToken)).enqueue(mCallbackData);
        }
    }

    @Override
    public void sendResponseBodyData(PojoCommonResponse data) {
        if (data.getSuccess()) {
            if(mIsResetPassword)
            {
                liveEventResetSuccess.call();
                mIsResetPassword = false;
            }else if(data.getAccessToken() != null)
                liveEventLoginSuccess.setValue(data);
            else {
                observerSnackBarInt.set(R.string.message_something_wrong);
            }
        } else {
            observerSnackBarInt.set(R.string.message_something_wrong);
        }
    }
}