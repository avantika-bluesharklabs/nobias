package com.nobias.utils;

import android.util.Log;

import com.nobias.BuildConfig;

/**
 * Created by Avantika Gadhiya on Apr, 16 2020 5:30.
 */

public class Logger {
    private Logger() {
        /*default constructor*/
    }

    public static void e(String tag, Exception exception) {
        e(tag, exception.toString());
    }

    public static void e(String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, message);
        }
    }

    public static void log(String message) {
        e("log:", message);
    }

    public static void logInDetails(String message) {
        if (BuildConfig.DEBUG) {
            Throwable stack = new Throwable().fillInStackTrace();
            StackTraceElement[] trace = stack.getStackTrace();
            e(trace[1].getClassName() + "." + trace[1].getMethodName() + ":" +
                    trace[1].getLineNumber(), message);
        }
    }
}
