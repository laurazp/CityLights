package com.luridevlabs.citylights.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.luridevlabs.citylights.data.MonumentsPaging


open class GetComposeMonumentListUseCase(
    private val pagingSource: MonumentsPaging
) {
    operator fun invoke(limit: Int) = Pager(
        config = PagingConfig(
            pageSize = limit,
            prefetchDistance = limit / 2
        ),
        pagingSourceFactory = {
            pagingSource
        }
    ).flow
}