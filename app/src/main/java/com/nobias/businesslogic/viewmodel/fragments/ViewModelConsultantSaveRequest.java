package com.nobias.businesslogic.viewmodel.fragments;

import android.text.TextUtils;

import androidx.databinding.ObservableBoolean;

import com.nobias.MyApplication;
import com.nobias.R;
import com.nobias.businesslogic.interactors.ObservableString;
import com.nobias.businesslogic.interactors.SingleLiveEvent;
import com.nobias.businesslogic.pojo.PojoCommonResponse;
import com.nobias.businesslogic.pojo.PojoSaveRequest;
import com.nobias.businesslogic.viewmodel.ViewModelCommon;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Smit Shah on Aug 22 2018, 10:24 AM
 * <p>
 * View model for attendance log
 */
public class ViewModelConsultantSaveRequest extends ViewModelCommon<PojoCommonResponse> {
    public PojoSaveRequest pojoSaveRequests = new PojoSaveRequest();
    public ObservableString observerNeeds = new ObservableString();
    public ObservableBoolean observerNeedsErrorVisibility = new ObservableBoolean(false);
    private SingleLiveEvent<Boolean> liveEventSaveRequest = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> liveEventSaveRequestSuccess = new SingleLiveEvent<>();

    public SingleLiveEvent<Boolean> getLiveEventSaveRequest() {
        return liveEventSaveRequest;
    }
    public SingleLiveEvent<Boolean> getLiveEventSaveRequestSuccess() {
        return liveEventSaveRequestSuccess;
    }

    public ViewModelConsultantSaveRequest(MyApplication application) {
        super(application, false);
    }

    private boolean validateNeeds()
    {
        if (TextUtils.isEmpty(observerNeeds.getTrimmed())) {
            observerNeedsErrorVisibility.set(true);
            return false;
        }  else {
            observerNeedsErrorVisibility.set(false);
            return true;
        }
    }
    public void onSaveRequestClick()
    {
        if(validateNeeds())
            liveEventSaveRequest.call();
    }

    @Override
    public void networkCallData() {
        setProgressBar(true);
        mApplication.getRetroFitInterface().userAppointment(mPreferences.getString(R.string.prefAccessToken),
                RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf(pojoSaveRequests.getConsultantId())),
                RequestBody.create(MediaType.parse("multipart/form-data"),pojoSaveRequests.getSessionDate()),
                RequestBody.create(MediaType.parse("multipart/form-data"),pojoSaveRequests.getSessionTimeSlot()),
                RequestBody.create(MediaType.parse("multipart/form-data"),pojoSaveRequests.getSessionCategory()),
                RequestBody.create(MediaType.parse("multipart/form-data"),observerNeeds.getTrimmed()))
                .enqueue(mCallbackData);
    }

    @Override
    public void sendResponseBodyData(PojoCommonResponse pojoCommonResponse) {
        if (pojoCommonResponse.getSuccess()) {
            liveEventSaveRequestSuccess.call();
        } else {
            observerSnackBarInt.set(R.string.message_something_wrong);
        }
    }
}