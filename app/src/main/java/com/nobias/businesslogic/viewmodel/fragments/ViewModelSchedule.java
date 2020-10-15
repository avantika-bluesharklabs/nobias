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
import com.nobias.businesslogic.pojo.PojoSpeciality;
import com.nobias.businesslogic.viewmodel.ViewModelRecyclerView;

/**
 * Created by Smit Shah on Aug 22 2018, 10:24 AM
 * <p>
 * View model for attendance log
 */
public class ViewModelSchedule extends ViewModelRecyclerView<PojoCommonResponse, PojoCommonResponse> {
    public ObservableList<PojoSpeciality> observerListSessionTopic = new ObservableArrayList<>();
    public ObservableBoolean observerSessionTopicErrorVisibility = new ObservableBoolean(false);

    public ObservableString observerSessionDate = new ObservableString("");
    public ObservableInt observerErrorSessionDate = new ObservableInt();
    public ObservableBoolean observerErrorEnabledSessionDate = new ObservableBoolean(false);

    private SingleLiveEvent<String> liveEventSessionTopic = new SingleLiveEvent<>();
    private SingleLiveEvent<Void> liveEventDatePicker = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> liveEventContinue = new SingleLiveEvent<>();

    public ObservableInt observerSessionTopicPosition = new ObservableInt(0);

    public SingleLiveEvent<String> getLiveEventSessionTopic() {
        return liveEventSessionTopic;
    }
    public SingleLiveEvent<Void> getLiveEventDatePicker() {
        return liveEventDatePicker;
    }
    public SingleLiveEvent<Boolean> getLiveEventContinue() {
        return liveEventContinue;
    }


    public void onDateClick() {
        liveEventDatePicker.call();
    }

    public void onContinueClick() {
        boolean validateSessionDate = validateSessionDate();
        boolean validateSessionTopic = validateSessionTopic();
        if(validateSessionDate && validateSessionTopic)
            liveEventContinue.call();
    }

    private boolean validateSessionDate() {
        if (TextUtils.isEmpty(observerSessionDate.getTrimmed())) {
            observerErrorEnabledSessionDate.set(true);
            observerErrorSessionDate.set(R.string.error_blank_session_date);
            return false;
        } else {
            observerErrorEnabledSessionDate.set(false);
            return true;
        }
    }

    private boolean validateSessionTopic(){
        if(observerListSessionTopic.size() > 0) {
            observerSessionTopicErrorVisibility.set(false);
            return true;
        }
        else {
            observerSessionTopicErrorVisibility.set(true);
            return false;
        }
    }

    public ViewModelSchedule(MyApplication application) {
        super(application, false);
        fetchDataList();
    }

    @Override
    public void refreshListUpdate() {

    }

    @Override
    public void networkCallList() {
        setProgressBar(true);
        mApplication.getRetroFitInterface().getSpeciality(mPreferences.getString(R.string.prefAccessToken))
                .enqueue(mCallbackList);
    }

    @Override
    public void offlineDataList() {

    }

    @Override
    public void sendResponseBodyList(PojoCommonResponse list) {
        if (list.getSuccess() && list.getSpecialties().size() > 0) {
            observerListSessionTopic.addAll(list.getSpecialties());

        } else {
            observerSnackBarInt.set(R.string.message_something_wrong);
        }
    }

    /**
     * Item selected listener for language selection
     */
    public void onItemSelectedSessionTopicSelection(int position) {
        if(position > 0)
            observerSessionTopicErrorVisibility.set(false);
        liveEventSessionTopic.setValue(observerListSessionTopic.get(position).getName());
    }
}