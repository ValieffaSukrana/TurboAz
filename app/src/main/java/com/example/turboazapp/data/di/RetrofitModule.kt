package com.example.turboazapp.data.di

import com.example.turboazapp.data.remote.CarQueryApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitModule {
    private const val BASE_URL = "https://www.carqueryapi.com/"

    val api: CarQueryApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CarQueryApi::class.java)
    }
}
