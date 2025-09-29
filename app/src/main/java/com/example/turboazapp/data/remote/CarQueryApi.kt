package com.example.turboazapp.data.remote

import com.example.turboazapp.domain.model.MakesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CarQueryApi {
    @GET("api/0.3/")
    suspend fun getMakes(
        @Query("cmd") cmd: String = "getMakes"
        // callback parametrini tamamilə sil
    ): Response<MakesResponse>
}

