package com.example.easyratetracker2.data.sources.executors

import androidx.paging.PagingSource.LoadResult
import com.example.easyratetracker2.data.models.UntrackedRatesElementModel
import com.example.easyratetracker2.di.AppEntryPoint
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class CbrfLatestUntrackedElement @Inject constructor() : PositionalSourceExecutor<UntrackedRatesElementModel>() {

    override suspend fun execute(
        startPosition: Int,
        loadSize: Int,
    ): LoadResult<Int, UntrackedRatesElementModel> =
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
        .map { UntrackedRatesElementModel(it) }
        .toList()

    private fun createCbrfService() = EntryPointAccessors.fromApplication(
        context,
        AppEntryPoint::class.java
    ).createCbrfService()

}