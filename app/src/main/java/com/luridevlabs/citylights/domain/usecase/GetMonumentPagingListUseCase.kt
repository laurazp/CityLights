package com.luridevlabs.citylights.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.luridevlabs.citylights.data.MonumentsPaging
import com.luridevlabs.citylights.domain.PagingConstants.Companion.PAGE_SIZE

open class GetMonumentPagingListUseCase(
    private val pagingSource: MonumentsPaging
) {
    operator fun invoke() = Pager(
        config = PagingConfig(
            pageSize = PAGE_SIZE,
            prefetchDistance = PAGE_SIZE / 2
        ),
        pagingSourceFactory = {
            pagingSource
        }
    ).flow
}