package com.nobias.businesslogic.viewmodel.fragments;

import androidx.databinding.ObservableBoolean;

import com.nobias.MyApplication;
import com.nobias.R;
import com.nobias.businesslogic.interactors.ObservableString;
import com.nobias.businesslogic.interactors.SingleLiveEvent;
import com.nobias.businesslogic.pojo.PojoCommonResponse;
import com.nobias.businesslogic.viewmodel.ViewModelCommon;
import com.nobias.utils.constants.RetrofitConstants;

/**
 * Created by Smit Shah on Aug 22 2018, 10:24 AM
 * <p>
 * View model for attendance log
 */
public class ViewModelStripe extends ViewModelCommon<PojoCommonResponse> {
    public String appointmentID = "";
    public ObservableString observableCardType = new ObservableString("");
    public ObservableString observableCardNumber = new ObservableString("");

    public ObservableBoolean observerCardAvailable = new ObservableBoolean(false);
    public ObservableBoolean observerIsCheckout = new ObservableBoolean(false);
    private SingleLiveEvent<String> liveEventSelectPaymentMethod = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> liveEventSave = new SingleLiveEvent<>();
    private SingleLiveEvent<String> liveEventTwilio = new SingleLiveEvent<>();
    public SingleLiveEvent<String> getSingleLiveEventTwilio() {
        return liveEventTwilio;
    }

    public SingleLiveEvent<String> getLiveEventSelectPaymentMethod() {
        return liveEventSelectPaymentMethod;
    }

    public SingleLiveEvent<Boolean> getLiveEventSave() {
        return liveEventSave;
    }

    public void onContinueClick() {
            liveEventSave.call();
    }
    public void onPaymentMethodClick() {
        liveEventSelectPaymentMethod.call();
    }

    public ViewModelStripe(MyApplication application) {
        super(application, false);
    }

    @Override
    public void networkCallData() {
        setProgressBar(true);
        String url = RetrofitConstants.METHOD_TWILIO_TOKEN_PREFIX + appointmentID + RetrofitConstants.METHOD_TWILIO_TOKEN_POSTFIX;
        mApplication.getRetroFitInterface().appointmentTwilioToken(mPreferences.getString(R.string.prefAccessToken),
                url).enqueue(mCallbackData);
    }

    @Override
    public void sendResponseBodyData(PojoCommonResponse pojoCommonResponse) {
        if (pojoCommonResponse.getSuccess()) {
            mApplication.setmTwilioToken(pojoCommonResponse.getTwilioToken());
            liveEventTwilio.setValue("");
        } else {
            observerSnackBarInt.set(R.string.message_something_wrong);
        }
    }
}