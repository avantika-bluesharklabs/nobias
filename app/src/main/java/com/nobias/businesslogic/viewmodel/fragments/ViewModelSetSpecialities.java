package com.nobias.businesslogic.viewmodel.fragments;

import androidx.databinding.ObservableInt;

import com.nobias.MyApplication;
import com.nobias.R;
import com.nobias.businesslogic.interactors.SingleLiveEvent;
import com.nobias.businesslogic.pojo.PojoCommonResponse;
import com.nobias.businesslogic.pojo.PojoSpeciality;
import com.nobias.businesslogic.viewmodel.ViewModelRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Smit Shah on Aug 22 2018, 10:24 AM
 * <p>
 * View model for attendance log
 */
public class ViewModelSetSpecialities extends ViewModelRecyclerView<PojoCommonResponse, PojoSpeciality> {
    public ObservableInt observerPosition = new ObservableInt(0);
    private SingleLiveEvent<String> singleLiveEventSave = new SingleLiveEvent<>();
    private boolean isSaveClick = false;
    private List<Integer> listSelected = new ArrayList<>();

    public SingleLiveEvent<String> getSingleLiveEventSave(){return  singleLiveEventSave;}

    public ViewModelSetSpecialities(MyApplication application) {
        super(application, false);
        fetchDataList();
    }

    public void onSaveClick()
    {
        isSaveClick = true;
        for(int i =0 ;i < observerContent.size(); i++)
        {
            if(observerContent.get(i).getSelected()) {
                listSelected.add(observerContent.get(i).getId());
            }
        }
        networkCallList();
    }

    @Override
    public void refreshListUpdate() {

    }

    @Override
    public void networkCallList() {
        setProgressBar(true);
        if(!isSaveClick) {
            mApplication.getRetroFitInterface().getSpeciality(mPreferences.getString(R.string.prefAccessToken))
                    .enqueue(mCallbackList);
        }
        else
        {
            mApplication.getRetroFitInterface().consultantSetSpeciality(mPreferences.getString(R.string.prefAccessToken),
                    listSelected).enqueue(mCallbackList);
        }
    }

    @Override
    public void offlineDataList() {

    }

    @Override
    public void sendResponseBodyList(PojoCommonResponse list) {
        if (list.getSuccess() ){
            if(!isSaveClick && list.getSpecialties().size() > 0) {
                observerContent.addAll(list.getSpecialties());
                if(mApplication.getConsultantSpecialities().size() > 0)
                {
                    for(int i =0; i< mApplication.getConsultantSpecialities().size(); i++)
                    {
                        for(int j=0; j < observerContent.size(); j++)
                        {
                            if(mApplication.getConsultantSpecialities().get(i).getId() == observerContent.get(j).getId())
                            {
                                observerContent.get(j).setSelected(true);
                            }
                        }
                    }
                }
            }
            else if(isSaveClick)
            {
                isSaveClick = false;
                mApplication.getConsultantSpecialities().clear();
                List<PojoSpeciality> specialities = new ArrayList<>();
                for(int i=0; i < observerContent.size(); i++)
                {
                    if(observerContent.get(i).getSelected())
                    {
                        specialities.add(observerContent.get(i));
                    }
                }
                mApplication.setConsultantSpecialities(specialities);
                singleLiveEventSave.setValue(list.getMessage());
            }
        } else {
            observerSnackBarInt.set(R.string.message_something_wrong);
        }
    }
}