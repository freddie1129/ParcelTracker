package com.fredroid.parceltracking.api;


import com.fredroid.parceltracking.ParcelConstant;
import com.fredroid.parceltracking.db.entity.ParcelStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class ParcelApiClient {

//    private static String HOME_URL = "http://138.68.247.251:8001";
    private static String HOME_URL_EWE = "https://www.ewe.com.au";
    private static String HOME_URL_fastgo = "https://www.fastgoexpress.com";
    private static String HOME_URL_transrush = "http://api.transrush.com.au";






    private static Retrofit retrofit_ewe = null;
    private static Retrofit retrofit_transrush = null;
    private static Retrofit retrofit_fastgo = null;



    public static Retrofit getClient(int commpany) {
        if (commpany == ParcelConstant.COMMPANY_ewe) {
            if (retrofit_ewe == null) {
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                retrofit_ewe = new Retrofit.Builder()
                        .baseUrl(HOME_URL_EWE)
                        .client(new OkHttpClient.Builder()
                                .addInterceptor(interceptor)
                                .readTimeout(30, TimeUnit.SECONDS)
                                .connectTimeout(30, TimeUnit.SECONDS)
                                .build())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit_ewe;
        }
        else if (commpany == ParcelConstant.COMMPANY_fast_go)
        {
            if (retrofit_fastgo == null) {
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                retrofit_fastgo = new Retrofit.Builder()
                        .baseUrl(HOME_URL_fastgo)
                        .client(new OkHttpClient.Builder()
                                .addInterceptor(interceptor)
                                .readTimeout(30, TimeUnit.SECONDS)
                                .connectTimeout(30, TimeUnit.SECONDS)
                                .build())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit_fastgo;
        }
        else if (commpany == ParcelConstant.COMMPANY_transrush)
        {
            if (retrofit_transrush == null) {
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                retrofit_transrush = new Retrofit.Builder()
                        .baseUrl(HOME_URL_transrush)
                        .client(new OkHttpClient.Builder()
                                .addInterceptor(interceptor)
                                .readTimeout(30, TimeUnit.SECONDS)
                                .connectTimeout(30, TimeUnit.SECONDS)
                                .build())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit_transrush;
        }
        else
        {
            return null;
        }
    }

    public static void getParcelStatus(
                               final IRequestCallback<List<ParcelStatus>> callback,
                               final String parcel_id) {

        Call<List<ParcelStatus>> response = getClient(ParcelConstant.COMMPANY_ewe).create(ParcelService.class).getStatus_Ewe(parcel_id);

        response.enqueue(new RequestServiceCallback<>(callback));
    }

    public interface IRequestCallback<T> {
        void onResponse(T model);

        void onFailure(int responseCode, String msg, Throwable t);

    }

    public static class RequestServiceCallback<T> implements Callback {
        final private IRequestCallback<T> callback;

        RequestServiceCallback(IRequestCallback<T> callback) {
            this.callback = callback;
        }

        @SuppressWarnings("NullableProblems")
        @Override
        public void onResponse(Call call, Response response) {
            if (callback != null) {
                if (response.code() >= 200 && response.code() < 300) {
                    //noinspection unchecked
                    callback.onResponse((T) response.body());
                } else {

                    String msg = response.message();
                    try {
                        msg = response.errorBody().string();
                        try {
                            JSONObject jsonObject = new JSONObject(msg);
                            msg = jsonObject.getString("message");
                        } catch (JSONException e) {
                        }
                    } catch (IOException e) {
                    }
                    callback.onFailure(response.code(), msg, null);
                }
            }
        }

        @SuppressWarnings("NullableProblems")
        @Override
        public void onFailure(Call call, Throwable t) {
            if (callback != null) {
                callback.onFailure(-1, "Something is wrong", t);
            }
        }
    }
}
