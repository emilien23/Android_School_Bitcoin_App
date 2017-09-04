package com.androidschool_bitcoin_app.api.api.bitcoin.bitcoindetailed;

/**
 * Created by Эмиль on 23.08.2017.
 */

import android.support.annotation.NonNull;

import com.androidschool_bitcoin_app.BuildConfig;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class ApiFactory {

private static final String API_BASE_URL = "https://min-api.cryptocompare.com/data/";

private static Retrofit sRetrofit;

private static OkHttpClient sHttpClient;

private ApiFactory() {
        throw new IllegalStateException("Final class can not be instantiated");
        }

@NonNull
public static Retrofit getRetrofitInstance() {
        if (sRetrofit == null) {
        sRetrofit = new Retrofit.Builder()
        .baseUrl(API_BASE_URL)
        .client(sHttpClient == null ? sHttpClient = provideClient() : sHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
        }
        return sRetrofit;
        }

private static OkHttpClient provideClient() {
final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES);
                if (BuildConfig.DEBUG) {
                builder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
                builder.addNetworkInterceptor(new StethoInterceptor());
                }

        return builder.build();
        }
}
