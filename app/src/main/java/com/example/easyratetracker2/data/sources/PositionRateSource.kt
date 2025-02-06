package com.example.easyratetracker2.data.sources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.easyratetracker2.adapters.util.NetworkObserver
import com.example.easyratetracker2.data.sources.executors.PositionalSourceExecutor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.easyratetracker2.adapters.util.NetworkObserver.Status

class PositionRateSource<T: Any>(
    private val networkObserver: NetworkObserver,
    private val executor: PositionalSourceExecutor<T>,
) : PagingSource<Int, T>() {

    override fun getRefreshKey(state: PagingState<Int, T>): Int =
        state.anchorPosition?.plus(1) ?: 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {

        networkObserver.setStatus(Status.LOADING)

        return withContext(Dispatchers.IO) {
            executor.execute(params.key ?: 0, params.loadSize)
        }.also {
            when (it) {
                is LoadResult.Error -> networkObserver.addError(it.throwable)
                else -> networkObserver.setStatus(Status.READY)
            }
        }
    }

    companion object{
        const val INITIAL_KEY = 0
    }

}