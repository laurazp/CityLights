package com.luridevlabs.citylights.domain

import com.luridevlabs.citylights.model.Monument
import kotlinx.coroutines.flow.Flow

interface MonumentsRepository {
    suspend fun getMonuments(): List<Monument>
    suspend fun getMonumentsPaging(page: Int, limit: Int): Flow<List<Monument>>
    suspend fun getMonument(monumentId: String): Monument
}