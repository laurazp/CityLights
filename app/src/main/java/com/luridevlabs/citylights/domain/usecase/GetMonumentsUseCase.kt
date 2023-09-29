package com.luridevlabs.citylights.domain.usecase

import com.luridevlabs.citylights.domain.MonumentsRepository
import com.luridevlabs.citylights.model.Monument

class GetMonumentsUseCase(
    private val monumentsRepository: MonumentsRepository
) {


    suspend fun execute(): List<Monument> {
        return monumentsRepository.getMonuments()
    }
}