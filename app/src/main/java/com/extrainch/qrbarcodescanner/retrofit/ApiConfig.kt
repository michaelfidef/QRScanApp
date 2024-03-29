package com.extrainch.qrbarcodescanner.retrofit

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    fun getApiConfig(): ApiService {
        val authInterceptor = Interceptor { chain ->
            val req = chain.request()
            val requestHeaders = req.newBuilder()
                .build()
            chain.proceed(requestHeaders)
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("http://170.64.133.47/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }
}