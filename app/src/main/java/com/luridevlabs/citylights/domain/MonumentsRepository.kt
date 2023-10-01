package com.luridevlabs.citylights.domain

import com.luridevlabs.citylights.model.Monument

interface MonumentsRepository {

    suspend fun getMonuments(page: Int): List<Monument>

    suspend fun getMonument(monumentId: Long): Monument
}