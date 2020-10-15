package com.nobias.businesslogic.viewmodel.fragments;

import androidx.databinding.ObservableBoolean;

import com.nobias.MyApplication;
import com.nobias.R;
import com.nobias.businesslogic.interactors.SingleLiveEvent;
import com.nobias.businesslogic.pojo.PojoCommonResponse;
import com.nobias.businesslogic.viewmodel.ViewModelCommon;


/**
 * Created by Brij Dholakia on Jul, 10 2018 18:40.
 * <p>
 * View model for login
 */
public class ViewModelTwilio extends ViewModelCommon<PojoCommonResponse> {
    public ObservableBoolean observableTwilioConnected = new ObservableBoolean(false);
    public ObservableBoolean observableMute = new ObservableBoolean(false);
    private SingleLiveEvent<String> liveEventMute = new SingleLiveEvent<>();
    private SingleLiveEvent<String> liveEventDisconnect = new SingleLiveEvent<>();

    public ViewModelTwilio(MyApplication application) {
        super(application, true);
    }

    public SingleLiveEvent<String> getLiveEventMute() {
        return liveEventMute;
    }
    public SingleLiveEvent<String> getLiveEventDisconnect() {
        return liveEventDisconnect;
    }

    public void onMuteClick() {
        liveEventMute.call();
    }

    public void onDisconnectClick() {
        liveEventDisconnect.call();
    }
    @Override
    public void networkCallData() {
    }

    @Override
    public void sendResponseBodyData(PojoCommonResponse pojoCommonResponse) {
        if (pojoCommonResponse.getSuccess()) {

        } else {
            observerSnackBarInt.set(R.string.message_something_wrong);
        }
    }
}