package com.fredroid.parceltracking;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by jackttc on 23/12/17.
 */

public class APIClient {


    private static long upLoadSize = 0;

    private static Retrofit retrofit = null;

    private static Retrofit retrofit1 = null;


    public static long getUpLoadSize() {
        return upLoadSize;
    }

    public static void setUpLoadSize(long upLoadSize) {
        APIClient.upLoadSize = upLoadSize;
    }

    /*static Retrofit getClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        //OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        // OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
        //         .readTimeout(60, TimeUnit.SECONDS)
        //        .connectTimeout(60, TimeUnit.SECONDS);


        // OkHttpClient client = httpClient.build(); //.addInterceptor(interceptor).build();




        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .writeTimeout(5, TimeUnit.MINUTES)
                .readTimeout(180, TimeUnit.SECONDS)
                .connectTimeout(180, TimeUnit.SECONDS)
                //   .addInterceptor(interceptor)
                .build();





        retrofit = new Retrofit.Builder()
                .baseUrl("http://captya.2mm.io")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();



        return retrofit;
    }*/


    static Retrofit getClient_aa() {

       // HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
      //  interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        //OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        // OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
        //         .readTimeout(60, TimeUnit.SECONDS)
        //        .connectTimeout(60, TimeUnit.SECONDS);


        // OkHttpClient client = httpClient.build(); //.addInterceptor(interceptor).build();




        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .writeTimeout(5,TimeUnit.MINUTES)
                .readTimeout(180, TimeUnit.SECONDS)
                .connectTimeout(180, TimeUnit.SECONDS)
                //   .addInterceptor(interceptor)
                .build();





        retrofit = new Retrofit.Builder()
                .baseUrl("https://www.ewe.com.au")
                .client(okHttpClient)
                .build();



        return retrofit;
    }












    public static <S> S createService(Class<S> serviceClass) {
        return getClient_aa().create(serviceClass);
    }



}
