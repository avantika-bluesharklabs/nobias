package com.nobias.businesslogic.network;

import com.google.gson.JsonObject;
import com.nobias.businesslogic.pojo.PojoCommonResponse;
import com.nobias.businesslogic.pojo.PojoSetHours;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Url;

import static com.nobias.utils.constants.RetrofitConstants.METHOD_CONSULTANT_AVAILABLE_NOW;
import static com.nobias.utils.constants.RetrofitConstants.METHOD_EMAIL_NOTIFICATION_STATUS;
import static com.nobias.utils.constants.RetrofitConstants.METHOD_EPHEMERAL_KEY;
import static com.nobias.utils.constants.RetrofitConstants.METHOD_GET_APPOINTMENT;
import static com.nobias.utils.constants.RetrofitConstants.METHOD_GET_SPECIALITY;
import static com.nobias.utils.constants.RetrofitConstants.METHOD_LOGIN;
import static com.nobias.utils.constants.RetrofitConstants.METHOD_PN_TOKEN;
import static com.nobias.utils.constants.RetrofitConstants.METHOD_REGISTER_CONSULTANT;
import static com.nobias.utils.constants.RetrofitConstants.METHOD_REGISTER_USER;
import static com.nobias.utils.constants.RetrofitConstants.METHOD_RESET_PASSWORD;
import static com.nobias.utils.constants.RetrofitConstants.METHOD_SET_HOURS;
import static com.nobias.utils.constants.RetrofitConstants.METHOD_SET_SPECIALITY;
import static com.nobias.utils.constants.RetrofitConstants.METHOD_USER_APPOINTMENT;
import static com.nobias.utils.constants.RetrofitParams.PARAM_API_VERSION;
import static com.nobias.utils.constants.RetrofitParams.PARAM_AUTHORIZATION;
import static com.nobias.utils.constants.RetrofitParams.PARAM_AVAILABLE_NOW;
import static com.nobias.utils.constants.RetrofitParams.PARAM_COMPANY;
import static com.nobias.utils.constants.RetrofitParams.PARAM_CONSULTANT_ID;
import static com.nobias.utils.constants.RetrofitParams.PARAM_CUSTOMER_ID;
import static com.nobias.utils.constants.RetrofitParams.PARAM_DATE;
import static com.nobias.utils.constants.RetrofitParams.PARAM_DEVICE_NAME;
import static com.nobias.utils.constants.RetrofitParams.PARAM_DEVICE_TOKEN;
import static com.nobias.utils.constants.RetrofitParams.PARAM_DOB;
import static com.nobias.utils.constants.RetrofitParams.PARAM_EMAIL;
import static com.nobias.utils.constants.RetrofitParams.PARAM_NAME;
import static com.nobias.utils.constants.RetrofitParams.PARAM_PASSWORD;
import static com.nobias.utils.constants.RetrofitParams.PARAM_PHONE;
import static com.nobias.utils.constants.RetrofitParams.PARAM_PROFILE_PIC;
import static com.nobias.utils.constants.RetrofitParams.PARAM_RATING;
import static com.nobias.utils.constants.RetrofitParams.PARAM_RESPONSE;
import static com.nobias.utils.constants.RetrofitParams.PARAM_SPECIALITY;
import static com.nobias.utils.constants.RetrofitParams.PARAM_STATUS;
import static com.nobias.utils.constants.RetrofitParams.PARAM_SYNOPSIS;
import static com.nobias.utils.constants.RetrofitParams.PARAM_TIME;
import static com.nobias.utils.constants.RetrofitParams.PARAM_TITLE;
import static com.nobias.utils.constants.RetrofitParams.PARAM_TOKEN;
import static com.nobias.utils.constants.RetrofitParams.PARAM_TOPIC;
import static com.nobias.utils.constants.RetrofitParams.PARAM_TYPE;

/**
 * Created by Avantika Gadhiya on Apr, 16 2020 5:30.
 */

public interface RetroFitInterface {
    @FormUrlEncoded
    @POST(METHOD_LOGIN)
    Call<PojoCommonResponse> login(@Field(PARAM_TOKEN) String token,
                                   @Field(PARAM_EMAIL) String username,
                                   @Field(PARAM_PASSWORD) String password,
                                   @Field(PARAM_DEVICE_NAME) String deviceName);

    @FormUrlEncoded
    @POST(METHOD_PN_TOKEN)
    Call<PojoCommonResponse> pushNotificationToken(@Header(PARAM_AUTHORIZATION) String auth,
                                                   @Field(PARAM_DEVICE_TOKEN) String deviceName);

    @POST(METHOD_RESET_PASSWORD)
    @FormUrlEncoded
    Call<PojoCommonResponse> resetPassword(@Header(PARAM_AUTHORIZATION) String auth,
                                           @Field(PARAM_TOKEN) String token,
                                           @Field(PARAM_EMAIL) String username);

    @Multipart
    @POST(METHOD_REGISTER_USER)
    @Headers({
            "Accept:application/json",
    })
    Call<PojoCommonResponse> registerUser(@Header(PARAM_AUTHORIZATION) String auth,
                                          @Part(PARAM_TOKEN) RequestBody token,
                                          @Part(PARAM_NAME) RequestBody name,
                                          @Part(PARAM_EMAIL) RequestBody email,
                                          @Part(PARAM_PASSWORD) RequestBody password,
                                          @Part(PARAM_DOB) RequestBody dob,
                                          @Part(PARAM_PHONE) RequestBody phone,
                                          @Part(PARAM_COMPANY) RequestBody company,
                                          @Part(PARAM_TITLE) RequestBody title,
                                          @Part(PARAM_PROFILE_PIC) RequestBody pic);

    //@Multipart
    @PUT(METHOD_REGISTER_USER)
    @Headers({
            "Accept:application/json",
    })
    @FormUrlEncoded
    Call<PojoCommonResponse> updateUser(@Header(PARAM_AUTHORIZATION) String auth,
                                        @Field(PARAM_NAME) String name,
                                        @Field(PARAM_DOB) String dob,
                                        @Field(PARAM_COMPANY) String company,
                                        @Field(PARAM_PHONE) String phone,
                                        @Field(PARAM_TITLE) String title,
                                        @Field(PARAM_PROFILE_PIC) String pic);

    @PUT(METHOD_REGISTER_CONSULTANT)
    @Headers({
            "Accept:application/json",
    })
    @FormUrlEncoded
    Call<PojoCommonResponse> updateConsultant(@Header(PARAM_AUTHORIZATION) String auth,
                                              @Field(PARAM_NAME) String name,
                                              @Field(PARAM_DOB) String dob,
                                              @Field(PARAM_COMPANY) String company,
                                              @Field(PARAM_PHONE) String phone,
                                              @Field(PARAM_TITLE) String title,
                                              @Field(PARAM_PROFILE_PIC) String pic);

    @Multipart
    @Headers({
            "Accept:application/json",
    })
    @POST(METHOD_REGISTER_CONSULTANT)
    Call<PojoCommonResponse> registerConsultant(@Header(PARAM_AUTHORIZATION) String auth,
                                                @Part(PARAM_TOKEN) RequestBody token,
                                                @Part(PARAM_NAME) RequestBody name,
                                                @Part(PARAM_EMAIL) RequestBody email,
                                                @Part(PARAM_PASSWORD) RequestBody password,
                                                @Part(PARAM_DOB) RequestBody dob,
                                                @Part(PARAM_PHONE) RequestBody phone,
                                                @Part(PARAM_COMPANY) RequestBody company,
                                                @Part(PARAM_TITLE) RequestBody title,
                                                @Part(PARAM_PROFILE_PIC) RequestBody pic);

    @POST(METHOD_USER_APPOINTMENT)
    @Multipart
    @Headers({
            "Accept:application/json",
    })
    Call<PojoCommonResponse> userAppointment(@Header(PARAM_AUTHORIZATION) String auth,
                                             @Part(PARAM_CONSULTANT_ID) RequestBody consultantId,
                                             @Part(PARAM_DATE) RequestBody date,
                                             @Part(PARAM_TIME) RequestBody time,
                                             @Part(PARAM_TOPIC) RequestBody topic,
                                             @Part(PARAM_SYNOPSIS) RequestBody synopsis);

    @GET(METHOD_GET_APPOINTMENT)
    Call<PojoCommonResponse> getAppointment(@Header(PARAM_AUTHORIZATION) String auth);

    @GET(METHOD_GET_SPECIALITY)
    Call<PojoCommonResponse> getSpeciality(@Header(PARAM_AUTHORIZATION) String auth);

    @GET
    Call<PojoCommonResponse> getTimeSlot(@Header(PARAM_AUTHORIZATION) String auth,
                                         @Url String url);

    @FormUrlEncoded
    @POST
    Call<PojoCommonResponse> ratePostConsultant(@Header(PARAM_AUTHORIZATION) String auth,
                                                @Url String url,
                                                @Field(PARAM_RATING) String rating);

    @POST
    Call<PojoCommonResponse> appointmentPaymentIntent(@Header(PARAM_AUTHORIZATION) String auth,
                                                      @Url String url);

    @DELETE
    Call<PojoCommonResponse> appointmentDelete(@Header(PARAM_AUTHORIZATION) String auth,
                                               @Url String url);

    @FormUrlEncoded
    @PUT
    Call<PojoCommonResponse> appointmentReschedule(@Header(PARAM_AUTHORIZATION) String auth,
                                                   @Url String url,
                                                   @Field(PARAM_TIME) String time);

    @GET
    Call<PojoCommonResponse> appointmentTwilioToken(@Header(PARAM_AUTHORIZATION) String auth,
                                                    @Url String ur);

    @FormUrlEncoded
    @POST(METHOD_EMAIL_NOTIFICATION_STATUS)
    Call<PojoCommonResponse> emailNotificationStatus(@Header(PARAM_AUTHORIZATION) String auth,
                                                     @Field(PARAM_STATUS) String status);

    @FormUrlEncoded
    @PUT(METHOD_CONSULTANT_AVAILABLE_NOW)
    Call<PojoCommonResponse> consultantAvailableNow(@Header(PARAM_AUTHORIZATION) String auth,
                                                    @Field(PARAM_AVAILABLE_NOW) String status);

    @FormUrlEncoded
    @POST
    Call<PojoCommonResponse> consultantAppointment(@Header(PARAM_AUTHORIZATION) String auth,
                                                   @Url String url,
                                                   @Field(PARAM_RESPONSE) String response);

    @FormUrlEncoded
    @POST(METHOD_EPHEMERAL_KEY)
    Call<PojoCommonResponse> callEphemeralKey(@Header(PARAM_AUTHORIZATION) String auth,
                                              @Field(PARAM_CUSTOMER_ID) String customerId,
                                              @Field(PARAM_API_VERSION) String apiVersion);

    @FormUrlEncoded
    @POST
    Call<PojoCommonResponse> consultantReminder(@Header(PARAM_AUTHORIZATION) String auth,
                                                @Url String url,
                                                @Field(PARAM_TYPE) String response);

    @DELETE
    Call<PojoCommonResponse> consultantRemoveReminder(@Header(PARAM_AUTHORIZATION) String auth,
                                                      @Url String url);

    @GET
    Call<PojoCommonResponse> getConsultants(@Header(PARAM_AUTHORIZATION) String auth,
                                            @Url String url);

    @FormUrlEncoded
    @PUT(METHOD_SET_SPECIALITY)
    Call<PojoCommonResponse> consultantSetSpeciality(@Header(PARAM_AUTHORIZATION) String auth,
                                                     @Field(PARAM_SPECIALITY) List<Integer> items);

    @PUT(METHOD_SET_HOURS)
    Call<PojoSetHours> consultantSetHours(@Header(PARAM_AUTHORIZATION) String auth,
                                          @Body JsonObject jsonObject);
}
