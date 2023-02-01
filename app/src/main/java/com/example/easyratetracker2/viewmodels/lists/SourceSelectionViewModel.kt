package com.example.easyratetracker2.viewmodels.lists

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.example.easyratetracker2.data.models.SourceSelectionModel
import com.example.easyratetracker2.data.repositories.SelectableSourceRepository
import com.example.easyratetracker2.viewmodels.createPageListFromDataSourceFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SourceSelectionViewModel @Inject constructor(private var repository: SelectableSourceRepository) : ViewModel(){

    var pagedList: LiveData<PagedList<SourceSelectionModel>>? = null

    fun initNewPageList(onInitCallback: (pagedList: LiveData<PagedList<SourceSelectionModel>>) -> Unit){
        checkSelectableSources{
            val pagedListLocal = repository.getAllSourcesForList().createPageListFromDataSourceFactory()
            pagedList = pagedListLocal
            onInitCallback(pagedListLocal)
        }
    }

    private fun checkSelectableSources(onCheckCallback: () -> Unit) {
        repository.checkSelectableSources(this.viewModelScope, onCheckCallback)
    }
}