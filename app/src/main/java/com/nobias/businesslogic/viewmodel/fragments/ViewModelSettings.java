package com.nobias.businesslogic.viewmodel.fragments;

import android.text.TextUtils;

import androidx.databinding.ObservableBoolean;

import com.nobias.MyApplication;
import com.nobias.R;
import com.nobias.businesslogic.interactors.ObservableString;
import com.nobias.businesslogic.interactors.SingleLiveEvent;
import com.nobias.businesslogic.pojo.PojoCommonResponse;
import com.nobias.businesslogic.viewmodel.ViewModelCommon;

/**
 * Created by Smit Shah on Aug 22 2018, 10:24 AM
 * <p>
 * View model for attendance log
 */
public class ViewModelSettings extends ViewModelCommon<PojoCommonResponse> {
    public ObservableBoolean observableIsConsultant = new ObservableBoolean();
    public ObservableString observerConsultantCode = new ObservableString("");
    private SingleLiveEvent<Boolean> liveEventSetSpeciality = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> liveEventSetHours = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> liveEventLogout = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> liveEventUpdateInfo = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> liveEventChangePassword = new SingleLiveEvent<>();
    private SingleLiveEvent<String> liveEventSuccess = new SingleLiveEvent<>();
    public String emailNotificationStatus = "", availableNowStatus = "", networkCallType = "";

    public SingleLiveEvent<Boolean> getLiveEventLogout() {
        return liveEventLogout;
    }
    public SingleLiveEvent<Boolean> getLiveEventSetSpeciality() {
        return liveEventSetSpeciality;
    }
    public SingleLiveEvent<Boolean> getLiveEventSetHours() {
        return liveEventSetHours;
    }
    public SingleLiveEvent<Boolean> getLiveEventUpdateInfo() {
        return liveEventUpdateInfo;
    }
    public SingleLiveEvent<Boolean> getLiveEventChangePassword() {
        return liveEventChangePassword;
    }
    public SingleLiveEvent<String> getLiveEventSuccess() {
        return liveEventSuccess;
    }

    public ViewModelSettings(MyApplication application) {
        super(application, false);
    }

    public void onLogoutClick()
    {
        liveEventLogout.setValue(true);
    }
    public void onSetSpecialityClick()
    {
        liveEventSetSpeciality.setValue(true);
    }
    public void onSetHoursClick()
    {
        liveEventSetHours.setValue(true);
    }
    public void onUpdateInfoClick()
    {
        liveEventUpdateInfo.setValue(true);
    }
    public void onChangePasswordClick()
    {
        liveEventChangePassword.setValue(true);
    }

    @Override
    public void networkCallData() {
        if(TextUtils.equals(networkCallType, "emailNotification") && !TextUtils.isEmpty(emailNotificationStatus)) {
            setProgressBar(true);
            mApplication.getRetroFitInterface().emailNotificationStatus(
                    mPreferences.getString(R.string.prefAccessToken),
                    emailNotificationStatus).enqueue(mCallbackData);
        }
        if(TextUtils.equals(networkCallType, "availableNow") && !TextUtils.isEmpty(availableNowStatus)) {
            setProgressBar(true);
            mApplication.getRetroFitInterface().consultantAvailableNow(
                    mPreferences.getString(R.string.prefAccessToken),
                    availableNowStatus).enqueue(mCallbackData);
        }
    }

    @Override
    public void sendResponseBodyData(PojoCommonResponse pojoCommonResponse) {
        if (pojoCommonResponse.getSuccess()) {
            networkCallType = "";
            liveEventSuccess.setValue(pojoCommonResponse.getMessage());
        } else {
            observerSnackBarInt.set(R.string.message_something_wrong);
        }
    }
}