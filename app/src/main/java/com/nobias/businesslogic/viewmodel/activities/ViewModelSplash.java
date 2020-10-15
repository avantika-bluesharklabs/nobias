package com.nobias.businesslogic.viewmodel.activities;

import com.nobias.MyApplication;
import com.nobias.businesslogic.pojo.PojoCommonResponse;
import com.nobias.businesslogic.viewmodel.ViewModelCommon;

/**
 * Created by Naria Sachin on Feb, 26 2020 12:10.
 */

public class ViewModelSplash extends ViewModelCommon<PojoCommonResponse> {
    public ViewModelSplash(MyApplication application) {
        super(application,false);
    }

    @Override
    public void networkCallData() {

    }

    @Override
    public void sendResponseBodyData(PojoCommonResponse pojoCommonResponse) {

    }
}
