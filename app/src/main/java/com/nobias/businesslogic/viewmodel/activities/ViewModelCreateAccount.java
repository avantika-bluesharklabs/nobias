package com.nobias.businesslogic.viewmodel.activities;

import com.nobias.MyApplication;
import com.nobias.businesslogic.interactors.SingleLiveEvent;
import com.nobias.businesslogic.pojo.PojoUser;
import com.nobias.businesslogic.viewmodel.ViewModelCommon;


/**
 * Created by Brij Dholakia on Jul, 10 2018 18:40.
 * <p>
 * View model for login
 */
public class ViewModelCreateAccount extends ViewModelCommon<PojoUser> {
    private SingleLiveEvent<Boolean> liveEventUser = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> liveEventConsultant = new SingleLiveEvent<>();

    public ViewModelCreateAccount(MyApplication application) {
        super(application, true);
    }

    public SingleLiveEvent<Boolean> getLiveEventUser() {
        return liveEventUser;
    }
    public SingleLiveEvent<Boolean> getLiveEventConsultant() {
        return liveEventConsultant;
    }

    public void onIAmUserClick() {
        liveEventUser.setValue(true);
    }

    public void onIAmConsultantClick() {
        liveEventConsultant.setValue(true);
    }

    @Override
    public void networkCallData() {
    }

    @Override
    public void sendResponseBodyData(PojoUser data) {
    }
}