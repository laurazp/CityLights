package com.luridevlabs.citylights.data.monument

import com.luridevlabs.citylights.data.monument.local.MonumentsLocalImpl
import com.luridevlabs.citylights.data.monument.remote.MonumentsRemoteImpl
import com.luridevlabs.citylights.domain.MonumentsRepository
import com.luridevlabs.citylights.model.Monument

class MonumentsDataImpl (
    private val monumentsRemoteImpl: MonumentsRemoteImpl
    //private val monumentsLocalImpl: MonumentsLocalImpl
) : MonumentsRepository {

    override suspend fun getMonuments(): List<Monument> {
        return monumentsRemoteImpl.getMonuments()
    }

    override suspend fun getMonument(monumentId: Long): Monument {
        return monumentsRemoteImpl.getMonument(monumentId)
    }

    /*override fun saveMonuments(monuments: List<Monument>) {
        monumentsLocalImpl.saveMonuments(monuments)
    }*/

}