package com.luridevlabs.citylights.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.luridevlabs.citylights.domain.MonumentsRepository
import com.luridevlabs.citylights.domain.PagingConstants.Companion.PAGE_SIZE
import com.luridevlabs.citylights.model.Monument
import kotlinx.coroutines.flow.first
import timber.log.Timber

open class MonumentsPaging(
    private val repository: MonumentsRepository,
): PagingSource<Int, Monument>() {

    override fun getRefreshKey(state: PagingState<Int, Monument>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Monument> =
        try {
            val page = params.key ?: 0
            Timber.tag("Paging").i("Page: $page")
            val response = repository.getMonumentsPaging(
                page = page,
                limit = PAGE_SIZE
            ).first()

            LoadResult.Page(
                data = response,
                prevKey = if (page == 0) null else page.minus(1),
                nextKey = if (response.isEmpty()) null else page.plus(1),
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
}