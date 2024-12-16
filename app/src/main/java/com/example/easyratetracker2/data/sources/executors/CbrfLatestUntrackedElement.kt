package com.example.easyratetracker2.data.sources.executors

import androidx.paging.PagingSource.LoadResult
import com.example.easyratetracker2.data.models.RatesElementModel
import com.example.easyratetracker2.di.AppEntryPoint
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import javax.inject.Inject

class CbrfLatestUntrackedElement @Inject constructor() : PositionalSourceExecutor<RatesElementModel>() {

    override suspend fun execute(
        startPosition: Int,
        loadSize: Int,
    ): LoadResult<Int, RatesElementModel> =
        try {
            createResult(startPosition, loadSize, this::fetchData)
        } catch (e: Throwable) {
            createErrorResult(e)
        }

    private suspend fun fetchData(startPosition: Int, loadSize: Int) = getService(CBRF_SERVICE, ::createCbrfService).getLatestCurrencyRate()
        .rateList
        .asFlow()
        .drop(startPosition)
        .take(loadSize)
        .map { RatesElementModel(it) }
        .toList()

    private fun createCbrfService() = EntryPointAccessors.fromApplication(
        context,
        AppEntryPoint::class.java
    ).createCbrfService()

}