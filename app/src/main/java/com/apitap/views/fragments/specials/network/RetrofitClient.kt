package com.apitap.views.fragments.specials.network

import android.util.Log
import com.apitap.views.fragments.specials.network.APIConstant.BASE_URL
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.TimeZone
import java.util.concurrent.TimeUnit

object RetrofitClient {

    // Function to get the token, this should be dynamically set, for example from SharedPreferences
    private var token: String? = null

    fun setToken(newToken: String) {
        token = newToken
    }

    // Add Logging Interceptor
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // You can change the level (BODY, BASIC, HEADERS)
    }

    // Add Authorization Interceptor to add Bearer token
    private val authInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val builder = originalRequest.newBuilder()

        // Add Bearer token to the request if available
        token?.let {
            builder.addHeader("Authorization", "Bearer $it")
        }
        builder.addHeader("TimeZone",TimeZone.getDefault().id)

        Log.d("TAG", "Authorization: $token")
        Log.d("TAG", "TimeZone:"+TimeZone.getDefault().id)
        val newRequest = builder.build()
        chain.proceed(newRequest)
    }

    // Add OkHttpClient with interceptors
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor) // Add logging interceptor
        .addInterceptor(authInterceptor) // Add the auth interceptor
        .connectTimeout(60, TimeUnit.SECONDS)   // Connection timeout
        .readTimeout(60, TimeUnit.SECONDS)      // Read timeout
        .writeTimeout(60, TimeUnit.SECONDS)     // Write timeout
        .build()

    // Create Retrofit instance
    val instance: ApiService by lazy {
        val gson = GsonBuilder()
            .serializeNulls() // This ensures that null values are sent
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // Set the client with logging and auth
            .addConverterFactory(GsonConverterFactory.create(gson)) // Use Gson for JSON conversion
            .build()

        retrofit.create(ApiService::class.java)
    }
}
