package com.example.myclaudecodeapp.data.remote

import com.example.myclaudecodeapp.data.model.ChartResponse
import com.example.myclaudecodeapp.data.model.HelloResponse
import retrofit2.http.GET

/** Retrofit APIインターフェース */
interface ApiService {
    @GET("/v1/")
    suspend fun getHello(): HelloResponse

    @GET("/v1/chart")
    suspend fun getChart(): ChartResponse
}
