package com.nobias.utils.constants;

public class RetrofitConstants {
    public static final String METHOD_LOGIN = "api/login";
    public static final String METHOD_REGISTER_USER = "api/user";
    public static final String METHOD_REGISTER_CONSULTANT ="api/consultant";
    public static final String METHOD_GET_APPOINTMENT ="api/appointment";
    public static final String METHOD_RATE = "/rate";
    public static final String METHOD_GET_SPECIALITY = "api/specialty";
    public static final String METHOD_GET_CONSULTANTS = "api/user/consultant";
    public static final String METHOD_RESET_PASSWORD = "api/password/email";
    public static final String METHOD_USER_APPOINTMENT = "api/user/appointment";
    public static final String METHOD_CONSULTANT_APPOINTMENT ="api/consultant/appointment/";
    public static final String METHOD_RESPONSE = "/response";
    public static final String METHOD_SET_SPECIALITY = "api/consultant/specialty";
    public static final String METHOD_SET_HOURS = "api/consultant/availability";
    public static final String METHODD_GET_TIME_SLOT_POSTFIX = "/availability?date=";
    public static final String METHOD_GET_TIME_SLOT_PREFIX = "api/user/consultant/";
    public static final String METHOD_GET_TIME_SLOT_CODE_PREFIX ="api/user/consultant-code/";
    public static final String METHOD_SET_REMINDER_PREFIX = "api/appointment/";
    public static final String METHOD_SET_REMINDER_POSTFIX = "/reminder";
    public static final String METHOD_EMAIL_NOTIFICATION_STATUS = "api/device/email-notification/status";
    public static final String METHOD_CONSULTANT_AVAILABLE_NOW = "api/consultant/availability/now";
    public static final String METHOD_PN_TOKEN = "api/device/token";
    public static final String METHOD_EPHEMERAL_KEY = "api/stripe/ephemeral-key";
    public static final String METHOD_PAYMENT_INTENT_PREFIX = "api/stripe/appointment/";
    public static final String METHOD_PAYMENT_INTENT_POSTFIX = "/payment-intent";
    public static final String METHOD_TWILIO_TOKEN_PREFIX = "api/appointment/";
    public static final String METHOD_TWILIO_TOKEN_POSTFIX = "/twilio-token";
    public static final String METHOD_APPOINTMENT_DELETE_PREFIX = "api/appointment/";
    public static final String METHOD_APPOINTMENT_DELETE_POSTFIX = "/cancel";
    public static final String METHOD_APPOINTMENT_RESCHEDULE_POSTFIX = "/reschedule";
    public static final String METHOD_APPOINTMENT_RESCHEDULE_PREFIX = "api/appointment/";
}
