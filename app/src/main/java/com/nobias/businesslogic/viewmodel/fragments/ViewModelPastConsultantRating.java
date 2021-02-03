package com.nobias.businesslogic.viewmodel.fragments;

import androidx.databinding.ObservableInt;

import com.nobias.MyApplication;
import com.nobias.R;
import com.nobias.businesslogic.interactors.ObservableString;
import com.nobias.businesslogic.interactors.SingleLiveEvent;
import com.nobias.businesslogic.pojo.PojoCommonResponse;
import com.nobias.businesslogic.viewmodel.ViewModelCommon;
import com.nobias.businesslogic.viewmodel.ViewModelRecyclerView;

import static com.nobias.utils.constants.RetrofitConstants.METHOD_GET_APPOINTMENT;
import static com.nobias.utils.constants.RetrofitConstants.METHOD_RATE;

/**
 * Created by Smit Shah on Aug 22 2018, 10:24 AM
 * <p>
 * View model for attendance log
 */
public class ViewModelPastConsultantRating extends ViewModelCommon<PojoCommonResponse> {
    public ObservableInt observerRating = new ObservableInt();
    public ObservableString observerAppointmentId = new ObservableString();

    private SingleLiveEvent<Boolean> liveEventSave = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> liveEventSuccess = new SingleLiveEvent<>();

    public SingleLiveEvent<Boolean> getLiveEventSave() {
        return liveEventSave;
    }

    public SingleLiveEvent<Boolean> getLiveEventSuccess() {
        return liveEventSuccess;
    }

    public void onSaveClick() {
        liveEventSave.call();
    }

    public ViewModelPastConsultantRating(MyApplication application, String mAppointmentId, int mRating) {
        super(application, false);
        observerAppointmentId.set(mAppointmentId);
        observerRating.set(mRating);
    }

    @Override
    public void networkCallData() {
        setProgressBar(true);
        mApplication.getRetroFitInterface().ratePostConsultant(mPreferences.getString(R.string.prefAccessToken),
                METHOD_GET_APPOINTMENT + "/" + observerAppointmentId.getTrimmed() +
                        METHOD_RATE, String.valueOf(observerRating.get()))
                .enqueue(mCallbackData);

    }

    @Override
    public void sendResponseBodyData(PojoCommonResponse list) {
        if (list.getSuccess()) {
            liveEventSuccess.call();
        } else {
            observerSnackBarInt.set(R.string.message_something_wrong);
        }
    }
}