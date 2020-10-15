package com.nobias.businesslogic.viewmodel.fragments;

import android.text.TextUtils;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import androidx.databinding.ObservableList;

import com.nobias.MyApplication;
import com.nobias.R;
import com.nobias.businesslogic.interactors.ObservableString;
import com.nobias.businesslogic.interactors.SingleLiveEvent;
import com.nobias.businesslogic.pojo.PojoCommonResponse;
import com.nobias.businesslogic.viewmodel.ViewModelCommon;
import com.nobias.utils.UtilsDate;
import com.nobias.utils.constants.ConstantCodes;

import static com.nobias.utils.constants.RetrofitConstants.METHOD_APPOINTMENT_RESCHEDULE_POSTFIX;
import static com.nobias.utils.constants.RetrofitConstants.METHOD_APPOINTMENT_RESCHEDULE_PREFIX;

/**
 * Created by Smit Shah on Aug 22 2018, 10:24 AM
 * <p>
 * View model for attendance log
 */
public class ViewModelReschedule extends ViewModelCommon<PojoCommonResponse> {
    public ObservableList<String> observerListTimeSlot = new ObservableArrayList<>();
    public ObservableString observerDate = new ObservableString("");
    public ObservableInt observerErrorDate = new ObservableInt();
    public ObservableBoolean observerErrorEnabledDate = new ObservableBoolean(false);
    public String appointmentID = "";

    public ObservableBoolean observerTimeSlotErrorVisibility = new ObservableBoolean(false);
    private SingleLiveEvent<String> liveEventMessage = new SingleLiveEvent<>();
    private SingleLiveEvent<Void> liveEventDatePicker = new SingleLiveEvent<>();

    public ObservableInt observerTimeSlotPosition = new ObservableInt(0);

    public SingleLiveEvent<Void> getLiveEventDatePicker() {
        return liveEventDatePicker;
    }

    public SingleLiveEvent<String> getLiveEventMessage() {
        return liveEventMessage;
    }

    public void onContinueClick() {
        boolean time = validateTime();
        boolean date = validateDate();
        if (time && date)
            networkCallData();
    }

    public void onDateClick() {
        liveEventDatePicker.call();
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

    private boolean validateTime() {
        if (observerListTimeSlot.size() > 0) {
            observerTimeSlotErrorVisibility.set(false);
            return true;
        } else {
            observerTimeSlotErrorVisibility.set(true);
            return false;
        }
    }

    public ViewModelReschedule(MyApplication application) {
        super(application, false);
    }

    @Override
    public void networkCallData() {
        setProgressBar(true);
        String url = METHOD_APPOINTMENT_RESCHEDULE_PREFIX + appointmentID + METHOD_APPOINTMENT_RESCHEDULE_POSTFIX;
        String time = UtilsDate.changeDateformat(observerDate.getTrimmed(), ConstantCodes.DATE_FORMAT, ConstantCodes.DATE_FORMAT_API)
                + " " + UtilsDate.get24hourTimeFormat(observerListTimeSlot.get(observerTimeSlotPosition.get())) + ":00";
        mApplication.getRetroFitInterface().appointmentReschedule(mPreferences.getString(R.string.prefAccessToken), url,
                time).enqueue(mCallbackData);
    }

    @Override
    public void sendResponseBodyData(PojoCommonResponse pojoCommonResponse) {
        if (pojoCommonResponse.getSuccess()) {
            liveEventMessage.setValue(pojoCommonResponse.getMessage());
        } else {
            observerSnackBarInt.set(R.string.message_something_wrong);
        }
    }

    /**
     * Item selected listener for language selection
     */
    public void onItemSelectedSessionTopicSelection(int position) {
        if (position > 0)
            observerTimeSlotErrorVisibility.set(false);
        observerTimeSlotPosition.set(position);
    }
}