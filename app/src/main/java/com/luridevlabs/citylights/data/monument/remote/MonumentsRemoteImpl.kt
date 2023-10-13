package com.luridevlabs.citylights.data.monument.remote

import com.luridevlabs.citylights.data.monument.remote.mapper.MonumentResponseToMonumentMapper
import com.luridevlabs.citylights.data.monument.remote.model.ApiMonument
import com.luridevlabs.citylights.data.remote.CityLightsService
import com.luridevlabs.citylights.model.Monument

class MonumentsRemoteImpl (
    private val cityLightsService: CityLightsService,
    private val apiMonumentToMonumentMapper: MonumentResponseToMonumentMapper
) {

    suspend fun getMonuments(): List<Monument> {
        val apiMonuments = cityLightsService.getMonuments().monuments

        return apiMonuments.map {
                apiMonument -> apiMonumentToMonumentMapper.mapMonument(apiMonument)
        }
    }

    suspend fun getMonument(monumentId: String): Monument {
        val apiMonument = cityLightsService.getMonument(monumentId)

        return apiMonumentToMonumentMapper.mapMonument(apiMonument)
    }
}