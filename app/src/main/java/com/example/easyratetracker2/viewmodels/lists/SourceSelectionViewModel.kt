package com.example.easyratetracker2.viewmodels.lists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.easyratetracker2.data.models.SourceSelectionModel
import com.example.easyratetracker2.data.repositories.SelectableSourceRepository
import com.example.easyratetracker2.data.sources.PositionRateSource
import com.example.easyratetracker2.viewmodels.createPagingDataFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SourceSelectionViewModel @Inject constructor(private var repository: SelectableSourceRepository) : ViewModel(){

    private val _sourceList = MutableStateFlow<Flow<PagingData<SourceSelectionModel>>?>(null)
    val sourceList: Flow<Flow<PagingData<SourceSelectionModel>>?> = _sourceList

    init {
        viewModelScope.launch { refreshSourceList() }
    }

    private suspend fun refreshSourceList(){

        repository.checkSelectableSources()

        _sourceList.value = viewModelScope.createPagingDataFlow(
            PositionRateSource.INITIAL_KEY
        ){
            repository.getAllSourcesForList()
        }
    }

}