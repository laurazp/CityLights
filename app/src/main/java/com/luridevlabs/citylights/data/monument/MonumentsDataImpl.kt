package com.luridevlabs.citylights.data.monument

import com.luridevlabs.citylights.data.monument.remote.mapper.MonumentResponseToMonumentMapper
import com.luridevlabs.citylights.data.monument.remote.MonumentsRemoteImpl
import com.luridevlabs.citylights.domain.MonumentsRepository
import com.luridevlabs.citylights.model.Monument

class MonumentsDataImpl (
    private val monumentsRemoteImpl: MonumentsRemoteImpl,
    //private val monumentsLocalImpl: MonumentsLocalImpl
    private val apiMonumentToMonumentMapper: MonumentResponseToMonumentMapper
) : MonumentsRepository {

    override suspend fun getMonuments(page: Int): List<Monument> {
        val apiMonuments = monumentsRemoteImpl.getMonuments()
        val monumentList: MutableList<Monument> = mutableListOf()

        apiMonuments.forEach { apiMonument ->
            val mappedMonument = apiMonumentToMonumentMapper.mapMonument(apiMonument, isFavorite = false)
            monumentList.add(mappedMonument)
        }
        return monumentList
    }

    override suspend fun getMonument(monumentId: String): Monument {
        return monumentsRemoteImpl.getMonument(monumentId)
    }

    /*override fun saveMonuments(monuments: List<Monument>) {
        monumentsLocalImpl.saveMonuments(monuments)
    }*/

}