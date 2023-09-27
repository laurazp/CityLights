package com.luridevlabs.citylights.data.remote

import com.luridevlabs.citylights.model.Monument
import com.luridevlabs.citylights.model.MonumentsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface CityLightsService {
    @GET("monumento?rf=html&srsname=utm30n&start=0&rows=20&distance=500&locale=es")
    suspend fun getMonuments(): MonumentsResponse

    @GET("monumento/monumentId")
    suspend fun getMonument(@Path("monumentId") monumentId: Long): Monument
}