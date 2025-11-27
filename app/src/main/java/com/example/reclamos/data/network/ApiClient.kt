package com.example.reclamos.data.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BASE_URL = "http://54.172.226.158:3000/"

    private val client = OkHttpClient.Builder().build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: ApiService = retrofit.create(ApiService::class.java)
}
