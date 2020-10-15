package com.nobias.businesslogic.pojo;

import com.google.gson.annotations.SerializedName;

public class PojoLink
{
    @SerializedName("first")
    private String first;
    @SerializedName("last")
    private String last;
    @SerializedName("prev")
    private Object prev;
    @SerializedName("next")
    private String next;

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public Object getPrev() {
        return prev;
    }

    public void setPrev(Object prev) {
        this.prev = prev;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }
}

