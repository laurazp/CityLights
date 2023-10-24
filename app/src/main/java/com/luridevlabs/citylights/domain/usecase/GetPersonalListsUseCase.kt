package com.luridevlabs.citylights.domain.usecase

import com.luridevlabs.citylights.domain.MonumentListsRepository
import com.luridevlabs.citylights.model.MonumentList

class GetPersonalListsUseCase(
    private val monumentsListRepository: MonumentListsRepository,
) {

    fun execute(): List<MonumentList> {
        return monumentsListRepository.getPersonalLists()
    }
}