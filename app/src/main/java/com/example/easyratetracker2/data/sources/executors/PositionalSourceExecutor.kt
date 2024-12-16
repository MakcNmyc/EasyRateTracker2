package com.example.easyratetracker2.data.sources.executors

import androidx.paging.PagingSource.LoadResult
import com.example.easyratetracker2.data.sources.PositionRateSource
import kotlin.reflect.KSuspendFunction2

abstract class PositionalSourceExecutor<T : Any>: ServiceSourceExecutor(){

    abstract suspend fun execute(
        startPosition: Int,
        loadSize: Int,
    ) : LoadResult<Int, T>

    internal suspend inline fun createResult(
        startPosition: Int,
        loadSize: Int,
        fetchData: KSuspendFunction2<Int, Int, List<T>>
    ) = fetchData(startPosition, loadSize).let {
        LoadResult.Page(
            it,
            if(startPosition == PositionRateSource.INITIAL_KEY) null else startPosition - 1,
            if(it.size == loadSize) loadSize + startPosition else null
        )
    }

    internal fun createErrorResult(e: Throwable) = LoadResult.Error<Int, T>(e)

}