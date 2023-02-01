package com.example.easyratetracker2.viewmodels.lists

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.PagedList
import androidx.room.InvalidationTracker
import com.example.easyratetracker2.adapters.util.NetworkObserver
import com.example.easyratetracker2.data.models.TrackedRatesElementModel
import com.example.easyratetracker2.data.sources.PositionRateSource
import com.example.easyratetracker2.data.sources.executors.TrackedRatesExecutor
import com.example.easyratetracker2.data.store.database.AppDatabase
import com.example.easyratetracker2.viewmodels.createPageListFromDataSourceFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TrackedRatesViewModel @Inject constructor(val networkObserver: NetworkObserver,
                                                val executor: TrackedRatesExecutor,
                                                val database: AppDatabase) : ViewModel() {

    private val refreshObserver = object : InvalidationTracker.Observer(AppDatabase.TABLE_NAME_TRACKED_RATES) {
        override fun onInvalidated(tables: Set<String>) {
            refreshPagedList()
        }
    }

    init {
        database.invalidationTracker.addObserver(refreshObserver)
    }

    var pagedList = createPagedList()

    private fun createPagedList(): LiveData<PagedList<TrackedRatesElementModel>> =
        createSourceFactory().createPageListFromDataSourceFactory()

    private fun createSourceFactory(): DataSource.Factory<Int, TrackedRatesElementModel> {
        return object : DataSource.Factory<Int, TrackedRatesElementModel>() {
            override fun create(): DataSource<Int, TrackedRatesElementModel> {
                return PositionRateSource(networkObserver, viewModelScope, executor)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        database.invalidationTracker.removeObserver(refreshObserver)
    }

    fun refreshPagedList(){
        pagedList = createPagedList()
    }
}