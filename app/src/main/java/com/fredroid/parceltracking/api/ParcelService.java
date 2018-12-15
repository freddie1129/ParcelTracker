package com.fredroid.parceltracking.api;

import com.fredroid.parceltracking.db.entity.ParcelStatus;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ParcelService {
    @GET("/quickstart/parcel/{parcel_id}/status")
    Call<List<ParcelStatus>> getStatus_Ewe(@Path("parcel_id") String parcelId);

    @GET("/quickstart/parcel/{parcel_id}/status")
    Call<ResponseBody> getStatusRes_Ewe(@Path("parcel_id") String parcelId);

    @GET("/oms/api/tracking/ewe/{parcel_id}")
    Call<ResponseBody> getStatusRes_Ewe_Original(@Path("parcel_id") String parcelId);


    @GET("/track/search.json")
    Call<ResponseBody> getStatusRes_transrush_Original(@Query("number") String parcelId);


    @POST("/TrackNum.aspx?action=getjson")
    Call<ResponseBody> getStatusRes_fastgo_Original(@Query("dh") String parcelId);
}
