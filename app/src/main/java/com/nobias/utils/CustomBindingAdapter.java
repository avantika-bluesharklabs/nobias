package com.nobias.utils;

import android.text.TextUtils;
import android.view.View;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableInt;

import com.nobias.businesslogic.interactors.ObservableString;

/**
 * Created by Avantika Gadhiya on Apr, 16 2020 5:30.
 */

public class CustomBindingAdapter {

    /**
     * Shows snack bar
     *
     * @param viewLayout         Layout relative to which snack bar is to be shown
     * @param snackMessageInt    Message resource to be shown in snack bar
     * @param snackMessageString Message to be shown in snack bar
     */
    @BindingAdapter(value = {"showSnackBarInt", "showSnackBarString"}, requireAll = false)
    public static void showSnackBar(View viewLayout, ObservableInt snackMessageInt,
                                    ObservableString snackMessageString) {
        String message = "";
        if (snackMessageString != null && !TextUtils.isEmpty(snackMessageString.getTrimmed())) {
            message = snackMessageString.getTrimmed();
            snackMessageString.set("");
        } else if (snackMessageInt != null && snackMessageInt.get() != 0) {
            message = viewLayout.getResources().getString(snackMessageInt.get());
            snackMessageInt.set(0);
        }

        if (!TextUtils.isEmpty(message)) {
            Utils.showSnackBar(viewLayout, message);
        }
    }
}
