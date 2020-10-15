package com.nobias.view.fragments;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import com.nobias.R;
import com.nobias.businesslogic.pojo.PojoCommonResponse;
import com.nobias.businesslogic.viewmodel.fragments.ViewModelUpdateInfo;
import com.nobias.databinding.FragmentUpdateInfoBinding;
import com.nobias.utils.Utils;
import com.nobias.utils.UtilsDate;
import com.nobias.utils.UtilsImage;
import com.nobias.utils.constants.ConstantCodes;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class FragmentUpdateInfo extends FragmentBase{
    private ViewModelUpdateInfo mVMUpdateInfo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentUpdateInfoBinding binding = DataBindingUtil.inflate(inflater, R.layout
                .fragment_update_info, container, false);
        mVMUpdateInfo = new ViewModelUpdateInfo(mApplication);
        binding.setVmUpdateInfo(mVMUpdateInfo);
        observerEvents();
        mBroadcastManager.registerReceiver(mReceiverImageResult, new IntentFilter
                (getResources().getString(R.string.broadcastImageResult)));

        mVMUpdateInfo.observerFirstName.set(mApplication.getAppSharedPreferences().getString(R.string.prefName));
        mVMUpdateInfo.observerDOB.set(UtilsDate.changeDateformat(mApplication.getAppSharedPreferences().getString(R.string.prefDOB),
                ConstantCodes.DATE_FORMAT_API, ConstantCodes.DATE_FORMAT));
        mVMUpdateInfo.observerCompanyName.set(mApplication.getAppSharedPreferences().getString(R.string.prefCompany));
        mVMUpdateInfo.observerPhoneNumber.set(mApplication.getAppSharedPreferences().getString(R.string.prefPhone));
        mVMUpdateInfo.observerJobTitle.set(mApplication.getAppSharedPreferences().getString(R.string.prefTitle));
        mVMUpdateInfo.observerPic.set(mApplication.getAppSharedPreferences().getString(R.string.prefProfilePath));
        new AsyncBase64().execute();
        return binding.getRoot();
    }

    /**
     * Observes events provided by live data single events from login and forgot password view
     * models
     */
    private void observerEvents() {
        mVMUpdateInfo.getLiveEventProfilePic().observe(this, aVoid -> {
            Utils.hideKeyboard(mMainActivity);
            imageChooser();
        });

        mVMUpdateInfo.getLiveEventSubmit().observe(this, aVoid -> {
            Utils.hideKeyboard(mMainActivity);
            if(!TextUtils.isEmpty(mVMUpdateInfo.observerPicBase64.get()))
                mVMUpdateInfo.networkCallData();
            else
                Toast.makeText(mContext, "Please select profile image.",Toast.LENGTH_SHORT).show();
        });

        mVMUpdateInfo.getLiveEventDatePicker().observe(this, aVoid -> {
            Utils.hideKeyboard(mMainActivity);
            setDateDialog();
        });

        mVMUpdateInfo.getLiveEventRegisterSuccess().observe(
                this, this::saveUserData);
    }

    private class AsyncBase64 extends AsyncTask<Void, Void, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            try {
                URL url = new URL(mVMUpdateInfo.observerPic.get());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                return BitmapFactory.decodeStream(input);
            } catch (Exception exception) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            mVMUpdateInfo.observerPicBase64.set(UtilsImage.convertBitmapToBase64(bitmap));
        }
    }

    /**
     * Receiver for getting image result
     */
    private final BroadcastReceiver mReceiverImageResult = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            byte[] byteArray = intent.getByteArrayExtra(mContext.getResources().getString(R.string
                    .bundleImageResultStream));
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(byteArray.length);
            outputStream.write(byteArray, 0, byteArray.length);
            mVMUpdateInfo.observerStreamPic.set(outputStream);
            mVMUpdateInfo.observerPicBase64.set(UtilsImage.convertImageToBase64(byteArray));
            mVMUpdateInfo.observerPicUri.set(intent.getStringExtra
                    (mContext.getResources().getString(R.string.bundleImageResultUri)));
        }
    };

    private void saveUserData(PojoCommonResponse data) {
        mApplication.saveUserData(data,true);
        Intent intent = new Intent(mApplication.getResources().getString(R
                .string.broadcastUpdateUserInfo));
        mApplication.getAppComponent().getLocalBroadcastManager().sendBroadcast(intent);
        showSuccessDialog(data.getMessage());
    }

    private void showSuccessDialog(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mMainActivity);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton(getString(R.string.message_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                mMainActivity.onBackPressed();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setTitle(getString(R.string.title_user_update));
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void setDateDialog() {
        DatePickerDialog dialog;
        int year;
        int month;
        int day;
        if (mVMUpdateInfo.observerDOB.getTrimmedLength() > 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(UtilsDate.dateToTimestamp(mVMUpdateInfo.observerDOB.getTrimmed(),
                    ConstantCodes.DATE_FORMAT));
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        } else {
            Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }
        dialog = new DatePickerDialog(mMainActivity, (view, year1, monthOfYear, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year1);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            mVMUpdateInfo.observerDOB.set(new SimpleDateFormat(ConstantCodes.DATE_FORMAT, Locale
                    .getDefault()).format(calendar.getTime()));
        }, year, month, day);

        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DAY_OF_YEAR, -1);
        yesterday = UtilsDate.clearTimes(yesterday);
        dialog.getDatePicker().setMaxDate(yesterday.getTimeInMillis());
        dialog.show();
    }

    @Override
    public void onDestroyView() {
        mBroadcastManager.unregisterReceiver(mReceiverImageResult);
        super.onDestroyView();
    }
}
