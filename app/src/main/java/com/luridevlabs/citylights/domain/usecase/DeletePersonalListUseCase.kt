package com.luridevlabs.citylights.domain.usecase

import com.luridevlabs.citylights.domain.MonumentListsRepository
import com.luridevlabs.citylights.model.MonumentList

class DeletePersonalListUseCase(
    private val monumentsListRepository: MonumentListsRepository,
) {

    fun execute(listId: Long): List<MonumentList> {
        return monumentsListRepository.deletePersonalList(listId)
    }
}