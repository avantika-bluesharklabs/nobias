package com.nobias.businesslogic.viewmodel.fragments;

import android.text.TextUtils;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import com.nobias.MyApplication;
import com.nobias.R;
import com.nobias.businesslogic.interactors.SingleLiveEvent;
import com.nobias.businesslogic.pojo.PojoCommonResponse;
import com.nobias.businesslogic.pojo.PojoSaveRequest;
import com.nobias.businesslogic.pojo.PojoUser;
import com.nobias.businesslogic.viewmodel.ViewModelRecyclerView;

import static com.nobias.utils.constants.RetrofitConstants.METHOD_GET_CONSULTANTS;

/**
 * Created by Smit Shah on Aug 22 2018, 10:24 AM
 * <p>
 * View model for attendance log
 */
public class ViewModelConsultants extends ViewModelRecyclerView<PojoCommonResponse, PojoUser> {
    public ObservableList<PojoUser> observerConsultantList = new ObservableArrayList<>();
    public PojoSaveRequest pojoSaveRequest = new PojoSaveRequest();
    private SingleLiveEvent<Boolean> liveEvent = new SingleLiveEvent<>();

    public SingleLiveEvent<Boolean> getLiveEvent() {
        return liveEvent;
    }

    public ViewModelConsultants(MyApplication application) {
        super(application, false);
        fetchDataList();
    }

    @Override
    public void refreshListUpdate() {
        observerContent.clear();
    }

    @Override
    public void networkCallList() {
        setProgressBar(true);
        String url = METHOD_GET_CONSULTANTS;
        if (TextUtils.equals(pojoSaveRequest.getNeedType(), mApplication.getString(R.string.needTypeNow))) {
            url += "?available_now=true";
            mApplication.getRetroFitInterface().getConsultants(mPreferences.getString(R.string.prefAccessToken), url)
                    .enqueue(mCallbackList);
        }
        else if (TextUtils.equals(pojoSaveRequest.getNeedType(), mApplication.getString(R.string.needTypeFuture)))
        {
            url += "?specialty=" + pojoSaveRequest.getSessionName() + "&category=" + pojoSaveRequest.getSessionCategory();
            mApplication.getRetroFitInterface().getConsultants(mPreferences.getString(R.string.prefAccessToken), url)
                    .enqueue(mCallbackList);
        }
    }

    @Override
    public void offlineDataList() {

    }

    @Override
    public void sendResponseBodyList(PojoCommonResponse list) {
        if (list.getSuccess()) {

            if (list.getPojoConsultant().size() > 0)
                observerContent.addAll(list.getPojoConsultant());
            else {
                liveEvent.call();
            }
        } else {
            observerSnackBarInt.set(R.string.message_something_wrong);
        }
    }
}