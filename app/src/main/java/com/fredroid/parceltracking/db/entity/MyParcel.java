package com.fredroid.parceltracking.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by jackttc on 23/12/17.
 */
@Entity(tableName = "ParcelTable", foreignKeys = @ForeignKey(entity = Customer.class,
        parentColumns = "nId",
        childColumns = "nCustomerId",
        onDelete = CASCADE))
public class MyParcel implements Parcelable {


    @PrimaryKey(autoGenerate = true)
    public int nId;
    public int nCustomerId;
    public String sParcel2DBar="";
    public Date time;
    public String sNote="";
    public int nCommpany;
    public String sPhotoPath="";


    public int getnId() {
        return nId;
    }

    public void setnId(int nId) {
        this.nId = nId;
    }

    public int getnCustomerId() {
        return nCustomerId;
    }

    public void setnCustomerId(int nCustomerId) {
        this.nCustomerId = nCustomerId;
    }

    public String getsParcel2DBar() {
        return sParcel2DBar;
    }

    public void setsParcel2DBar(String sParcel2DBar) {
        this.sParcel2DBar = sParcel2DBar;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getsNote() {
        return sNote;
    }

    public void setsNote(String sNote) {
        this.sNote = sNote;
    }

    public int getnCommpany() {
        return nCommpany;
    }

    public void setnCommpany(int nCommpany) {
        this.nCommpany = nCommpany;
    }

    public String getsPhotoPath() {
        return sPhotoPath;
    }

    public void setsPhotoPath(String sPhotoPath) {
        this.sPhotoPath = sPhotoPath;
    }

    public List<String> getPhotoPathList()
    {
        List<String> photoList = new ArrayList<String>();
        if (sPhotoPath.equals(""))
            return  photoList;
        String [] photos = sPhotoPath.split("&");
        for (String s: photos)
        {
            photoList.add(s);
        }
        return photoList;
    }

    public String getPhotoPathString(List<String> strings)
    {
        String sPhotoPath = "";
        for (String s:strings)
        {
            sPhotoPath += s + "&";
        }
        return  sPhotoPath;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.nId);
        dest.writeInt(this.nCustomerId);
        dest.writeString(this.sParcel2DBar);
        dest.writeLong(this.time != null ? this.time.getTime() : -1);
        dest.writeString(this.sNote);
        dest.writeInt(this.nCommpany);
        dest.writeString(this.sPhotoPath);
    }

    public MyParcel() {
    }

    protected MyParcel(Parcel in) {
        this.nId = in.readInt();
        this.nCustomerId = in.readInt();
        this.sParcel2DBar = in.readString();
        long tmpTime = in.readLong();
        this.time = tmpTime == -1 ? null : new Date(tmpTime);
        this.sNote = in.readString();
        this.nCommpany = in.readInt();
        this.sPhotoPath = in.readString();
    }

    public static final Creator<MyParcel> CREATOR = new Creator<MyParcel>() {
        @Override
        public MyParcel createFromParcel(Parcel source) {
            return new MyParcel(source);
        }

        @Override
        public MyParcel[] newArray(int size) {
            return new MyParcel[size];
        }
    };
}
