package com.luridevlabs.citylights.data.remote

import com.luridevlabs.citylights.data.monument.remote.model.ApiMonument
import com.luridevlabs.citylights.data.monument.remote.model.MonumentsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CityLightsService {

    /** A efectos prácticos para el mapa, pido sólo 100 monumentos
     * para no llenar en exceso la pantalla del mapa con los marcadores.
     */
    @GET("monumento.json")
    suspend fun getMonuments(
        @Query("rows") limit: Int = 100,
        @Query("srsname") srsname: String = "wgs84"
    ): MonumentsResponse

    @GET("monumento.json")
    suspend fun getMonumentsPaging(
        @Query("start") offset: Int,
        @Query("rows") limit: Int,
        @Query("srsname") srsname: String = "wgs84"
    ): MonumentsResponse

    @GET("monumento/{monumentId}.json?srsname=wgs84")
    suspend fun getMonument(
        @Path("monumentId") monumentId: String,
        @Query("srsname") srsname: String = "wgs84"
    ): ApiMonument
}