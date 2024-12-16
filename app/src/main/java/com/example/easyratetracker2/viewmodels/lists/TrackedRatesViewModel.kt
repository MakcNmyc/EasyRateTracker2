package com.example.easyratetracker2.viewmodels.lists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.room.InvalidationTracker
import com.example.easyratetracker2.adapters.util.NetworkObserver
import com.example.easyratetracker2.data.models.RatesElementModel
import com.example.easyratetracker2.data.sources.PositionRateSource
import com.example.easyratetracker2.data.sources.executors.TrackedRatesExecutor
import com.example.easyratetracker2.data.store.database.AppDatabase
import com.example.easyratetracker2.viewmodels.createPagingDataFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class TrackedRatesViewModel @Inject constructor(val networkObserver: NetworkObserver,
                                                val executor: TrackedRatesExecutor,
                                                val database: AppDatabase) : ViewModel() {

    private val _trackedRateList = MutableStateFlow(createRateList())
    val trackedRateList: Flow<Flow<PagingData<RatesElementModel>>> = _trackedRateList

    private val refreshObserver = object : InvalidationTracker.Observer(AppDatabase.TABLE_NAME_TRACKED_RATES) {
        override fun onInvalidated(tables: Set<String>) {
            refreshRateList()
        }
    }

    init {
        database.invalidationTracker.addObserver(refreshObserver)
    }

    override fun onCleared() {
        super.onCleared()
        database.invalidationTracker.removeObserver(refreshObserver)
    }

    fun refreshRateList() {
        _trackedRateList.value = createRateList()
    }

    fun createRateList() =
        viewModelScope.createPagingDataFlow(
            PositionRateSource.INITIAL_KEY
        ) { PositionRateSource(networkObserver, executor) }


}