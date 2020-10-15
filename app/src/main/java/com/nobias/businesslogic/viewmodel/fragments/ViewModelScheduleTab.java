package com.nobias.businesslogic.viewmodel.fragments;

import android.text.TextUtils;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;

import com.nobias.MyApplication;
import com.nobias.R;
import com.nobias.businesslogic.interactors.ObservableString;
import com.nobias.businesslogic.interactors.SingleLiveEvent;
import com.nobias.businesslogic.pojo.PojoCommonResponse;
import com.nobias.businesslogic.viewmodel.ViewModelRecyclerView;

/**
 * Created by Smit Shah on Aug 22 2018, 10:24 AM
 * <p>
 * View model for attendance log
 */
public class ViewModelScheduleTab extends ViewModelRecyclerView<PojoCommonResponse, PojoCommonResponse> {
    public ObservableString observerConsultantCode = new ObservableString("");
    public ObservableString observerDate = new ObservableString("");

    public ObservableInt observerErrorConsultantCode = new ObservableInt();
    public ObservableInt observerErrorDate = new ObservableInt();

    public ObservableBoolean observerErrorEnabledConsultantCode = new ObservableBoolean(false);
    public ObservableBoolean observerErrorEnabledDate = new ObservableBoolean(false);

    private SingleLiveEvent<Void> liveEventDatePicker = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> liveEventNeedHelp = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> liveEventWantConsultant = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> liveEventSubmit = new SingleLiveEvent<>();

    public SingleLiveEvent<Boolean> getLiveEventNeedHelp() {
        return liveEventNeedHelp;
    }
    public SingleLiveEvent<Boolean> getLiveEventWantConsultant() {
        return liveEventWantConsultant;
    }
    public SingleLiveEvent<Boolean> getLiveEventSubmit() {
        return liveEventSubmit;
    }
    public SingleLiveEvent<Void> getLiveEventDatePicker() {
        return liveEventDatePicker;
    }

    public ViewModelScheduleTab(MyApplication application) {
        super(application, false);
    }

    public void onNeedHelpClick()
    {
        liveEventNeedHelp.call();
    }

    public void onWantConsultantClick()
    {
        liveEventWantConsultant.call();
    }

    public void onDateClick() {
        liveEventDatePicker.call();
    }

    private boolean validateConsultantCode() {
        if (TextUtils.isEmpty(observerConsultantCode.getTrimmed())) {
            observerErrorEnabledConsultantCode.set(true);
            observerErrorConsultantCode.set(R.string.error_blank_consultant_code);
            return false;
        } else {
            observerErrorEnabledConsultantCode.set(false);
            return true;
        }
    }

    private boolean validateDate() {
        if (TextUtils.isEmpty(observerDate.getTrimmed())) {
            observerErrorEnabledDate.set(true);
            observerErrorDate.set(R.string.error_blank_date);
            return false;
        } else {
            observerErrorEnabledDate.set(false);
            return true;
        }
    }

    public void onSubmitClick() {
        boolean consultantCode = validateConsultantCode();
        boolean date = validateDate();
        if(consultantCode && date)
            liveEventSubmit.setValue(consultantCode && date);
    }

    @Override
    public void refreshListUpdate() {

    }

    @Override
    public void networkCallList() {
    }

    @Override
    public void offlineDataList() {
    }

    @Override
    public void sendResponseBodyList(PojoCommonResponse list) {
    }
}