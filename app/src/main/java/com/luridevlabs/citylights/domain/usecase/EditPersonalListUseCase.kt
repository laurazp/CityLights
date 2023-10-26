package com.luridevlabs.citylights.domain.usecase

import com.luridevlabs.citylights.domain.MonumentListsRepository
import com.luridevlabs.citylights.model.MonumentList

class EditPersonalListUseCase(
    private val monumentsListRepository: MonumentListsRepository,
) {

    fun execute(list: MonumentList): List<MonumentList> {
        return monumentsListRepository.editPersonalList(list)
    }
}