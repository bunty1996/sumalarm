package com.level_sense.app.RetroFit;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by prashantm on 10/11/2016.
 */
public class ApiClient {

   // 45.55.226.112
    //STAGING // http://45.55.226.112/auth/do_upload"
   //public static final String BASE_URL = "http://45.55.226.112/";
    //https://dash.level-sense.com/Level-Sense-API/web/api/v1/
    //
  // public static final String BASE_URL = "http://192.168.0.18/starleague/";
    private static Retrofit posting_APIClient = null;
   // http://192.168.5.147
    public static Retrofit getClient(String str) {
        if (posting_APIClient == null) {
            posting_APIClient = new Retrofit.Builder()
                    .baseUrl(Config.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getHttpClient())
                    .build();
        }
        return posting_APIClient;
    }

    public static OkHttpClient getHttpClient() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(45, TimeUnit.SECONDS)
                .connectTimeout(45, TimeUnit.SECONDS)

                .build();
        return okHttpClient;
    }

}
