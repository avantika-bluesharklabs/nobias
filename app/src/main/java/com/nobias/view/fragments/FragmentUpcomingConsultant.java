package com.nobias.view.fragments;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.nobias.BR;
import com.nobias.R;
import com.nobias.businesslogic.interfaces.OnClickUpcomingConsultant;
import com.nobias.businesslogic.pojo.PojoPastConsultants;
import com.nobias.businesslogic.viewmodel.fragments.ViewModelUpcomingConsultant;
import com.nobias.databinding.FragmentUpcomingConsultantBinding;
import com.nobias.utils.Utils;
import com.nobias.utils.UtilsDate;
import com.nobias.view.adapter.AdapterUpcomingConsultant;

import java.util.Timer;
import java.util.TimerTask;

import static android.Manifest.permission.WRITE_CALENDAR;
import static com.nobias.utils.constants.ConstantCodes.DATE_FORMAT_TIMESTAMP;
import static com.nobias.utils.constants.RetrofitParams.VALUE_ACCEPTED;
import static com.nobias.utils.constants.RetrofitParams.VALUE_DECLINED;

/**
 * Created by Smit Shah on Aug 22 2018, 11:38 AM
 * <p>
 * Fragment for attendance log
 */
public class FragmentUpcomingConsultant extends FragmentBase implements OnClickUpcomingConsultant {
    private ViewModelUpcomingConsultant mVMUpcomingConsultant;
    private FragmentUpcomingConsultantBinding binding;
    private AdapterUpcomingConsultant mAdapterUpcomingConsultant;
    private Timer mTimerTwilioAccessible;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout
                .fragment_upcoming_consultant, container, false);
        mVMUpcomingConsultant = new ViewModelUpcomingConsultant(mApplication);
        binding.setVmUpcomingConsultantList(mVMUpcomingConsultant);
        binding.setOnContentClickListener(this);

        mBroadcastManager.registerReceiver(mReceiverRefreshData, new IntentFilter
                (getResources().getString(R.string.broadcastNobiasRefreshList)));
        observerEvents();

        mTimerTwilioAccessible = new Timer();
        mTimerTwilioAccessible.schedule(mTimerTaskTwilioAccessible, 2*60*1000,720 );
        return binding.getRoot();
    }

    /**
     * Observes events provided by live data single events from login and forgot password view
     * models
     */
    private void observerEvents() {
        mVMUpcomingConsultant.getSingleLiveEventStatus().observe(this, aVoid -> {
            Utils.hideKeyboard(mMainActivity);
            if (mApplication.mIsConsultant) {
                if (TextUtils.equals(aVoid, VALUE_ACCEPTED)) {
                    mVMUpcomingConsultant.observerContent.get(mVMUpcomingConsultant.observablePosition.get()).setStatus(VALUE_ACCEPTED);
                    mVMUpcomingConsultant.observerContent.get(mVMUpcomingConsultant.observablePosition.get()).notifyPropertyChanged(BR.status);
                    showAcceptStatusSuccessDialog();
                } else
                    showSuccessDialog(aVoid, "setAppointment");
            } else {
                Fragment fragment = new FragmentStripe();
                Bundle bundle = new Bundle();
                bundle.putBoolean(getString(R.string.bundleIsCheckout), true);
                bundle.putString(getString(R.string.bundleAppointmentId), mVMUpcomingConsultant.appointmentID);
                bundle.putString(getString(R.string.bundleRoomName), mVMUpcomingConsultant.roomName);
                fragment.setArguments(bundle);
                mMainActivity.addFragment(fragment, R.string.tagFragmentConsultants, R.string.tagFragmentConsultants);
            }
        });

        mVMUpcomingConsultant.getSingleLiveEventReminder().observe(this, aVoid -> {
            Utils.hideKeyboard(mMainActivity);
            showSuccessDialog(aVoid, "setReminder");
        });
        mVMUpcomingConsultant.getSingleLiveEventRemoveReminder().observe(this, aVoid -> {
            Utils.hideKeyboard(mMainActivity);
            showSuccessDialog(aVoid, "removeReminder");
        });
        mVMUpcomingConsultant.getSingleLiveEventTwilio().observe(this, aVoid -> {
            Utils.hideKeyboard(mMainActivity);
            callTwilio();
        });
        mVMUpcomingConsultant.getSingleLiveEventMessage().observe(this, aVoid -> {
            Utils.hideKeyboard(mMainActivity);
            showMessageDialog(aVoid);
        });
        mVMUpcomingConsultant.getSingleLiveEventDelete().observe(this, aVoid -> {
            Utils.hideKeyboard(mMainActivity);
            mVMUpcomingConsultant.observerContent.remove(mVMUpcomingConsultant.observablePosition);
            mAdapterUpcomingConsultant = (AdapterUpcomingConsultant) binding.recyclerConsultantList.getAdapter();
            mAdapterUpcomingConsultant.removeItem(mVMUpcomingConsultant.observablePosition.get());
            mAdapterUpcomingConsultant.notifyDataSetChanged();
            showMessageDialog(aVoid);
        });
    }

    private void showMessageDialog(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton(getString(R.string.message_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setTitle("");
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void callTwilio() {
        Fragment fragment = new FragmentTwilio();
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.bundleTwilioRoomName), mVMUpcomingConsultant.roomName);
        bundle.putBoolean(getString(R.string.bundleTwilioFromStripe), false);
        fragment.setArguments(bundle);
        mMainActivity.addFragment(fragment, R.string.tagFragmentTwilio, R.string.tagFragmentTwilio);
    }

    private void showSuccessDialog(String message, String networkCallType) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton(getString(R.string.message_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                if (TextUtils.equals(networkCallType, "setReminder")) {
                    mVMUpcomingConsultant.observerContent.get(mVMUpcomingConsultant.observablePosition.get()).setReminder("true");
                    mVMUpcomingConsultant.observerContent.get(mVMUpcomingConsultant.observablePosition.get()).notifyPropertyChanged(BR.reminder);
                } else if (TextUtils.equals(networkCallType, "removeReminder")) {
                    mVMUpcomingConsultant.observerContent.get(mVMUpcomingConsultant.observablePosition.get()).setReminder("false");
                    mVMUpcomingConsultant.observerContent.get(mVMUpcomingConsultant.observablePosition.get()).notifyPropertyChanged(BR.reminder);
                } else if (TextUtils.equals(networkCallType, "setAppointment")) {
                    mVMUpcomingConsultant.observerContent.get(mVMUpcomingConsultant.observablePosition.get()).setStatus(VALUE_ACCEPTED);
                    mVMUpcomingConsultant.observerContent.get(mVMUpcomingConsultant.observablePosition.get()).notifyPropertyChanged(BR.status);
                }
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setTitle(getString(R.string.title_meeting_update));
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void showAcceptStatusSuccessDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
        alertDialogBuilder.setMessage(getString(R.string.message_add_calendar));
        alertDialogBuilder.setPositiveButton(getString(R.string.message_yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (ActivityCompat.checkSelfPermission(mActivity,
                        WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                    mActivity.requestCalendarPermissions();
                } else {
                    insertEventToCalendar();
                }
                dialog.cancel();
            }
        });
        alertDialogBuilder.setNegativeButton(getString(R.string.message_no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setTitle(getString(R.string.title_add_calendar));
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void insertEventToCalendar() {
        PojoPastConsultants pojo = mVMUpcomingConsultant.observerContent.get(mVMUpcomingConsultant.observablePosition.get());
        long startTime = UtilsDate.dateToTimestamp(pojo.getTime(), DATE_FORMAT_TIMESTAMP);
        String user = "";
        if (pojo.getUser() != null && !TextUtils.isEmpty(pojo.getUser().getName()))
            user = pojo.getUser().getName();
        else if (pojo.getConsultant() != null && !TextUtils.isEmpty(pojo.getConsultant().getName()))
            user = pojo.getConsultant().getName();
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, startTime + 60 * 60 * 1000)
                .putExtra(CalendarContract.Events.TITLE, "NoBias Meeting w/" + user)
                .putExtra(CalendarContract.Events.DESCRIPTION, "Summary:\n" + pojo.getSynopsis());
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        mBroadcastManager.unregisterReceiver(mReceiverRefreshData);
        super.onDestroyView();
    }

    @Override
    public void onClickConsultantStatus(View view, int layoutPosition, PojoPastConsultants data) {
        if (mApplication.mIsConsultant) {
            mVMUpcomingConsultant.observablePosition.set(layoutPosition);
            showStatusDialog(data);
        }
    }

    @Override
    public void onClickConsultantVideo(View view, int layoutPosition, PojoPastConsultants data) {
        if (mApplication.mIsConsultant) {
            mVMUpcomingConsultant.observablePosition.set(layoutPosition);
            mVMUpcomingConsultant.appointmentID = String.valueOf(data.getId());
            mVMUpcomingConsultant.roomName = data.getRoom_name();
            mVMUpcomingConsultant.networkCallType = "twilioToken";
            mVMUpcomingConsultant.networkCallList();
        } else {
            if (TextUtils.isEmpty(data.getPaid_at())) {
                mVMUpcomingConsultant.observablePosition.set(layoutPosition);
                mVMUpcomingConsultant.appointmentID = String.valueOf(data.getId());
                mVMUpcomingConsultant.roomName = data.getRoom_name();
                mVMUpcomingConsultant.networkCallType = "paymentIntent";
                mVMUpcomingConsultant.networkCallList();
            } else{
                mVMUpcomingConsultant.observablePosition.set(layoutPosition);
                mVMUpcomingConsultant.appointmentID = String.valueOf(data.getId());
                mVMUpcomingConsultant.roomName = data.getRoom_name();
                mVMUpcomingConsultant.networkCallType = "twilioToken";
                mVMUpcomingConsultant.networkCallList();
            }
        }
    }

    @Override
    public void onClickConsultantReminder(View view, int layoutPosition, PojoPastConsultants data) {
        if (TextUtils.equals(data.getReminder(), "true"))
            showReminderRemoveDialog(data);
        else
            showReminderDialog(data);
    }

    @Override
    public void onClickDeleteAppointment(View view, int layoutPosition, PojoPastConsultants data) {
        mVMUpcomingConsultant.observablePosition.set(layoutPosition);
        mVMUpcomingConsultant.appointmentID = String.valueOf(data.getId());
        mVMUpcomingConsultant.networkCallType = "deleteAppointment";
        mVMUpcomingConsultant.networkCallList();
    }

    @Override
    public void onClickReschedule(View view, int layoutPosition, PojoPastConsultants data) {
        Fragment fragment = new FragmentReschedule();
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.bundleRescheduleAppointmentID), data.getId().toString());
        fragment.setArguments(bundle);
        mMainActivity.addFragment(fragment, R.string.tagFragmentTwilio, R.string.tagFragmentTwilio);
    }

    private void showStatusDialog(PojoPastConsultants pojoPastConsultants) {
        final Dialog dialog = new Dialog(mMainActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_consultant_status);

        TextView txtHeader = (TextView) dialog.findViewById(R.id.dialog_title);
        txtHeader.setText(Html.fromHtml("Pending meeting request from <b>" + pojoPastConsultants.getUser().getName()
                + "</b> to discuss <b>" + pojoPastConsultants.getTopic() + " on " + pojoPastConsultants.getTimeReadable() + "</b>"));

        TextView txtAcceptMeeting = (TextView) dialog.findViewById(R.id.txtAcceptMeeting);
        txtAcceptMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVMUpcomingConsultant.networkCallType = "setAppointment";
                mVMUpcomingConsultant.meetingId = String.valueOf(pojoPastConsultants.getId());
                mVMUpcomingConsultant.meetingResponse = VALUE_ACCEPTED;
                mVMUpcomingConsultant.networkCallList();
                dialog.dismiss();
            }
        });

        TextView txtDeclineMeeting = (TextView) dialog.findViewById(R.id.txtDeclineMeeting);
        txtDeclineMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVMUpcomingConsultant.networkCallType = "setAppointment";
                mVMUpcomingConsultant.meetingId = String.valueOf(pojoPastConsultants.getId());
                mVMUpcomingConsultant.meetingResponse = VALUE_DECLINED;
                mVMUpcomingConsultant.networkCallList();
                dialog.dismiss();
            }
        });

        TextView txtCancel = (TextView) dialog.findViewById(R.id.txtCancel);
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showReminderDialog(PojoPastConsultants pojoPastConsultants) {
        final Dialog dialog = new Dialog(mMainActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_set_reminder);

        TextView txtHeader = (TextView) dialog.findViewById(R.id.dialog_title);
        txtHeader.setText(Html.fromHtml("<b>How far in advance </b>" + "would you like "
                + "<b>your reminder notification </b>" + "to be?"));

        TextView txt15Minutes = (TextView) dialog.findViewById(R.id.txt15Minutes);
        txt15Minutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVMUpcomingConsultant.duration = "15m";
                mVMUpcomingConsultant.meetingId = String.valueOf(pojoPastConsultants.getId());
                mVMUpcomingConsultant.networkCallType = "setReminder";
                mVMUpcomingConsultant.networkCallList();
                dialog.dismiss();
            }
        });

        TextView txt30Minutes = (TextView) dialog.findViewById(R.id.txt30Minutes);
        txt30Minutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVMUpcomingConsultant.duration = "30m";
                mVMUpcomingConsultant.meetingId = String.valueOf(pojoPastConsultants.getId());
                mVMUpcomingConsultant.networkCallType = "setReminder";
                mVMUpcomingConsultant.networkCallList();
                dialog.dismiss();
            }
        });

        TextView txt1Hour = (TextView) dialog.findViewById(R.id.txt1Hour);
        txt1Hour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVMUpcomingConsultant.duration = "1h";
                mVMUpcomingConsultant.meetingId = String.valueOf(pojoPastConsultants.getId());
                mVMUpcomingConsultant.networkCallType = "setReminder";
                mVMUpcomingConsultant.networkCallList();
                dialog.dismiss();
            }
        });

        TextView txt1Day = (TextView) dialog.findViewById(R.id.txt1Day);
        txt1Day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVMUpcomingConsultant.duration = "1d";
                mVMUpcomingConsultant.meetingId = String.valueOf(pojoPastConsultants.getId());
                mVMUpcomingConsultant.networkCallType = "setReminder";
                mVMUpcomingConsultant.networkCallList();
                dialog.dismiss();
            }
        });

        TextView txtCancel = (TextView) dialog.findViewById(R.id.txtCancel);
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showReminderRemoveDialog(PojoPastConsultants pojoPastConsultants) {
        final Dialog dialog = new Dialog(mMainActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_remove_reminder);

        TextView txtHeader = (TextView) dialog.findViewById(R.id.dialog_title);
        txtHeader.setText(Html.fromHtml("You are about to cancel your reminder notification.<b> Are you sure?</b>"));

        TextView txtRemoveReminder = (TextView) dialog.findViewById(R.id.txtRemoveReminder);
        txtRemoveReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVMUpcomingConsultant.duration = "15m";
                mVMUpcomingConsultant.meetingId = String.valueOf(pojoPastConsultants.getId());
                mVMUpcomingConsultant.networkCallType = "removeReminder";
                mVMUpcomingConsultant.networkCallList();
                dialog.dismiss();
            }
        });

        TextView txtCancel = (TextView) dialog.findViewById(R.id.txtCancel);
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    TimerTask mTimerTaskTwilioAccessible = new TimerTask() {
        public void run() {
            if (mVMUpcomingConsultant.observerContent.size() > 0) {
                for(int i=0; i< mVMUpcomingConsultant.observerContent.size(); i++)
                {
                    if(mVMUpcomingConsultant.observerContent.get(i).getAppointmentDateTimestamp() > 0)
                    {
                        if(UtilsDate.checkAppointmentTimeFiveMinutesAgo(mVMUpcomingConsultant.observerContent.get(i).getAppointmentDateTimestamp()))
                        {
                            mVMUpcomingConsultant.observerContent.get(i).setTwilioAvailable(true);
                            mVMUpcomingConsultant.observerContent.get(i).notifyPropertyChanged(BR.twilioAvailable);
                        }
                    }
                }
            }
        }
    };

    private final BroadcastReceiver mReceiverRefreshData = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            boolean isFromCalendarPermission = intent.getBooleanExtra(getString(R.string.bundleIsFromCalendarPermission), false);
            if (isFromCalendarPermission) {
                insertEventToCalendar();
            } else
                mVMUpcomingConsultant.networkCallList();
        }
    };
}
