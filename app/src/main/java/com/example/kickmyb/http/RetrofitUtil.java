package com.example.kickmyb.http;

import android.content.Context;

import okhttp3.CookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitUtil {

    private static Service instance;

    public static Service get(Context mContext){
        if (instance == null) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client(mContext))
                .baseUrl("https://rhubarb-cobbler-43725.herokuapp.com/")
                //.baseUrl("http://10.0.2.2:8080/")
                .build();

            instance = retrofit.create(Service.class);
            return instance;
        } else {
            return instance;
        }
    }

    private static OkHttpClient client(Context mContext) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        CookieJar jar = new SessionCookieJar();
        OkHttpClient client = new OkHttpClient.Builder()
                //Test
                .addInterceptor(interceptor)
                .addInterceptor(new NetworkConnectionInterceptor(mContext))
                //
                .cookieJar(jar)
                .build();
        return client;

    }

}
