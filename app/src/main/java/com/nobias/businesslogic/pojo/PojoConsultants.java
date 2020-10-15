package com.nobias.businesslogic.pojo;

import com.google.gson.annotations.SerializedName;

public class PojoConsultants {
    @SerializedName("id")
    private Integer id;
    @SerializedName("name")
    private String name;
    @SerializedName("profile_thumb_path")
    private String profileThumbPath;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileThumbPath() {
        return profileThumbPath;
    }

    public void setProfileThumbPath(String profileThumbPath) {
        this.profileThumbPath = profileThumbPath;
    }
}
