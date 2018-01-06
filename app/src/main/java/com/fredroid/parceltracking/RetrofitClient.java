package com.fredroid.parceltracking;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by jackttc on 23/12/17.
 */

public class RetrofitClient {
    private static Retrofit retrofit = null;
    private static Retrofit retrofit1 = null;




    static Retrofit getClient_aa() {








        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .writeTimeout(5, TimeUnit.MINUTES)
                .readTimeout(180, TimeUnit.SECONDS)
                .connectTimeout(180, TimeUnit.SECONDS)

                .build();



        if (retrofit1 == null) {


            retrofit1 = new Retrofit.Builder()
                    .baseUrl("https://www.ewe.com.au")
                    .client(okHttpClient)
                    .build();

        }
        return  retrofit1;


    }
}
