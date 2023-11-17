package com.luridevlabs.citylights.data.remote

import com.luridevlabs.citylights.data.monument.remote.model.ApiMonument
import com.luridevlabs.citylights.data.monument.remote.model.MonumentsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface CityLightsService {

    /** A efectos prácticos para el mapa, pido sólo 100 monumentos
     * para no llenar en exceso la pantalla del mapa con los marcadores.
     */
    @GET("monumento.json?rows=100&srsname=wgs84")
    suspend fun getMonuments(): MonumentsResponse

    @GET("monumento/{monumentId}.json?srsname=wgs84")
    suspend fun getMonument(@Path("monumentId") monumentId: String): ApiMonument
}