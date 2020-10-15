package com.nobias.businesslogic.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Brij Dholakia on Sep, 18 2018 11:39.
 * <p>
 * Getter setter for common response
 */
public class PojoCommonResponse {
    @SerializedName("success")
    private Boolean success;
    @SerializedName("message")
    private String message;
    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("user")
    private PojoUser user;
    @SerializedName("available_times")
    private List<String> availableTimes;
    @SerializedName("appointments")
    private List<PojoPastConsultants> appointments;
    @SerializedName("past_appointments")
    private List<PojoPastConsultants> pastAppointments;
    @SerializedName("specialties")
    private List<PojoSpeciality> specialties;
    /*@SerializedName("availability")
    private List<PojoAvailability> pojoAvailabilities;*/
    @SerializedName("data")
    private List<PojoUser> pojoConsultant;
    @SerializedName("links")
    private PojoLink links;
    @SerializedName("meta")
    private PojoMeta meta;
    @SerializedName("consultant")
    private PojoUser pojoConsultantInfo;
    @SerializedName("ephemeral_key")
    private PojoEphemeralKey pojoEphemeralKey;
    @SerializedName("client_secret")
    private String clientSecret;
    @SerializedName("token")
    private String twilioToken;

    public String getTwilioToken() {
        return twilioToken;
    }

    public void setTwilioToken(String twilioToken) {
        this.twilioToken = twilioToken;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public PojoEphemeralKey getPojoEphemeralKey() {
        return pojoEphemeralKey;
    }

    public void setPojoEphemeralKey(PojoEphemeralKey pojoEphemeralKey) {
        this.pojoEphemeralKey = pojoEphemeralKey;
    }

    public List<PojoSpeciality> getSpecialties() {
        return specialties;
    }

    public void setSpecialties(List<PojoSpeciality> specialties) {
        this.specialties = specialties;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public PojoUser getUser() {
        return user;
    }

    public void setUser(PojoUser user) {
        this.user = user;
    }

    public List<PojoPastConsultants> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<PojoPastConsultants> appointments) {
        this.appointments = appointments;
    }

    public List<PojoPastConsultants> getPastAppointments() {
        return pastAppointments;
    }

    public void setPastAppointments(List<PojoPastConsultants> pastAppointments) {
        this.pastAppointments = pastAppointments;
    }

    public List<PojoUser> getPojoConsultant() {
        return pojoConsultant;
    }

    public void setPojoConsultant(List<PojoUser> pojoConsultant) {
        this.pojoConsultant = pojoConsultant;
    }

    public PojoLink getLinks() {
        return links;
    }

    public void setLinks(PojoLink links) {
        this.links = links;
    }

    public PojoMeta getMeta() {
        return meta;
    }

    public void setMeta(PojoMeta meta) {
        this.meta = meta;
    }

    public PojoUser getPojoConsultantInfo() {
        return pojoConsultantInfo;
    }

    public void setPojoConsultantInfo(PojoUser pojoConsultantInfo) {
        this.pojoConsultantInfo = pojoConsultantInfo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getAvailableTimes() {
        return availableTimes;
    }

    public void setAvailableTimes(List<String> availableTimes) {
        this.availableTimes = availableTimes;
    }

}