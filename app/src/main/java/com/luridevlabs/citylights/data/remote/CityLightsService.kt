package com.luridevlabs.citylights.data.remote

import com.luridevlabs.citylights.model.Monument
import com.luridevlabs.citylights.model.MonumentsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface CityLightsService {

    @GET("monumento.json?rows=100")

    suspend fun getMonuments(): MonumentsResponse

    @GET("monumento/{monumentId}.json")
    suspend fun getMonument(@Path("monumentId") monumentId: Long): Monument
}