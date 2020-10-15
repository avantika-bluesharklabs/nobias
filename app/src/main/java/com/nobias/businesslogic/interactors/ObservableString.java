package com.nobias.businesslogic.interactors;

import android.text.TextUtils;

import androidx.databinding.ObservableField;

/**
 * Created by Avantika Gadhiya on Apr, 16 2020 5:30.
 */

public class ObservableString extends ObservableField<String> {
    public ObservableString(String s) {
        set(s);
    }

    public ObservableString() {
    }

    @Override
    public String get() {
        return (super.get() == null ? "" : super.get());
    }

    /**
     * Method for get trimmed data
     *
     * @return trimmed data
     */
    public String getTrimmed() {
        String stringData = get();
        return (!TextUtils.isEmpty(stringData) && !TextUtils.isEmpty(stringData.trim()) ? stringData.trim() : "");
    }

    /**
     * Get String length
     *
     * @return length
     */
    public int getTrimmedLength() {
        String trimmedData = getTrimmed();
        return (!TextUtils.isEmpty(trimmedData) ? trimmedData.length() : 0);
    }

    /**
     * Check is Empty String
     *
     * @return is Empty
     */
    public boolean isEmpty() {
        String stringData = get();
        return (TextUtils.isEmpty(stringData) || (TextUtils.isEmpty(stringData.trim())));
    }
}
