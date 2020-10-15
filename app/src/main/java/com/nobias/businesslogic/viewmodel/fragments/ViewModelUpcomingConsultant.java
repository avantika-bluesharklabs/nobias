package com.nobias.businesslogic.viewmodel.fragments;

import android.text.TextUtils;

import androidx.databinding.ObservableInt;

import com.nobias.MyApplication;
import com.nobias.R;
import com.nobias.businesslogic.interactors.SingleLiveEvent;
import com.nobias.businesslogic.pojo.PojoCommonResponse;
import com.nobias.businesslogic.pojo.PojoPastConsultants;
import com.nobias.businesslogic.viewmodel.ViewModelRecyclerView;
import com.nobias.utils.UtilsDate;
import com.nobias.utils.constants.ConstantCodes;
import com.nobias.utils.constants.RetrofitConstants;

import static com.nobias.utils.constants.RetrofitConstants.METHOD_CONSULTANT_APPOINTMENT;
import static com.nobias.utils.constants.RetrofitConstants.METHOD_RESPONSE;
import static com.nobias.utils.constants.RetrofitConstants.METHOD_SET_REMINDER_POSTFIX;
import static com.nobias.utils.constants.RetrofitConstants.METHOD_SET_REMINDER_PREFIX;
import static com.nobias.utils.constants.RetrofitParams.VALUE_ACCEPTED;

/**
 * Created by Smit Shah on Aug 22 2018, 10:24 AM
 * <p>
 * View model for attendance log
 */
public class ViewModelUpcomingConsultant extends ViewModelRecyclerView<PojoCommonResponse, PojoPastConsultants>{
    private SingleLiveEvent<String> singleLiveEventStatus = new SingleLiveEvent<>();
    private SingleLiveEvent<String> singleLiveEventReminder = new SingleLiveEvent<>();
    private SingleLiveEvent<String> singleLiveEventRemoveReminder = new SingleLiveEvent<>();
    private SingleLiveEvent<String> liveEventTwilio = new SingleLiveEvent<>();
    private SingleLiveEvent<String> liveEventMessage = new SingleLiveEvent<>();
    private SingleLiveEvent<String> liveEventDelete = new SingleLiveEvent<>();

    public ViewModelUpcomingConsultant(MyApplication application) {

        super(application, false);
        networkCallList();
    }

    public ObservableInt observablePosition = new ObservableInt();
    public String meetingResponse;
    public String meetingId;
    public String duration;
    public String networkCallType = "";
    public String appointmentID = "";
    public String roomName = "";

    public SingleLiveEvent<String> getSingleLiveEventMessage() {
        return liveEventMessage;
    }
    public SingleLiveEvent<String> getSingleLiveEventDelete() {
        return liveEventDelete;
    }
    public SingleLiveEvent<String> getSingleLiveEventStatus() {
        return singleLiveEventStatus;
    }
    public SingleLiveEvent<String> getSingleLiveEventTwilio() {
        return liveEventTwilio;
    }
    public SingleLiveEvent<String> getSingleLiveEventReminder() {
        return singleLiveEventReminder;
    }
    public SingleLiveEvent<String> getSingleLiveEventRemoveReminder() {
        return singleLiveEventRemoveReminder;
    }

    @Override
    public void refreshListUpdate() {
        networkCallType = "";
        networkCallList();
    }

    @Override
    public void networkCallList() {
        setProgressBar(true);
        if (TextUtils.equals(networkCallType, "setReminder")) {
            String url = METHOD_SET_REMINDER_PREFIX + meetingId + METHOD_SET_REMINDER_POSTFIX;
            mApplication.getRetroFitInterface().consultantReminder(mPreferences.getString(R.string.prefAccessToken),
                    url, duration).enqueue(mCallbackList);
        } else if (TextUtils.equals(networkCallType, "removeReminder")) {
            String url = METHOD_SET_REMINDER_PREFIX + meetingId + METHOD_SET_REMINDER_POSTFIX;
            mApplication.getRetroFitInterface().consultantRemoveReminder(mPreferences.getString(R.string.prefAccessToken),
                    url).enqueue(mCallbackList);
        } else if (TextUtils.equals(networkCallType, "setAppointment")) {
            mApplication.getRetroFitInterface().consultantAppointment(mPreferences.getString(R.string.prefAccessToken),
                    METHOD_CONSULTANT_APPOINTMENT + meetingId + METHOD_RESPONSE, meetingResponse).enqueue(mCallbackList);
        }else if(TextUtils.equals(networkCallType, "twilioToken"))
        {
            String url = RetrofitConstants.METHOD_TWILIO_TOKEN_PREFIX + appointmentID + RetrofitConstants.METHOD_TWILIO_TOKEN_POSTFIX;
            mApplication.getRetroFitInterface().appointmentTwilioToken(mPreferences.getString(R.string.prefAccessToken),
                    url).enqueue(mCallbackList);
        }else if(TextUtils.equals(networkCallType, "paymentIntent")) {
            String url = RetrofitConstants.METHOD_PAYMENT_INTENT_PREFIX + appointmentID + RetrofitConstants.METHOD_PAYMENT_INTENT_POSTFIX;
            mApplication.getRetroFitInterface().appointmentPaymentIntent(mPreferences.getString(R.string.prefAccessToken),
                    url).enqueue(mCallbackList);
        }else if(TextUtils.equals(networkCallType, "deleteAppointment")){
            String url = RetrofitConstants.METHOD_APPOINTMENT_DELETE_PREFIX + appointmentID + RetrofitConstants.METHOD_APPOINTMENT_DELETE_POSTFIX;
            mApplication.getRetroFitInterface().appointmentDelete(mPreferences.getString(R.string.prefAccessToken),
                    url).enqueue(mCallbackList);
        }
        else {
            mApplication.getRetroFitInterface().getAppointment(mPreferences.getString(R.string.prefAccessToken)).enqueue(mCallbackList);
        }
    }

    @Override
    public void offlineDataList() {
    }

    @Override
    public void sendResponseBodyList(PojoCommonResponse pojoCommonResponse) {
        if (pojoCommonResponse.getSuccess()) {
            if (TextUtils.equals(networkCallType, "setReminder")) {
                networkCallType = "";
                if (!TextUtils.isEmpty(pojoCommonResponse.getMessage()))
                    singleLiveEventReminder.setValue(pojoCommonResponse.getMessage());
            } else if (TextUtils.equals(networkCallType, "removeReminder")) {
                networkCallType = "";
                if (!TextUtils.isEmpty(pojoCommonResponse.getMessage()))
                    singleLiveEventRemoveReminder.setValue(pojoCommonResponse.getMessage());
            } else if (TextUtils.equals(networkCallType, "setAppointment")) {
                networkCallType = "";
                if(TextUtils.equals(meetingResponse, VALUE_ACCEPTED))
                    singleLiveEventStatus.setValue(VALUE_ACCEPTED);
                else if (!TextUtils.isEmpty(pojoCommonResponse.getMessage()))
                    singleLiveEventStatus.setValue(pojoCommonResponse.getMessage());
            }else if(TextUtils.equals(networkCallType, "twilioToken"))
            {
                networkCallType = "";
                mApplication.setmTwilioToken(pojoCommonResponse.getTwilioToken());
                liveEventTwilio.setValue("");
            }else if(TextUtils.equals(networkCallType, "paymentIntent"))
            {
                networkCallType = "";
                mApplication.setmClientSecret(pojoCommonResponse.getClientSecret());
                singleLiveEventStatus.call();
            }else if(TextUtils.equals(networkCallType, "deleteAppointment"))
            {
                networkCallType = "";
                liveEventDelete.setValue(pojoCommonResponse.getMessage());
            }
            else
            {
                networkCallType = "";
                if (pojoCommonResponse.getAppointments().size() > 0) {
                    observerContent.clear();
                    //observerContent.addAll(pojoCommonResponse.getAppointments());
                    for(int i = 0; i< pojoCommonResponse.getAppointments().size(); i++)
                    {
                        observerContent.add(pojoCommonResponse.getAppointments().get(i));
                        if(!TextUtils.isEmpty(observerContent.get(i).getTime()))
                        {
                            long apptTimestamp = UtilsDate.dateToTimestampWithTimeZone(observerContent.get(i).getTime(),
                                    ConstantCodes.DATE_FORMAT_TIMESTAMP);
                            observerContent.get(i).setAppointmentDateTimestamp(apptTimestamp);
                            if(UtilsDate.checkAppointmentTimeFiveMinutesAgo(observerContent.get(i).getAppointmentDateTimestamp()))
                            {
                                observerContent.get(i).setTwilioAvailable(true);
                            }
                        }
                    }
                }
                if(pojoCommonResponse.getUser().getSpecialities() != null &&
                        pojoCommonResponse.getUser().getSpecialities().size() > 0)
                {
                    mApplication.setConsultantSpecialities(pojoCommonResponse.getUser().getSpecialities());
                }
                if(pojoCommonResponse.getUser().getAvailabilities() != null &&
                        pojoCommonResponse.getUser().getAvailabilities().size() > 0)
                {
                    mApplication.setConsultantAvailability(pojoCommonResponse.getUser().getAvailabilities());
                }
            }
        } else {
            if(TextUtils.equals(networkCallType, "twilioToken") && !TextUtils.isEmpty(pojoCommonResponse.getMessage())) {
                networkCallType = "";
                liveEventMessage.setValue(pojoCommonResponse.getMessage());
            }else if(TextUtils.equals(networkCallType, "paymentIntent") && !TextUtils.isEmpty(pojoCommonResponse.getMessage())) {
                networkCallType = "";
                liveEventMessage.setValue(pojoCommonResponse.getMessage());
            }
            else{
                networkCallType = "";
                observerSnackBarInt.set(R.string.message_something_wrong);
            }
        }
    }
}