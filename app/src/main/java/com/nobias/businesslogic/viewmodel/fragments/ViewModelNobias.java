package com.nobias.businesslogic.viewmodel.fragments;

import com.nobias.MyApplication;
import com.nobias.businesslogic.interactors.ObservableString;
import com.nobias.businesslogic.pojo.PojoCommonResponse;
import com.nobias.businesslogic.viewmodel.ViewModelCommon;

/**
 * Created by Smit Shah on Aug 22 2018, 10:24 AM
 * <p>
/ * View model for attendance log
 */
public class ViewModelNobias extends ViewModelCommon<PojoCommonResponse> {
    public ObservableString observerUserName = new ObservableString("");
    public ObservableString observerPic = new ObservableString("");
    public ObservableString observerTitle = new ObservableString("");

    public ViewModelNobias(MyApplication application)
    {
        super(application, false);
    }

    @Override
    public void networkCallData() {

    }

    @Override
    public void sendResponseBodyData(PojoCommonResponse pojoCommonResponse) {
    }
}