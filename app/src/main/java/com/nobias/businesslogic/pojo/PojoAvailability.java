package com.nobias.businesslogic.pojo;

import com.google.gson.annotations.SerializedName;

public class PojoAvailability {
    @SerializedName("day")
    private String day;
    @SerializedName("to")
    private String to;
    @SerializedName("from")
    private String from;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
