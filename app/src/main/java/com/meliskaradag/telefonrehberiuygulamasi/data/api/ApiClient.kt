package com.meliskaradag.telefonrehberiuygulamasi.data.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
//import com.meliskaradag.telefonrehberiuygulamasi.data.api.ApiService

object ApiClient {

    fun create(baseUrl: String, apiKey: String): ApiService {
        val headerInterceptor = Interceptor { chain ->
            val req = chain.request().newBuilder()
                .addHeader("accept", "application/json")
                .addHeader("ApiKey", apiKey)   // Zorunlu header
                .build()
            chain.proceed(req)
        }

        val client = OkHttpClient.Builder().addInterceptor(headerInterceptor).build()

        return Retrofit.Builder()
            .baseUrl(baseUrl) // http://146.59.52.68:11235/
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ApiService::class.java)
    }
}