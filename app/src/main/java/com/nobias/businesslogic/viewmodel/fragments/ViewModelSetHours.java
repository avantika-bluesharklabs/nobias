package com.nobias.businesslogic.viewmodel.fragments;

import android.text.TextUtils;

import androidx.databinding.ObservableBoolean;

import com.google.gson.JsonObject;
import com.nobias.MyApplication;
import com.nobias.R;
import com.nobias.businesslogic.interactors.ObservableString;
import com.nobias.businesslogic.interactors.SingleLiveEvent;
import com.nobias.businesslogic.pojo.PojoSetHours;
import com.nobias.businesslogic.viewmodel.ViewModelCommon;
import com.nobias.utils.UtilsDate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Smit Shah on Aug 22 2018, 10:24 AM
 * <p>
 * View model for attendance log
 */
public class ViewModelSetHours extends ViewModelCommon<PojoSetHours> {
    public ObservableString observerMonTo = new ObservableString();
    public ObservableString observerMonFrom = new ObservableString();
    public ObservableString observerTuesTo = new ObservableString();
    public ObservableString observerTuesFrom = new ObservableString();
    public ObservableString observerWedTo = new ObservableString();
    public ObservableString observerWedFrom = new ObservableString();
    public ObservableString observerThursTo = new ObservableString();
    public ObservableString observerThursFrom = new ObservableString();
    public ObservableString observerFriTo = new ObservableString();
    public ObservableString observerFriFrom = new ObservableString();
    public ObservableString observerSatTo = new ObservableString();
    public ObservableString observerSatFrom = new ObservableString();
    public ObservableString observerSunTo = new ObservableString();
    public ObservableString observerSunFrom = new ObservableString();

    public ObservableBoolean isMonCloseVisible = new ObservableBoolean(false);
    public ObservableBoolean isTuesCloseVisible = new ObservableBoolean(false);
    public ObservableBoolean isWedCloseVisible = new ObservableBoolean(false);
    public ObservableBoolean isThursCloseVisible = new ObservableBoolean(false);
    public ObservableBoolean isFriCloseVisible = new ObservableBoolean(false);
    public ObservableBoolean isSatCloseVisible = new ObservableBoolean(false);
    public ObservableBoolean isSunCloseVisible = new ObservableBoolean(false);

    private JsonObject jsonArrayMain = new JsonObject();

    private SingleLiveEvent<String> singleLiveEventSave = new SingleLiveEvent<>();
    private SingleLiveEvent<String> singleLiveEventTime = new SingleLiveEvent<>();
    private SingleLiveEvent<String> singleLiveEventClose = new SingleLiveEvent<>();

    private List<Integer> listSelected = new ArrayList<>();

    public SingleLiveEvent<String> getSingleLiveEventSave(){return  singleLiveEventSave;}
    public SingleLiveEvent<String> getSingleLiveEventTime(){return  singleLiveEventTime;}
    public SingleLiveEvent<String> getSingleLiveEventClose(){return  singleLiveEventClose;}

    public ViewModelSetHours(MyApplication application) {
        super(application, false,true);
    }

    public void onSaveClick()
    {
        try {
            if (!TextUtils.isEmpty(observerMonTo.getTrimmed()) && !TextUtils.isEmpty(observerMonFrom.getTrimmed())) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("to", UtilsDate.get24hourTimeFormat(observerMonTo.getTrimmed()));
                jsonObject.addProperty("from", UtilsDate.get24hourTimeFormat(observerMonFrom.getTrimmed()));
                jsonArrayMain.add("M", jsonObject);
            }
            if (!TextUtils.isEmpty(observerTuesTo.getTrimmed()) && !TextUtils.isEmpty(observerTuesFrom.getTrimmed())) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("to", UtilsDate.get24hourTimeFormat(observerTuesTo.getTrimmed()));
                jsonObject.addProperty("from", UtilsDate.get24hourTimeFormat(observerTuesFrom.getTrimmed()));
                jsonArrayMain.add("T", jsonObject);
            }
            if (!TextUtils.isEmpty(observerWedTo.getTrimmed()) && !TextUtils.isEmpty(observerWedFrom.getTrimmed())) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("to", UtilsDate.get24hourTimeFormat(observerWedTo.getTrimmed()));
                jsonObject.addProperty("from", UtilsDate.get24hourTimeFormat(observerWedFrom.getTrimmed()));
                jsonArrayMain.add("W", jsonObject);
            }
            if (!TextUtils.isEmpty(observerThursTo.getTrimmed()) && !TextUtils.isEmpty(observerThursFrom.getTrimmed())) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("to", UtilsDate.get24hourTimeFormat(observerThursTo.getTrimmed()));
                jsonObject.addProperty("from", UtilsDate.get24hourTimeFormat(observerThursFrom.getTrimmed()));
                jsonArrayMain.add("Th", jsonObject);
            }
            if (!TextUtils.isEmpty(observerFriTo.getTrimmed()) && !TextUtils.isEmpty(observerFriFrom.getTrimmed())) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("to", UtilsDate.get24hourTimeFormat(observerFriTo.getTrimmed()));
                jsonObject.addProperty("from", UtilsDate.get24hourTimeFormat(observerFriFrom.getTrimmed()));
                jsonArrayMain.add("F", jsonObject);
            }
            if (!TextUtils.isEmpty(observerSatTo.getTrimmed()) && !TextUtils.isEmpty(observerSatFrom.getTrimmed())) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("to", UtilsDate.get24hourTimeFormat(observerSatTo.getTrimmed()));
                jsonObject.addProperty("from", UtilsDate.get24hourTimeFormat(observerSatFrom.getTrimmed()));
                jsonArrayMain.add("Sa", jsonObject);
            }
            if (!TextUtils.isEmpty(observerSunTo.getTrimmed()) && !TextUtils.isEmpty(observerSunFrom.getTrimmed())) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("to", UtilsDate.get24hourTimeFormat(observerSunTo.getTrimmed()));
                jsonObject.addProperty("from", UtilsDate.get24hourTimeFormat(observerSunFrom.getTrimmed()));
                jsonArrayMain.add("Su", jsonObject);
            }
            networkCallData();
        }
        catch (Exception e)
        {

        }
    }

    public void onToFromClick(String tag)
    {
        singleLiveEventTime.setValue(tag);
    }

    public void onCloseClick(String tag)
    {
        if(TextUtils.equals(tag, mApplication.getString(R.string.tagMondayClose)))
        {
            observerMonTo.set("");
            observerMonFrom.set("");
            isMonCloseVisible.set(false);
        }else if(TextUtils.equals(tag, mApplication.getString(R.string.tagTuesdayClose)))
        {
            observerTuesFrom.set("");
            observerTuesTo.set("");
            isTuesCloseVisible.set(false);
        }else if(TextUtils.equals(tag, mApplication.getString(R.string.tagWednesdayClose)))
        {
            observerWedFrom.set("");
            observerWedTo.set("");
            isWedCloseVisible.set(false);
        }else if(TextUtils.equals(tag, mApplication.getString(R.string.tagThursdayClose)))
        {
            observerThursFrom.set("");
            observerThursTo.set("");
            isThursCloseVisible.set(false);
        }else if(TextUtils.equals(tag, mApplication.getString(R.string.tagFridayClose)))
        {
            observerFriFrom.set("");
            observerFriTo.set("");
            isFriCloseVisible.set(false);
        }else if(TextUtils.equals(tag, mApplication.getString(R.string.tagSaturdayClose)))
        {
            observerSatFrom.set("");
            observerSatTo.set("");
            isSatCloseVisible.set(false);
        }else if(TextUtils.equals(tag, mApplication.getString(R.string.tagSundayClose)))
        {
            observerSunFrom.set("");
            observerSatTo.set("");
            isSunCloseVisible.set(false);
        }
    }

    @Override
    public void networkCallData() {
        setProgressBar(true);
        mApplication.getRetroFitInterface().consultantSetHours(mPreferences.getString(R.string.prefAccessToken),
                jsonArrayMain).enqueue(mCallbackData);
    }

    @Override
    public void sendResponseBodyData(PojoSetHours pojoSetHours) {
        if (pojoSetHours.getSuccess() ){
            singleLiveEventSave.setValue(pojoSetHours.getMessage());
            if(pojoSetHours.getPojoAvailabilities() != null &&
                    pojoSetHours.getPojoAvailabilities().size() > 0)
            {
                mApplication.setConsultantAvailability(pojoSetHours.getPojoAvailabilities());
            }
        } else {
            observerSnackBarInt.set(R.string.message_something_wrong);
        }
    }
}