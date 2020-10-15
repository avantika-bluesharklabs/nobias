package com.nobias.businesslogic.pojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Brij Dholakia on Aug, 02 2018 14:30.
 * <p>
 * Getter setter for login
 */
public class PojoUser extends PojoCommonResponse implements Serializable {
    @SerializedName("id")
    private Integer id;
    @SerializedName("is_consultant")
    private Boolean isConsultant;
    @SerializedName("consultant_code")
    private String consultantCode;
    @SerializedName("available_now")
    private Boolean availableNow;
    @SerializedName("title")
    private String title;
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("country_code")
    private Integer countryCode;
    @SerializedName("phone")
    private String phone;
    @SerializedName("dob")
    private String dob;
    @SerializedName("profile_path")
    private String profilePath;
    @SerializedName("profile_thumb_path")
    private String profileThumbPath;
    @SerializedName("company")
    private String company;
    @SerializedName("experience")
    private String experience;
    @SerializedName("terms_agreed_at")
    private String termsAgreedAt;
    @SerializedName("receives_push_notifications")
    private String receivesPushNotifications;
    @SerializedName("receives_email_notifications")
    private Boolean receivesEmailNotifications;
    @SerializedName("stripe_id")
    private String stripeId;
    @SerializedName("specialities")
    private List<PojoSpeciality> specialities = null;
    @SerializedName("availability")
    private List<PojoAvailability> availabilities;
    private String sessionDate;
    private String sessionTopicName;
    private String sessionTopicCategory;

    public Boolean getConsultant() {
        return isConsultant;
    }

    public void setConsultant(Boolean consultant) {
        isConsultant = consultant;
    }

    public String getStripeId() {
        return stripeId;
    }

    public void setStripeId(String stripeId) {
        this.stripeId = stripeId;
    }

    public String getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(String sessionDate) {
        this.sessionDate = sessionDate;
    }

    public String getSessionTopicName() {
        return sessionTopicName;
    }

    public void setSessionTopicName(String sessionTopicName) {
        this.sessionTopicName = sessionTopicName;
    }

    public String getSessionTopicCategory() {
        return sessionTopicCategory;
    }

    public void setSessionTopicCategory(String sessionTopicCategory) {
        this.sessionTopicCategory = sessionTopicCategory;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getIsConsultant() {
        return isConsultant;
    }

    public void setIsConsultant(Boolean isConsultant) {
        this.isConsultant = isConsultant;
    }

    public String getConsultantCode() {
        return consultantCode;
    }

    public void setConsultantCode(String consultantCode) {
        this.consultantCode = consultantCode;
    }

    public Boolean getAvailableNow() {
        return availableNow;
    }

    public void setAvailableNow(Boolean availableNow) {
        this.availableNow = availableNow;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(Integer countryCode) {
        this.countryCode = countryCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    public String getProfileThumbPath() {
        return profileThumbPath;
    }

    public void setProfileThumbPath(String profileThumbPath) {
        this.profileThumbPath = profileThumbPath;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getTermsAgreedAt() {
        return termsAgreedAt;
    }

    public void setTermsAgreedAt(String termsAgreedAt) {
        this.termsAgreedAt = termsAgreedAt;
    }

    public String getReceivesPushNotifications() {
        return receivesPushNotifications;
    }

    public void setReceivesPushNotifications(String receivesPushNotifications) {
        this.receivesPushNotifications = receivesPushNotifications;
    }

    public Boolean getReceivesEmailNotifications() {
        return receivesEmailNotifications;
    }

    public void setReceivesEmailNotifications(Boolean receivesEmailNotifications) {
        this.receivesEmailNotifications = receivesEmailNotifications;
    }

    public List<PojoSpeciality> getSpecialities() {
        return specialities;
    }

    public void setSpecialities(List<PojoSpeciality> specialities) {
        this.specialities = specialities;
    }

    public List<PojoAvailability> getAvailabilities() {
        return availabilities;
    }

    public void setAvailabilities(List<PojoAvailability> availabilities) {
        this.availabilities = availabilities;
    }
}