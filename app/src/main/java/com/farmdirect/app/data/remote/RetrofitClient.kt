package com.farmdirect.app.data.remote

import com.farmdirect.app.util.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(Constants.TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(Constants.TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(Constants.TIMEOUT, TimeUnit.SECONDS)
        .build()
    val instance: Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService: ApiService = instance.create(ApiService::class.java)
}
