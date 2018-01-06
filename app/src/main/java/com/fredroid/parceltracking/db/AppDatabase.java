package com.fredroid.parceltracking.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.fredroid.parceltracking.db.converter.DateConverter;
import com.fredroid.parceltracking.db.dao.CustomerDao;
import com.fredroid.parceltracking.db.dao.ParcelDao;
import com.fredroid.parceltracking.db.entity.Customer;
import com.fredroid.parceltracking.db.entity.MyParcel;

/**
 * Created by jackttc on 23/12/17.
 */

@Database(entities = {Customer.class, MyParcel.class}, version = 1)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    public static String DATABASE_NAME = "parceltracking-database";

    public abstract CustomerDao customerDao();
    public abstract ParcelDao parcelDao();


}