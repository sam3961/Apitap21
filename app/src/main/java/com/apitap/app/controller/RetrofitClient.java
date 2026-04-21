package com.apitap.app.controller;

import static com.apitap.app.model.Client.BASE_URL_ONLY;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.TimeZone;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RetrofitClient {

    private static Retrofit retrofit;
    private static String token;

    public static void setToken(String newToken) {
        token = newToken;
    }

    public static Retrofit getClient() {

        if (retrofit == null) {

            // Logging interceptor
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            // Header interceptor
            Interceptor headerInterceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {

                    Request original = chain.request();

                    Request.Builder builder = original.newBuilder()
                            .addHeader("TimeZone", TimeZone.getDefault().getID())
                            .addHeader("Content-Type", "application/json");

                    if (token != null) {
                        builder.addHeader("Authorization", "Bearer " + token);
                    }

                    Request request = builder.build();

                    return chain.proceed(request);
                }
            };

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(headerInterceptor)
                    .addInterceptor(logging)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL_ONLY)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}