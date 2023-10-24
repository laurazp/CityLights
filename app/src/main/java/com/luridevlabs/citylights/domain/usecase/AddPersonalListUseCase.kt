package com.luridevlabs.citylights.domain.usecase

import com.luridevlabs.citylights.domain.MonumentListsRepository
import com.luridevlabs.citylights.model.MonumentList

class AddPersonalListUseCase(
    private val monumentsListRepository: MonumentListsRepository,
) {

    fun execute(listName: String): List<MonumentList> {
        return monumentsListRepository.addPersonalList(listName)
    }
}