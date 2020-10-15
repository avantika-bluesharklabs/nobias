package com.nobias.businesslogic.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.nobias.businesslogic.crypt.CryptLib;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Set;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.inject.Inject;

import static com.nobias.utils.Logger.e;
import static com.nobias.utils.constants.ConstantCodes.PREF_APP_NAME;

/**
 * Created by Avantika Gadhiya on Apr, 16 2020 5:30.
 */

public class AppSharedPreferences {
    private SharedPreferences mPreferences;
    private Context mContext;

    @Inject
    public AppSharedPreferences(Context context) {
        mContext = context;
        mPreferences = context.getSharedPreferences(PREF_APP_NAME, 0);
    }

    public boolean getBoolean(int prefName) {
        return mPreferences.getBoolean(mContext.getResources().getString(prefName), false);
    }

    public boolean getBoolean(int prefName, boolean defaultValue) {
        return mPreferences.getBoolean(mContext.getResources().getString(prefName), defaultValue);
    }

    public void setBoolean(int prefName, boolean value) {
        mPreferences.edit().putBoolean(mContext.getResources().getString(prefName), value).apply();
    }

    public String getString(int prefName) {
        return mPreferences.getString(mContext.getResources().getString(prefName), "");
    }

    public String getString(String prefName) {
        return mPreferences.getString(prefName, "");
    }

    public String getString(int prefName, String defaultValue) {
        return mPreferences.getString(mContext.getResources().getString(prefName), defaultValue);
    }

    public String getString(String prefName, String defaultValue) {
        return mPreferences.getString(prefName, defaultValue);
    }

    public void setString(int prefName, String value) {
        mPreferences.edit().putString(mContext.getResources().getString(prefName), value).apply();
    }

    public void setString(String prefName, String value) {
        mPreferences.edit().putString(prefName, value).apply();
    }

    public int getInteger(int prefName) {
        return mPreferences.getInt(mContext.getResources().getString(prefName), 0);
    }

    public int getInteger(int prefName, int defaultValue) {
        return mPreferences.getInt(mContext.getResources().getString(prefName), defaultValue);
    }

    public void setInteger(int prefName, int value) {
        mPreferences.edit().putInt(mContext.getResources().getString(prefName), value).apply();
    }

    public long getLong(int prefName) {
        return mPreferences.getLong(mContext.getResources().getString(prefName), 0L);
    }

    public long getLong(int prefName, long defaultValue) {
        return mPreferences.getLong(mContext.getResources().getString(prefName), defaultValue);
    }

    public void setLong(int prefName, long value) {
        mPreferences.edit().putLong(mContext.getResources().getString(prefName), value).apply();
    }

    public void clearAllPreferences() {
        mPreferences.edit().clear().apply();
    }

    public Set<String> getStringSet(String key, Set<String> defValues) {
        return mPreferences.getStringSet(key, defValues);
    }

    public void setStringEncrypted(int prefName, int value) {
        setStringEncrypted(prefName, String.valueOf(value));
    }

    public void setStringEncrypted(int prefName, String value) {
        try {
            value = !TextUtils.isEmpty(value) ? new CryptLib().EncryptString(value) : "";
        } catch (InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException |
                BadPaddingException | IllegalBlockSizeException |
                InvalidAlgorithmParameterException | UnsupportedEncodingException e) {
            e(getClass().getSimpleName() + " setStringEncrypted", e);
        }
        mPreferences.edit().putString(mContext.getResources().getString(prefName), value).apply();
    }
}
