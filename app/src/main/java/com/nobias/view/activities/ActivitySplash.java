package com.nobias.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.nobias.R;
import com.nobias.businesslogic.viewmodel.activities.ViewModelSplash;
import com.nobias.databinding.ActivitySplashBinding;
import com.nobias.utils.Logger;

/**
 * Created by Avantika Gadhiya on Apr, 16 2020 5:30.
 */

public class ActivitySplash extends ActivityBase {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySplashBinding mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        ViewModelSplash vmModel = new ViewModelSplash(mApplication);
        mBinding.setVmMain(vmModel);
        generateFCMToken();
        beginNavigationThread();
    }

    /**
     * Initiate splash navigation
     */
    private void beginNavigationThread() {
        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (Exception e) {
                Logger.log(e.toString());
            }
            navigateFromSplash();
        }).start();
    }

    private void generateFCMToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        String token = task.getResult().getToken();
                        mPreferences.setString(R.string.prefGcmToken, token.trim());
                        Log.d("Push notification token", token);
                    }
                });
    }

    /**
     * Navigated user to designated screen depending on logged in status of the user
     */
    private void navigateFromSplash() {
        if (mPreferences.getBoolean(R.string.prefIsUserLoggedIn, false)) {
            startActivity(new Intent(ActivitySplash.this, ActivityMain.class));
        } else {
            startActivity(new Intent(ActivitySplash.this, ActivityLogin.class));
        }
        overridePendingTransition(0, 0);
        finish();
    }
}
