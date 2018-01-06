package com.fredroid.parceltracking.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jackttc on 23/12/17.
 */
@Entity(tableName = "CustomerTable")
public class Customer implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    public int  nId;
    public String sName;
    public String sAddress;
    public String sPhone;
    public String sNote;

    public Customer(String sName, String sAddress, String sPhone, String sNote) {
        this.sName = sName;
        this.sAddress = sAddress;
        this.sPhone = sPhone;
        this.sNote = sNote;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.nId);
        dest.writeString(this.sName);
        dest.writeString(this.sAddress);
        dest.writeString(this.sPhone);
        dest.writeString(this.sNote);
    }

    protected Customer(Parcel in) {
        this.nId = in.readInt();
        this.sName = in.readString();
        this.sAddress = in.readString();
        this.sPhone = in.readString();
        this.sNote = in.readString();
    }

    public static final Parcelable.Creator<Customer> CREATOR = new Parcelable.Creator<Customer>() {
        @Override
        public Customer createFromParcel(Parcel source) {
            return new Customer(source);
        }

        @Override
        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };
}
