package com.fredroid.parceltracking;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by jackttc on 23/12/17.
 */

public interface APIInterface {


    //https://www.ewe.com.au/track?cno=b978379135b#track-results
    @GET("/track?#track-results")
    Call<ResponseBody> httpTrackInfo(@Query("cno") String strBarcode
                                          );
}
