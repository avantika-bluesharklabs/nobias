package com.nobias.utils;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.nobias.R;
import com.nobias.utils.constants.ConstantCodes;

import java.util.regex.Pattern;

/**
 * Created by Avantika Gadhiya on Apr, 16 2020 5:30.
 */

public class Utils {

    /**
     * Shows the snackbar
     *
     * @param toastMessage Message for the snackbar
     * @param viewLayout   View layout along which snackbar will be shown
     */
    public static void showSnackBar(View viewLayout, String toastMessage) {
        try {
            Snackbar.make(viewLayout, toastMessage, Snackbar.LENGTH_LONG).show();
        } catch (Exception exception) {
            Logger.log(exception.toString());
        }
    }

    /**
     * Closes the open keyboard if exist in focus
     *
     * @param activity Activity calling class
     */
    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (activity.getCurrentFocus() != null) {
                IBinder iBinder = activity.getCurrentFocus().getWindowToken();
                if (iBinder != null && inputMethodManager != null) {
                    inputMethodManager.hideSoftInputFromWindow(iBinder, 0);
                }
            }
        } catch (Exception exception) {
            Logger.log(exception.toString());
        }
    }

    /**
     * Provides error message depending on the message received from api being it non empty of
     * containing data
     *
     * @param context Context of calling class
     * @param message Message to be checked for
     * @return Error message if present else something went wong message
     */
    public static String getErrorMessage(Context context, String message) {
        if (!TextUtils.isEmpty(message)) {
            return message;
        } else {
            return context.getResources().getString(R.string.message_something_wrong);
        }
    }

    public static void setSpinnerTextPadding(boolean isDropDown, Context context, TextView
            textView) {
        if (isDropDown) {
            textView.setPadding(context.getResources().getDimensionPixelSize(R.dimen.margin_eight),
                    context.getResources().getDimensionPixelSize(R.dimen.margin_eight),
                    context.getResources().getDimensionPixelSize(R.dimen.margin_eight),
                    context.getResources().getDimensionPixelSize(R.dimen.margin_eight));
        } else {
            textView.setPadding(0, 0, 0,
                    0);
        }
    }

    public static boolean isValidPassword(String password)
    {
        Pattern pattern = Pattern.compile(ConstantCodes.PASSWORD_VALIDATION);
        return pattern.matcher(password).matches();
    }
}
