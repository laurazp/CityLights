package com.luridevlabs.citylights.data.monument

import com.luridevlabs.citylights.data.monument.remote.MonumentsRemoteImpl
import com.luridevlabs.citylights.data.personallist.db.ListsDatabaseImpl
import com.luridevlabs.citylights.domain.MonumentsRepository
import com.luridevlabs.citylights.model.Monument
import kotlinx.coroutines.flow.Flow

class MonumentsDataImpl (
    private val monumentsRemoteImpl: MonumentsRemoteImpl,
    private val listsDatabaseImpl: ListsDatabaseImpl
) : MonumentsRepository {

    override suspend fun getMonuments(): List<Monument> {
        return monumentsRemoteImpl.getMonuments()
    }

    override suspend fun getMonumentsPaging(page: Int, limit: Int): Flow<List<Monument>> {
        return monumentsRemoteImpl.getMonumentsPaging(page, limit)
    }

    override suspend fun getMonument(monumentId: String): Monument {
        val monument = monumentsRemoteImpl.getMonument(monumentId)
        monument.isFavorite = listsDatabaseImpl.getList(1).monuments.any {
            it.monumentId == monument.monumentId
        }
        return monument
    }
}