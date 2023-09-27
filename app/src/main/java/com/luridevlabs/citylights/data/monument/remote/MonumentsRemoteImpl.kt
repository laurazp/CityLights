package com.luridevlabs.citylights.data.monument.remote

import com.luridevlabs.citylights.data.remote.CityLightsService
import com.luridevlabs.citylights.model.Monument

class MonumentsRemoteImpl (
    private val cityLightsService: CityLightsService
) {

    suspend fun getMonuments(): List<Monument> {
        return cityLightsService.getMonuments().monuments
    }

    suspend fun getMonument(monumentId: Long): Monument {
        return cityLightsService.getMonument(monumentId)
    }
}