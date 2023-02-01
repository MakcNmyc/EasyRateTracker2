package com.example.easyratetracker2.viewmodels.lists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.PagedList
import com.example.easyratetracker2.adapters.util.NetworkObserver
import com.example.easyratetracker2.data.models.UntrackedListModel
import com.example.easyratetracker2.data.models.UntrackedRatesElementModel
import com.example.easyratetracker2.data.sources.executors.ServiceSourceExecutor.Companion.CBRF_SERVICE
import com.example.easyratetracker2.data.sources.factories.UntrackedSourceFactory
import com.example.easyratetracker2.viewmodels.createPageListFromDataSourceFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class UntrackedRatesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    @Inject lateinit var networkObserver: NetworkObserver
    @Inject internal lateinit var sourceFactory: UntrackedSourceFactory

    val model: MutableLiveData<UntrackedListModel> = MutableLiveData()

    init {
        viewModelScope.launch {
            model.postValue(savedStateHandle[MODEL_NAME])
        }
    }

    var pagedList = createNewPagedList()

    private fun createNewPagedList(): LiveData<PagedList<UntrackedRatesElementModel>>{
        return Transformations.switchMap(model) { v ->
            if (v != null) {
                createPageList()
            } else {
                MutableLiveData()
            }
        }
    }

    fun refreshPagedList(){
        pagedList = createNewPagedList()
    }

    private fun createPageList(): LiveData<PagedList<UntrackedRatesElementModel>> =
        createSourceFactory().createPageListFromDataSourceFactory()

    @Suppress("UNCHECKED_CAST")
    private fun createSourceFactory(): DataSource.Factory<Any, UntrackedRatesElementModel> {
        return object : DataSource.Factory<Any, UntrackedRatesElementModel>() {
            override fun create(): DataSource<Any, UntrackedRatesElementModel> {
                return sourceFactory.create(model.value!!.receivingMethod, this@UntrackedRatesViewModel) as DataSource<Any, UntrackedRatesElementModel>
            }
        }
    }

    companion object {
        const val MODEL_NAME = "untrackedListDescription"
    }
}


