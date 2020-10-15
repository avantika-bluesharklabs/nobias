package com.nobias.businesslogic.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PojoEphemeralKey {
    @SerializedName("id")
    private String id;
    @SerializedName("object")
    private String object;
    @SerializedName("associated_objects")
    private List<PojoAssociatedObject> associatedObjects;
    @SerializedName("created")
    private Integer created;
    @SerializedName("expires")
    private Integer expires;
    @SerializedName("livemode")
    private Boolean livemode;
    @SerializedName("secret")
    private String secret;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public List<PojoAssociatedObject> getAssociatedObjects() {
        return associatedObjects;
    }

    public void setAssociatedObjects(List<PojoAssociatedObject> associatedObjects) {
        this.associatedObjects = associatedObjects;
    }

    public Integer getCreated() {
        return created;
    }

    public void setCreated(Integer created) {
        this.created = created;
    }

    public Integer getExpires() {
        return expires;
    }

    public void setExpires(Integer expires) {
        this.expires = expires;
    }

    public Boolean getLivemode() {
        return livemode;
    }

    public void setLivemode(Boolean livemode) {
        this.livemode = livemode;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
