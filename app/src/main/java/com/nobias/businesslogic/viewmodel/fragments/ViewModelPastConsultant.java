package com.nobias.businesslogic.viewmodel.fragments;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import com.nobias.MyApplication;
import com.nobias.R;
import com.nobias.businesslogic.pojo.PojoCommonResponse;
import com.nobias.businesslogic.pojo.PojoPastConsultants;
import com.nobias.businesslogic.viewmodel.ViewModelRecyclerView;

/**
 * Created by Smit Shah on Aug 22 2018, 10:24 AM
 * <p>
 * View model for attendance log
 */
public class ViewModelPastConsultant extends ViewModelRecyclerView<PojoCommonResponse, PojoPastConsultants> {
    public ObservableList<PojoPastConsultants> observerPastConsultantList = new ObservableArrayList<>();
    public int observerPosition;

    public ViewModelPastConsultant(MyApplication application) {
        super(application, false);
        networkCallList();
    }

    @Override
    public void refreshListUpdate() {
        networkCallList();
    }

    @Override
    public void networkCallList() {
        setProgressBar(true);
        mApplication.getRetroFitInterface().getAppointment(mPreferences.getString(R.string.prefAccessToken))
                .enqueue(mCallbackList);
    }

    @Override
    public void offlineDataList() {

    }

    @Override
    public void sendResponseBodyList(PojoCommonResponse pojoCommonResponse) {
        if (pojoCommonResponse.getSuccess()) {
            if (pojoCommonResponse.getPastAppointments().size() > 0) {
                observerPastConsultantList.clear();
                observerContent.clear();
                observerPastConsultantList.addAll(pojoCommonResponse.getPastAppointments());
                observerContent.addAll(pojoCommonResponse.getPastAppointments());
            }
            if (pojoCommonResponse.getUser().getSpecialities() != null &&
                    pojoCommonResponse.getUser().getSpecialities().size() > 0) {
                mApplication.setConsultantSpecialities(pojoCommonResponse.getUser().getSpecialities());
            }
            if (pojoCommonResponse.getUser().getAvailabilities() != null &&
                    pojoCommonResponse.getUser().getAvailabilities().size() > 0) {
                mApplication.setConsultantAvailability(pojoCommonResponse.getUser().getAvailabilities());
            }
        } else {
            observerSnackBarInt.set(R.string.message_something_wrong);
        }
    }
}