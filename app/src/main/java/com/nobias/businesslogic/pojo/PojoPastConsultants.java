package com.nobias.businesslogic.pojo;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.gson.annotations.SerializedName;

public class PojoPastConsultants extends BaseObservable {
    @SerializedName("id")
    private Integer id;
    @SerializedName("time")
    private String time;
    @SerializedName("time_readable")
    private String timeReadable;
    @SerializedName("topic")
    private String topic;
    @SerializedName("synopsis")
    private String synopsis;
    @SerializedName("rating")
    private String rating = "0";
    @SerializedName("status")
    private String status;
    @SerializedName("reminder")
    private String reminder;
    @SerializedName("room_name")
    private String room_name;
    @SerializedName("paid_at")
    private String paid_at;
    @SerializedName("user")
    private PojoConsultants user;
    @SerializedName("consultant")
    private PojoConsultants consultant;
    private Boolean isTwilioAvailable;
    private long appointmentDateTimestamp;

    public long getAppointmentDateTimestamp() {
        return appointmentDateTimestamp;
    }

    public void setAppointmentDateTimestamp(long appointmentDateTimestamp) {
        this.appointmentDateTimestamp = appointmentDateTimestamp;
    }

    @Bindable
    public Boolean getTwilioAvailable() {
        return isTwilioAvailable;
    }

    public void setTwilioAvailable(Boolean twilioAvailable) {
        isTwilioAvailable = twilioAvailable;
    }

    public String getPaid_at() {
        return paid_at;
    }

    public void setPaid_at(String paid_at) {
        this.paid_at = paid_at;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTimeReadable() {
        return timeReadable;
    }

    public void setTimeReadable(String timeReadable) {
        this.timeReadable = timeReadable;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    @Bindable
    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public PojoConsultants getUser() {
        return user;
    }

    public void setUser(PojoConsultants user) {
        this.user = user;
    }

    @Bindable
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Bindable
    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public PojoConsultants getConsultant() {
        return consultant;
    }

    public void setConsultant(PojoConsultants consultant) {
        this.consultant = consultant;
    }
}
