package com.luridevlabs.citylights.data.monument.remote

import com.luridevlabs.citylights.data.monument.remote.mapper.MonumentResponseMapper
import com.luridevlabs.citylights.data.remote.CityLightsService
import com.luridevlabs.citylights.model.Monument
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MonumentsRemoteImpl(
    private val cityLightsService: CityLightsService,
    private val apiMonumentToMonumentMapper: MonumentResponseMapper
) {

    suspend fun getMonuments(): List<Monument> {
        val apiMonuments = cityLightsService.getMonuments().monuments

        return apiMonuments.map { apiMonument ->
            apiMonumentToMonumentMapper.mapFromRemote(apiMonument)
        }
    }

    suspend fun getMonumentsPaging(page: Int, limit: Int): Flow<List<Monument>> = flow {
        val apiMonuments = cityLightsService.getMonumentsPaging(
            offset = page * limit,
            limit = limit,
        )
        if (apiMonuments.monuments.isNotEmpty()) {
            emit(apiMonuments.monuments.map { apiMonument ->
                apiMonumentToMonumentMapper.mapFromRemote(apiMonument)
            })
        } else {
            emit(emptyList())
        }
    }

    suspend fun getMonument(monumentId: String): Monument {
        val apiMonument = cityLightsService.getMonument(monumentId)

        return apiMonumentToMonumentMapper.mapFromRemote(apiMonument)
    }
}