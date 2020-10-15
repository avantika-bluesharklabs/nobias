package com.nobias;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.nobias.businesslogic.di.AppComponent;
import com.nobias.businesslogic.di.AppModule;
import com.nobias.businesslogic.di.DaggerAppComponent;
import com.nobias.businesslogic.network.RetroFitCallFactory;
import com.nobias.businesslogic.network.RetroFitInterface;
import com.nobias.businesslogic.pojo.PojoAvailability;
import com.nobias.businesslogic.pojo.PojoCommonResponse;
import com.nobias.businesslogic.pojo.PojoEphemeralKey;
import com.nobias.businesslogic.pojo.PojoSpeciality;
import com.nobias.businesslogic.pojo.PojoUser;
import com.nobias.businesslogic.preferences.AppSharedPreferences;
import com.nobias.utils.UtilsImage;
import com.nobias.utils.constants.ConstantCodes;
import com.stripe.android.PaymentConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nobias.utils.Logger.e;
import static com.nobias.utils.UtilsImage.getMatrixedBitmapFromFile;

/**
 * Created by Avantika Gadhiya on Feb, 26 2020 11:20.
 */

public class MyApplication extends Application {
    private Context mContext;
    private AppComponent mAppComponent;
    private RetroFitInterface mRetrofitInterface;
    private AppSharedPreferences mPreferences;
    public boolean mIsConsultant;
    private List<PojoSpeciality> consultantSpecialities = new ArrayList<>();
    private List<PojoAvailability> consultantAvailability = new ArrayList<>();
    private PojoEphemeralKey mPojoEphemeralKey;
    private String mClientSecret="";
    private String mTwilioToken = "";

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        Firebase.setAndroidContext(this);
        mAppComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
        mAppComponent.inject(this);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        mPreferences = new AppSharedPreferences(this);
        mIsConsultant = mPreferences.getBoolean(R.string.prefIsConsultant);
        initializeRetrofit();
        generateFCMToken();
        initiateStripe();
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

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    /**
     * Checks Internet Connectivity.
     *
     * @return returns true if connected else false.
     */
    public boolean isInternetConnected() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        }
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    /**
     * Used to get retrofit interface for network reference
     *
     * @return Retrofit interface reference
     */
    public RetroFitInterface getRetroFitInterface() {
        return mRetrofitInterface;
    }

    public AppSharedPreferences getAppSharedPreferences(){return  mPreferences;}

    /**
     * Initializes retrofit instances
     */
    private void initializeRetrofit() {
        RetroFitCallFactory retrofit = new RetroFitCallFactory();
        mRetrofitInterface = retrofit.create();
    }

    public void saveUserData(PojoCommonResponse pojoCommonResponse, boolean isFromUpdateInfo) {
        if (pojoCommonResponse == null) {
            return;
        }
        if(!TextUtils.isEmpty(pojoCommonResponse.getAccessToken()))
            mPreferences.setString(R.string.prefAccessToken,getString(R.string.bearer) + " " +
                    pojoCommonResponse.getAccessToken());
        PojoUser pojoUser = null;
        if(isFromUpdateInfo) {
            if (pojoCommonResponse.getPojoConsultantInfo() != null)
                pojoUser = pojoCommonResponse.getPojoConsultantInfo();
            else if (pojoCommonResponse.getUser() != null)
                pojoUser = pojoCommonResponse.getUser();
        }
        else if(pojoCommonResponse.getUser() != null)
            pojoUser=  pojoCommonResponse.getUser();
        else
            return;

        if(pojoUser != null) {
            mPreferences.setString(getString(R.string.prefUserId), pojoUser.getId().toString());
            mPreferences.setBoolean(R.string.prefIsConsultant, pojoUser.getIsConsultant());
            mPreferences.setString(R.string.prefConsultantCode, pojoUser.getConsultantCode());
            mPreferences.setBoolean(R.string.prefAvailableNow, pojoUser.getAvailableNow());
            mPreferences.setString(R.string.prefTitle, pojoUser.getTitle());
            mPreferences.setString(R.string.prefName, pojoUser.getName());
            mPreferences.setString(R.string.prefUserEmail, pojoUser.getEmail());
            if (pojoUser.getCountryCode() != null)
                mPreferences.setInteger(R.string.prefCountryCode, pojoUser.getCountryCode());
            else
                mPreferences.setInteger(R.string.prefCountryCode, 0);
            mPreferences.setString(R.string.prefPhone, pojoUser.getPhone());
            mPreferences.setString(R.string.prefDOB, pojoUser.getDob());
            mPreferences.setString(R.string.prefProfilePath, pojoUser.getProfilePath());
            mPreferences.setString(R.string.prefProfileThumbPath, pojoUser.getProfileThumbPath());
            mPreferences.setString(R.string.prefCompany, pojoUser.getCompany());
            mPreferences.setString(R.string.prefExperience, pojoUser.getExperience());
            mPreferences.setString(R.string.prefStripId, pojoUser.getStripeId());
            mPreferences.setString(R.string.prefTermsAgreedAt, pojoUser.getTermsAgreedAt());
            mPreferences.setString(R.string.prefReceivesPushNotification, pojoUser.getReceivesPushNotifications());
            mPreferences.setBoolean(R.string.prefReceivesEmailNotification, pojoUser.getReceivesEmailNotifications());
            mPreferences.setBoolean(R.string.prefIsUserLoggedIn, true);
            mIsConsultant = mPreferences.getBoolean(R.string.prefIsConsultant);
        }
        initiateStripe();
    }

    public File getFileDocument(File file) {
        Bitmap bitmap = getMatrixedBitmapFromFile(file);
        try {
            return UtilsImage.savepic(bitmap, UtilsImage.createTempImageFile(mContext));
        } catch (IOException e) {
            e(getClass().getSimpleName() + " getFileDocument", e);
        }
        return null;
    }

    public RequestOptions glideCenterCircle() {
        return new RequestOptions()
                .dontAnimate()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .circleCrop();
    }

    public RequestOptions glideCenterCircle(int drawable) {
        return new RequestOptions()
                .dontAnimate()
                .centerCrop()
                .placeholder(drawable)
                .error(drawable)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .circleCrop();
    }

    public RequestOptions glideOptionCenterProfileBitmap() {
        return new RequestOptions()
                .dontAnimate()
                .centerCrop()
                .placeholder(R.drawable.placeholder_circle_small)
                .error(R.drawable.placeholder_circle_small)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .circleCrop();
    }

    public void initiateStripe()
    {
        PaymentConfiguration.init(
                getApplicationContext(),
                "pk_test_bqTMtr8h63C7XidLMVNIu5df"
        );

        if(!TextUtils.isEmpty(getAppSharedPreferences().getString(R.string.prefStripId))) {
            getRetroFitInterface().callEphemeralKey(mPreferences.getString(R.string.prefAccessToken),
                    mPreferences.getString(R.string.prefStripId), ConstantCodes.API_VERSION).enqueue(mCallEphemeralKey);
        }
    }

    /**
     * Callback for network call
     */
    protected Callback<PojoCommonResponse> mCallEphemeralKey = new Callback<PojoCommonResponse>() {
        @Override
        public void onResponse(Call<PojoCommonResponse> call, Response<PojoCommonResponse> response) {
            if (response != null && response.isSuccessful() && response.body() != null) {
                PojoCommonResponse body = response.body();
                mPojoEphemeralKey = body.getPojoEphemeralKey();
            }
        }

        @Override
        public void onFailure(Call<PojoCommonResponse> call, Throwable t) {

        }
    };

    public List<PojoSpeciality> getConsultantSpecialities() {
        return consultantSpecialities;
    }

    public void setConsultantSpecialities(List<PojoSpeciality> consultantSpecialities) {
        this.consultantSpecialities = consultantSpecialities;
    }

    public List<PojoAvailability> getConsultantAvailability() {
        return consultantAvailability;
    }

    public void setConsultantAvailability(List<PojoAvailability> consultantAvailability) {
        this.consultantAvailability = consultantAvailability;
    }

    public PojoEphemeralKey getmPojoEphemeralKey() {
        return mPojoEphemeralKey;
    }

    public void setmPojoEphemeralKey(PojoEphemeralKey mPojoEphemeralKey) {
        this.mPojoEphemeralKey = mPojoEphemeralKey;
    }

    public String getmClientSecret() {
        return mClientSecret;
    }

    public void setmClientSecret(String mClientSecret) {
        this.mClientSecret = mClientSecret;
    }

    public String getmTwilioToken() {
        return mTwilioToken;
    }

    public void setmTwilioToken(String mTwilioToken) {
        this.mTwilioToken = mTwilioToken;
    }
}
