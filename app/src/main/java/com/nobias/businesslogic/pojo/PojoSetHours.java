package com.nobias.businesslogic.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Brij Dholakia on Sep, 18 2018 11:39.
 * <p>
 * Getter setter for common response
 */
public class PojoSetHours {
    @SerializedName("success")
    private Boolean success;
    @SerializedName("message")
    private String message;
    @SerializedName("availability")
    private List<PojoAvailability> pojoAvailabilities;

    public List<PojoAvailability> getPojoAvailabilities() {
        return pojoAvailabilities;
    }

    public void setPojoAvailabilities(List<PojoAvailability> pojoAvailabilities) {
        this.pojoAvailabilities = pojoAvailabilities;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}