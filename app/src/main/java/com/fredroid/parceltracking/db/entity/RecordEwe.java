package com.fredroid.parceltracking.db.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jackttc on 25/12/17.
 */

public class RecordEwe implements Parcelable {

   public String sTime;
   public String sLocation;
   public String sStatus;

    public RecordEwe(String sTime, String sLocation, String sStatus) {
        this.sTime = sTime;
        this.sLocation = sLocation;
        this.sStatus = sStatus;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.sTime);
        dest.writeString(this.sLocation);
        dest.writeString(this.sStatus);
    }

    protected RecordEwe(Parcel in) {
        this.sTime = in.readString();
        this.sLocation = in.readString();
        this.sStatus = in.readString();
    }

    public static final Parcelable.Creator<RecordEwe> CREATOR = new Parcelable.Creator<RecordEwe>() {
        @Override
        public RecordEwe createFromParcel(Parcel source) {
            return new RecordEwe(source);
        }

        @Override
        public RecordEwe[] newArray(int size) {
            return new RecordEwe[size];
        }
    };
}
