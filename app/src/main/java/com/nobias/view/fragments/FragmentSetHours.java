package com.nobias.view.fragments;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import com.nobias.R;
import com.nobias.businesslogic.pojo.PojoAvailability;
import com.nobias.businesslogic.viewmodel.fragments.ViewModelSetHours;
import com.nobias.databinding.FragmentSetHoursBinding;
import com.nobias.utils.Utils;
import com.nobias.utils.UtilsDate;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Smit Shah on Aug 22 2018, 11:38 AM
 * <p>
 * Fragment for attendance log
 */
public class FragmentSetHours extends FragmentBase {
    private ViewModelSetHours mVMSetHours;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentSetHoursBinding binding = DataBindingUtil.inflate(inflater, R.layout
                .fragment_set_hours, container, false);
        mVMSetHours = new ViewModelSetHours(mApplication);
        binding.setVmSetHours(mVMSetHours);
        observerEvents();
        setUserTimeSlots();
        return binding.getRoot();
    }

    private void setUserTimeSlots() {
        List<PojoAvailability> pojoAvailabilities = mApplication.getConsultantAvailability();
        if(pojoAvailabilities.size() > 0)
        {
            for(int i=0;i < pojoAvailabilities.size(); i++)
            {
                if(TextUtils.equals(pojoAvailabilities.get(i).getDay(), "M"))
                {
                    if(!TextUtils.isEmpty(pojoAvailabilities.get(i).getFrom()))
                        mVMSetHours.observerMonFrom.set(UtilsDate.get12hourTimeFormat(pojoAvailabilities.get(i).getFrom()));
                    if(!TextUtils.isEmpty(pojoAvailabilities.get(i).getTo()))
                        mVMSetHours.observerMonTo.set(UtilsDate.get12hourTimeFormat(pojoAvailabilities.get(i).getTo()));
                    if(mVMSetHours.observerMonTo.getTrimmedLength() > 0 || mVMSetHours.observerMonFrom.getTrimmedLength() > 0)
                        mVMSetHours.isMonCloseVisible.set(true);
                }
                else if(TextUtils.equals(pojoAvailabilities.get(i).getDay(), "T"))
                {
                    if(!TextUtils.isEmpty(pojoAvailabilities.get(i).getFrom()))
                        mVMSetHours.observerTuesFrom.set(UtilsDate.get12hourTimeFormat(pojoAvailabilities.get(i).getFrom()));
                    if(!TextUtils.isEmpty(pojoAvailabilities.get(i).getTo()))
                        mVMSetHours.observerTuesTo.set(UtilsDate.get12hourTimeFormat(pojoAvailabilities.get(i).getTo()));
                    if(mVMSetHours.observerTuesTo.getTrimmedLength() > 0 || mVMSetHours.observerTuesFrom.getTrimmedLength() > 0)
                        mVMSetHours.isTuesCloseVisible.set(true);
                }
                else if(TextUtils.equals(pojoAvailabilities.get(i).getDay(), "W"))
                {
                    if(!TextUtils.isEmpty(pojoAvailabilities.get(i).getFrom()))
                        mVMSetHours.observerWedFrom.set(UtilsDate.get12hourTimeFormat(pojoAvailabilities.get(i).getFrom()));
                    if(!TextUtils.isEmpty(pojoAvailabilities.get(i).getTo()))
                        mVMSetHours.observerWedTo.set(UtilsDate.get12hourTimeFormat(pojoAvailabilities.get(i).getTo()));
                    if(mVMSetHours.observerWedTo.getTrimmedLength() > 0 || mVMSetHours.observerWedFrom.getTrimmedLength() > 0)
                        mVMSetHours.isWedCloseVisible.set(true);
                }
                else if(TextUtils.equals(pojoAvailabilities.get(i).getDay(), "Th"))
                {
                    if(!TextUtils.isEmpty(pojoAvailabilities.get(i).getFrom()))
                        mVMSetHours.observerThursFrom.set(UtilsDate.get12hourTimeFormat(pojoAvailabilities.get(i).getFrom()));
                    if(!TextUtils.isEmpty(pojoAvailabilities.get(i).getTo()))
                        mVMSetHours.observerThursTo.set(UtilsDate.get12hourTimeFormat(pojoAvailabilities.get(i).getTo()));
                    if(mVMSetHours.observerThursTo.getTrimmedLength() > 0 || mVMSetHours.observerThursFrom.getTrimmedLength() > 0)
                        mVMSetHours.isThursCloseVisible.set(true);
                }
                else if(TextUtils.equals(pojoAvailabilities.get(i).getDay(), "F"))
                {
                    if(!TextUtils.isEmpty(pojoAvailabilities.get(i).getFrom()))
                        mVMSetHours.observerFriFrom.set(UtilsDate.get12hourTimeFormat(pojoAvailabilities.get(i).getFrom()));
                    if(!TextUtils.isEmpty(pojoAvailabilities.get(i).getTo()))
                        mVMSetHours.observerFriTo.set(UtilsDate.get12hourTimeFormat(pojoAvailabilities.get(i).getTo()));
                    if(mVMSetHours.observerFriTo.getTrimmedLength() > 0 || mVMSetHours.observerFriFrom.getTrimmedLength() > 0)
                        mVMSetHours.isFriCloseVisible.set(true);
                }
                else if(TextUtils.equals(pojoAvailabilities.get(i).getDay(), "Sa"))
                {
                    if(!TextUtils.isEmpty(pojoAvailabilities.get(i).getFrom()))
                        mVMSetHours.observerSatFrom.set(UtilsDate.get12hourTimeFormat(pojoAvailabilities.get(i).getFrom()));
                    if(!TextUtils.isEmpty(pojoAvailabilities.get(i).getTo()))
                        mVMSetHours.observerSatTo.set(UtilsDate.get12hourTimeFormat(pojoAvailabilities.get(i).getTo()));
                    if(mVMSetHours.observerSatTo.getTrimmedLength() > 0 || mVMSetHours.observerSatFrom.getTrimmedLength() > 0)
                        mVMSetHours.isSatCloseVisible.set(true);
                }
                else if(TextUtils.equals(pojoAvailabilities.get(i).getDay(), "Su"))
                {
                    if(!TextUtils.isEmpty(pojoAvailabilities.get(i).getFrom()))
                        mVMSetHours.observerSunFrom.set(UtilsDate.get12hourTimeFormat(pojoAvailabilities.get(i).getFrom()));
                    if(!TextUtils.isEmpty(pojoAvailabilities.get(i).getTo()))
                        mVMSetHours.observerSunTo.set(UtilsDate.get12hourTimeFormat(pojoAvailabilities.get(i).getTo()));
                    if(mVMSetHours.observerSunTo.getTrimmedLength() > 0 || mVMSetHours.observerSunFrom.getTrimmedLength() > 0)
                        mVMSetHours.isSunCloseVisible.set(true);
                }
            }
        }
    }

    /**
     * Observes events provided by live data single events from login and forgot password view
     * models
     */
    /**
     * Observes events provided by live data single events from login and forgot password view
     * models
     */
    private void observerEvents() {
        mVMSetHours.getSingleLiveEventSave().observe(this, message -> {
            Utils.hideKeyboard(mMainActivity);
            showSavedDialog(message);
        });

        mVMSetHours.getSingleLiveEventTime().observe(this, tag -> {
            Utils.hideKeyboard(mMainActivity);
            setTimePicker(tag);
        });
    }

    private void setTimePicker(String tag) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(mMainActivity,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (TextUtils.equals(tag, getResources().getString(R.string.tagMondayStart))) {
                            mVMSetHours.observerMonTo.set(UtilsDate.get12hourTimeFormat(hourOfDay + ":" + minute));
                            mVMSetHours.isMonCloseVisible.set(true);
                        }
                        else if (TextUtils.equals(tag, getResources().getString(R.string.tagMondayEnd))) {
                            mVMSetHours.observerMonFrom.set(UtilsDate.get12hourTimeFormat(hourOfDay + ":" + minute));
                            mVMSetHours.isMonCloseVisible.set(true);
                        }
                        else if (TextUtils.equals(tag, getResources().getString(R.string.tagTuesdayStart))) {
                            mVMSetHours.observerTuesTo.set(UtilsDate.get12hourTimeFormat(hourOfDay + ":" + minute));
                            mVMSetHours.isTuesCloseVisible.set(true);
                        }
                        else if (TextUtils.equals(tag, getResources().getString(R.string.tagTuesdayEnd))) {
                            mVMSetHours.observerTuesFrom.set(UtilsDate.get12hourTimeFormat(hourOfDay + ":" + minute));
                            mVMSetHours.isTuesCloseVisible.set(true);
                        }
                        else if (TextUtils.equals(tag, getResources().getString(R.string.tagWednesdayStart))) {
                            mVMSetHours.observerWedTo.set(UtilsDate.get12hourTimeFormat(hourOfDay + ":" + minute));
                            mVMSetHours.isWedCloseVisible.set(true);
                        }
                        else if (TextUtils.equals(tag, getResources().getString(R.string.tagWednesdayEnd))) {
                            mVMSetHours.observerWedFrom.set(UtilsDate.get12hourTimeFormat(hourOfDay + ":" + minute));
                            mVMSetHours.isWedCloseVisible.set(true);
                        }
                        else if (TextUtils.equals(tag, getResources().getString(R.string.tagThursdayStart))) {
                            mVMSetHours.observerThursTo.set(UtilsDate.get12hourTimeFormat(hourOfDay + ":" + minute));
                            mVMSetHours.isThursCloseVisible.set(true);
                        }
                        else if (TextUtils.equals(tag, getResources().getString(R.string.tagThursdayEnd))) {
                            mVMSetHours.observerThursFrom.set(UtilsDate.get12hourTimeFormat(hourOfDay + ":" + minute));
                            mVMSetHours.isThursCloseVisible.set(true);
                        }
                        else if (TextUtils.equals(tag, getResources().getString(R.string.tagFridayStart))) {
                            mVMSetHours.observerFriTo.set(UtilsDate.get12hourTimeFormat(hourOfDay + ":" + minute));
                            mVMSetHours.isFriCloseVisible.set(true);
                        }
                        else if (TextUtils.equals(tag, getResources().getString(R.string.tagFridayEnd))) {
                            mVMSetHours.observerFriFrom.set(UtilsDate.get12hourTimeFormat(hourOfDay + ":" + minute));
                            mVMSetHours.isFriCloseVisible.set(true);
                        }
                        else if (TextUtils.equals(tag, getResources().getString(R.string.tagSaturdayStart))) {
                            mVMSetHours.observerSatTo.set(UtilsDate.get12hourTimeFormat(hourOfDay + ":" + minute));
                            mVMSetHours.isSatCloseVisible.set(true);
                        }
                        else if (TextUtils.equals(tag, getResources().getString(R.string.tagSaturdayEnd))) {
                            mVMSetHours.observerSatFrom.set(UtilsDate.get12hourTimeFormat(hourOfDay + ":" + minute));
                            mVMSetHours.isSatCloseVisible.set(true);
                        }
                        else if (TextUtils.equals(tag, getResources().getString(R.string.tagSundayStart))) {
                            mVMSetHours.observerSunTo.set(UtilsDate.get12hourTimeFormat(hourOfDay + ":" + minute));
                            mVMSetHours.isSunCloseVisible.set(true);
                        }
                        else if (TextUtils.equals(tag, getResources().getString(R.string.tagSundayEnd))) {
                            mVMSetHours.observerSunFrom.set(UtilsDate.get12hourTimeFormat(hourOfDay + ":" + minute));
                            mVMSetHours.isSunCloseVisible.set(true);
                        }
                    }
                }, hour, minute, false);
        timePickerDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void showSavedDialog(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton(getString(R.string.message_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                mMainActivity.onBackPressed();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setTitle(getString(R.string.title_hours_saved));
        alertDialog.setCancelable(false);
        alertDialog.show();
    }
}