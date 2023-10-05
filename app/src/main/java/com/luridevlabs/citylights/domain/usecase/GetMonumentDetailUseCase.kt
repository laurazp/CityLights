package com.luridevlabs.citylights.domain.usecase

import com.luridevlabs.citylights.domain.MonumentsRepository
import com.luridevlabs.citylights.model.Monument

class GetMonumentDetailUseCase (
    private val monumentsRepository: MonumentsRepository
) {

    suspend fun execute(monumentId: String) : Monument {
        return monumentsRepository.getMonument(monumentId)
    }
}