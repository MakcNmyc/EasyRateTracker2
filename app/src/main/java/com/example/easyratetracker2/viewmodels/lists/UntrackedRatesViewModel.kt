package com.example.easyratetracker2.viewmodels.lists

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.easyratetracker2.adapters.util.NetworkObserver
import com.example.easyratetracker2.data.models.UntrackedListModel
import com.example.easyratetracker2.data.models.RatesElementModel
import com.example.easyratetracker2.data.sources.factories.UntrackedSourceFactory
import com.example.easyratetracker2.viewmodels.createPagingDataFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UntrackedRatesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    @Inject lateinit var networkObserver: NetworkObserver
    @Inject internal lateinit var sourceFactory: UntrackedSourceFactory

    val model: StateFlow<UntrackedListModel?> = savedStateHandle.getStateFlow(MODEL_NAME, null)

    private val _untrackedRateList = MutableStateFlow<Flow<PagingData<RatesElementModel>>?>(null)

    fun init(){
        viewModelScope.launch {
            model.collectLatest{
                if (it != null) {
                    refreshRateList(it.receivingMethod)
                }
            }
        }
    }

    val untrackedRateList: Flow<Flow<PagingData<RatesElementModel>>?> = _untrackedRateList

    fun refreshRateList(){
        model.value?.let {
            viewModelScope.launch {
                refreshRateList(it.receivingMethod)
            }
        }
    }

    private fun refreshRateList(receivingMethod: Int){
        _untrackedRateList.value =  viewModelScope.createPagingDataFlow(
            sourceFactory.getInitialKey(receivingMethod)
        ) { sourceFactory.create(receivingMethod)}
    }

    companion object {
        const val MODEL_NAME = "untrackedListDescription"
    }
}


