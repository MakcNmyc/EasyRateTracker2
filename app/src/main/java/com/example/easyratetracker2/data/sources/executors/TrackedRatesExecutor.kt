package com.example.easyratetracker2.data.sources.executors

import com.example.easyratetracker2.data.models.TrackedIdModel
import com.example.easyratetracker2.data.models.TrackedRatesElementModel
import com.example.easyratetracker2.data.repositories.TrackedRateRepository
import com.example.easyratetracker2.di.AppEntryPoint
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import javax.inject.Inject

class TrackedRatesExecutor @Inject constructor() : PositionalSourceExecutor<TrackedRatesElementModel>(){

    @Inject lateinit var repository: TrackedRateRepository


    override suspend fun execute(
        startPosition: Int,
        loadSize: Int
    ) = try {
            createResult(startPosition, loadSize, this::fetchData)
        } catch (e: Throwable) {
            createErrorResult(e)
        }

    @OptIn(FlowPreview::class)
    private suspend fun fetchData(startPosition: Int, loadSize: Int) =
        repository.getTrackedIds(startPosition, loadSize).let { ids ->
            if (ids.isEmpty()) return emptyList<TrackedRatesElementModel>() else
                ids.groupBy { it.sourceId }
                    .map { getDataFromService(it.key, it.value) }
                    .asFlow()
                    .flatMapMerge { it }
                    .flowOn(Dispatchers.Default)
                    .toList()
        }


    private suspend fun getDataFromService(sourceId: Int, filteredList: List<TrackedIdModel>): Flow<TrackedRatesElementModel> {
        return when (sourceId) {
            CBRF_SERVICE -> getDataFromCbrf(filteredList)
            else -> throw IllegalArgumentException()
        }
    }

    private suspend fun getDataFromCbrf(filteredList: List<TrackedIdModel>): Flow<TrackedRatesElementModel> {

        val idsSet = filteredList.map { it.outerId }.toHashSet()

        return getService(CBRF_SERVICE, this::createCbrfService).getLatestCurrencyRate()
            .rateList
            .asFlow()
            .filter { idsSet.contains(it.id) }
            .map { TrackedRatesElementModel(it) }
    }

    private fun createCbrfService() = EntryPointAccessors.fromApplication(
        context,
        AppEntryPoint::class.java
    ).createCbrfService()
}