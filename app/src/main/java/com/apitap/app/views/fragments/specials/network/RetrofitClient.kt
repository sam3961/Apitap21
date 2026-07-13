package com.apitap.app.views.fragments.specials.network

import android.util.Log
import com.apitap.app.views.fragments.specials.network.APIConstant.BASE_URL
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.TimeZone
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


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
        builder.addHeader("TimeZone", TimeZone.getDefault().id)

        Log.d("TAG", "Authorization: $token")
        Log.d("TAG", "TimeZone:" + TimeZone.getDefault().id)
        val newRequest = builder.build()
        chain.proceed(newRequest)
    }

    //bypass ssl by sumit
/*
    // Add OkHttpClient with interceptors
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor) // Add logging interceptor
        .addInterceptor(authInterceptor) // Add the auth interceptor
        .connectTimeout(60, TimeUnit.SECONDS)   // Connection timeout
        .readTimeout(60, TimeUnit.SECONDS)      // Read timeout
        .writeTimeout(60, TimeUnit.SECONDS)     // Write timeout
        .build()
*/

    private val okHttpClient = getUnsafeOkHttpClient()

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

    private fun getUnsafeOkHttpClient(): OkHttpClient {

        val trustAllCerts = arrayOf<TrustManager>(
            object : X509TrustManager {
                override fun checkClientTrusted(
                    chain: Array<out X509Certificate>?,
                    authType: String?
                ) {
                }

                override fun checkServerTrusted(
                    chain: Array<out X509Certificate>?,
                    authType: String?
                ) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            }
        )

        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, trustAllCerts, SecureRandom())

        val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory

        return OkHttpClient.Builder()
            .sslSocketFactory(
                sslSocketFactory,
                trustAllCerts[0] as X509TrustManager
            )
            .hostnameVerifier(HostnameVerifier { _, _ -> true })
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

}

