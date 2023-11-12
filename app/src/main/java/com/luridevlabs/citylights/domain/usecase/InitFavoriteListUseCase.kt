package com.luridevlabs.citylights.domain.usecase

import com.luridevlabs.citylights.domain.MonumentListsRepository

class InitFavoriteListUseCase(
    private val monumentsListRepository: MonumentListsRepository,
) {

    fun execute() {
        return monumentsListRepository.initFavoriteList()
    }
}