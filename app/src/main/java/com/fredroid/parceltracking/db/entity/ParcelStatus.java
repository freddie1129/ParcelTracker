package com.fredroid.parceltracking.db.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ParcelStatus {

    @SerializedName("location")
    @Expose
    private String location;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("status")
    @Expose
    private String status;

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }
}
