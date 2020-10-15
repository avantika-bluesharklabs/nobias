package com.nobias.utils;

import android.text.TextUtils;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.nobias.utils.Logger.e;
import static com.nobias.utils.constants.ConstantCodes.DATE_FORMAT_TIMESTAMP;

/**
 * Created by Brij Dholakia on Jul, 10 2018 17:46.
 * <p>
 * Utility for date
 */
public class UtilsDate {

    /**
     * Converts the date into timestamp
     *
     * @param strDate Date to be converted
     * @param format  Format to be checked for
     * @return Timestamp corresponding to the date
     */
    public static Long dateToTimestamp(String strDate, String format) {
        Date date = new Date();
        SimpleDateFormat.getTimeInstance().format(Calendar.getInstance().getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        try {
            date = dateFormat.parse(strDate);
        } catch (java.text.ParseException exception) {
            e("UtilsDate dateToTimestamp", exception);
        }
        return date.getTime();
    }

    /**
     * Converts the date into timestamp
     *
     * @param strDate Date to be converted
     * @param format  Format to be checked for
     * @return Timestamp corresponding to the date
     */
    public static Long dateToTimestampWithTimeZone(String strDate, String format) {
        Date date = new Date();
        SimpleDateFormat.getTimeInstance().format(Calendar.getInstance().getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
       // dateFormat.setTimeZone(TimeZone.getTimeZone("EST"));
        try {
            date = dateFormat.parse(strDate);
        } catch (java.text.ParseException exception) {
            e("UtilsDate dateToTimestamp", exception);
        }
        return date.getTime();
    }

    /**
     * Converts timestamp into date and time
     *
     * @param timeStamp Timestamp to be converted
     * @return Required date time from timestamp
     */
    public static String getDateComplete(long timeStamp) {
        try {
            DateFormat sdf = new SimpleDateFormat("dd MMM, yyyy HH:mm", Locale.getDefault());
            Date netDate = (new Date(timeStamp * 1000));
            return sdf.format(netDate);
        } catch (Exception exception) {
            e("UtilsDate getDateComplete", exception);
            return String.valueOf(timeStamp);
        }
    }

    public static String getDate(long timeStamp, String format) {
        try {
            DateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
            Date netDate = (new Date(timeStamp * 1000));
            return sdf.format(netDate);
        } catch (Exception exception) {
            e("UtilsDate getDate", exception);
            return String.valueOf(timeStamp);
        }
    }

    /**
     * Converts timestamp into date
     *
     * @param timeStamp Timestamp to be converted
     * @return Required date from timestamp
     */
    public static String getDate(long timeStamp) {
        try {
            DateFormat sdf = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
            Date netDate = (new Date(timeStamp * 1000));
            return sdf.format(netDate);
        } catch (Exception exception) {
            e("UtilsDate getDate", exception);
            return String.valueOf(timeStamp);
        }
    }

    /**
     * Converts timestamp into time
     *
     * @param timeStamp Timestamp to be converted
     * @return Required time from timestamp
     */
    public static String getTime(long timeStamp) {
        try {
            DateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            Date netDate = (new Date(timeStamp * 1000));
            return sdf.format(netDate);
        } catch (Exception exception) {
            e("UtilsDate getTime", exception);
            return String.valueOf(timeStamp);
        }
    }

    /**
     * Converts the date into timestamp
     *
     * @param strDate Date to be converted
     * @return Timestamp corresponding to the date
     */
    public static Long dateToTimestampAttendance(String strDate) {
        Date date = new Date();
        SimpleDateFormat.getTimeInstance().format(Calendar.getInstance().getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy",
                Locale.getDefault());
        try {
            date = dateFormat.parse(strDate);
        } catch (java.text.ParseException exception) {
            e("UtilsDate dateToTimestampAttendance", exception);
        }
        return date.getTime();
    }

    public static Calendar clearTimes(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    public static String changeDateformat(String strDate, String dateFormatSet, String dateFormat) {
        DateFormat originalFormat = new SimpleDateFormat(dateFormatSet, Locale.getDefault());
        DateFormat targetFormat = new SimpleDateFormat(dateFormat, Locale.getDefault());
        //targetFormat.setTimeZone(TimeZone.getTimeZone("EST"));
        try {
            return targetFormat.format(originalFormat.parse(strDate));
        } catch (ParseException exception) {
            e("UtilsDate changeDateformat", exception);
        }
        return strDate;
    }

    public static String get12hourTimeFormat(String time24Hour)
    {
        String time12Hour = "";
        try {
            if(!TextUtils.isEmpty(time24Hour)) {
                final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                final Date dateObj = sdf.parse(time24Hour);
                time12Hour = new SimpleDateFormat("hh:mm aa").format(dateObj);
            }
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        return time12Hour;
    }

    public static String get24hourTimeFormat(String time24Hour)
    {
        String time12Hour = "";
        try {
            if(!TextUtils.isEmpty(time24Hour)) {
                final SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                final Date dateObj = sdf.parse(time24Hour);
                time12Hour = new SimpleDateFormat("HH:mm").format(dateObj);
            }
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        return time12Hour;
    }

    public static boolean checkAppointmentTimeFiveMinutesAgo(long appointmentTime)
    {
        int FIVE_MINUTES = 5 * 60 * 1000;
        Date now = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_TIMESTAMP);
        //simpleDateFormat.setTimeZone(TimeZone.getTimeZone("EST"));
        String date = simpleDateFormat.format(now);
        long currentTimestamp  = UtilsDate.dateToTimestamp(date, DATE_FORMAT_TIMESTAMP);
        long difference = Math.abs(appointmentTime - currentTimestamp);
        Log.d("difference" , String.valueOf(difference));

        if (difference <= FIVE_MINUTES) {
            return true;
        }
        return false;
    }
}
