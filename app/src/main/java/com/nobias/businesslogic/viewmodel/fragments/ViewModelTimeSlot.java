package com.nobias.businesslogic.viewmodel.fragments;

import android.text.TextUtils;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import androidx.databinding.ObservableList;

import com.nobias.MyApplication;
import com.nobias.R;
import com.nobias.businesslogic.interactors.SingleLiveEvent;
import com.nobias.businesslogic.pojo.PojoCommonResponse;
import com.nobias.businesslogic.pojo.PojoSaveRequest;
import com.nobias.businesslogic.viewmodel.ViewModelCommon;
import com.nobias.businesslogic.viewmodel.ViewModelRecyclerView;

import static com.nobias.utils.constants.RetrofitConstants.METHODD_GET_TIME_SLOT_POSTFIX;
import static com.nobias.utils.constants.RetrofitConstants.METHOD_GET_TIME_SLOT_CODE_PREFIX;
import static com.nobias.utils.constants.RetrofitConstants.METHOD_GET_TIME_SLOT_PREFIX;

/**
 * Created by Smit Shah on Aug 22 2018, 10:24 AM
 * <p>
 * View model for attendance log
 */
public class ViewModelTimeSlot extends ViewModelCommon<PojoCommonResponse> {
    public ObservableList<String> observerListTimeSlot = new ObservableArrayList<>();
    public ObservableBoolean observerSessionTopicErrorVisibility = new ObservableBoolean(false);
    public PojoSaveRequest mPojoSaveRequest;
    private SingleLiveEvent<String> liveEventSessionTopic = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> liveEventContinue = new SingleLiveEvent<>();

    public ObservableInt observerSessionTopicPosition = new ObservableInt(0);

    public SingleLiveEvent<String> getLiveEventSessionTopic() {
        return liveEventSessionTopic;
    }

    public SingleLiveEvent<Boolean> getLiveEventContinue() {
        return liveEventContinue;
    }

    public void onContinueClick() {
        boolean validateSessionTopic = validateSessionTopic();
        if (validateSessionTopic)
            liveEventContinue.call();
    }

    private boolean validateSessionTopic() {
        if (observerListTimeSlot.size() > 0) {
            observerSessionTopicErrorVisibility.set(false);
            return true;
        } else {
            observerSessionTopicErrorVisibility.set(true);
            return false;
        }
    }

    public ViewModelTimeSlot(MyApplication application) {
        super(application, false);
    }

    @Override
    public void networkCallData() {
        if (mPojoSaveRequest != null) {
            setProgressBar(true);
            String url = "";
            if (TextUtils.equals(mPojoSaveRequest.getNeedType(), mApplication.getString(R.string.needTypeCode))) {
                url = METHOD_GET_TIME_SLOT_CODE_PREFIX + mPojoSaveRequest.getConsultantId() +
                        METHODD_GET_TIME_SLOT_POSTFIX + mPojoSaveRequest.getSessionDate();
            } else {
                url = METHOD_GET_TIME_SLOT_PREFIX + mPojoSaveRequest.getConsultantId() +
                        METHODD_GET_TIME_SLOT_POSTFIX + mPojoSaveRequest.getSessionDate();
            }
            mApplication.getRetroFitInterface().getTimeSlot(mPreferences.getString(R.string.prefAccessToken), url).enqueue(mCallbackData);
        }
    }

    @Override
    public void sendResponseBodyData(PojoCommonResponse list) {
        if (list.getSuccess() && list.getAvailableTimes().size() > 0) {
            observerListTimeSlot.addAll(list.getAvailableTimes());
        } else {
            observerSnackBarInt.set(R.string.message_something_wrong);
        }
    }

    /**
     * Item selected listener for language selection
     */
    public void onItemSelectedSessionTopicSelection(int position) {
        if (position > 0)
            observerSessionTopicErrorVisibility.set(false);
        liveEventSessionTopic.setValue(observerListTimeSlot.get(position));
    }
}