package com.example.easyratetracker2.data.sources.executors

import com.example.easyratetracker2.data.models.TrackedIdModel
import com.example.easyratetracker2.data.models.TrackedRatesElementModel
import com.example.easyratetracker2.data.repositories.TrackedRateRepository
import com.example.easyratetracker2.di.AppEntryPoint
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class TrackedRatesExecutor @Inject constructor() : PositionalSourceExecutor<TrackedRatesElementModel>(){

    @Inject lateinit var repository: TrackedRateRepository

//    @FlowPreview
    override fun execute(
        scope: CoroutineScope,
        startPosition: Int,
        loadSize: Int,
        resultHandler: (result: List<TrackedRatesElementModel>) -> Unit,
        errorHandler: (e: Throwable) -> Unit
    ) {
        scope.launch (Dispatchers.IO){
            val ids = repository.getTrackedIds(startPosition, loadSize)

            if(ids.isEmpty()) {
                resultHandler(emptyList())
                return@launch
            }

            try {
                ids.groupBy { it.sourceId }
                    .map { getDataFromService(it.key, it.value) }
                    .asFlow()
                    .flatMapMerge { it }
                    .flowOn(Dispatchers.Default)
                    .toList()
                    .let { result ->  resultHandler(result)}
            } catch (e: Throwable) {
                errorHandler(e)
            }
        }
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