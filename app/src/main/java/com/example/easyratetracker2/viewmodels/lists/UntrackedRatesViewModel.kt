package com.example.easyratetracker2.viewmodels.lists

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.paging.DataSource
import androidx.paging.PagedList
import com.example.easyratetracker2.adapters.util.NetworkObserver
import com.example.easyratetracker2.data.sources.factories.UntrackedSourceFactory
import com.example.easyratetracker2.ui.viewmodels.lists.BaseListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.parceler.Parcels
import java.util.*
import javax.inject.Inject

@HiltViewModel
class UntrackedRatesViewModel<V> @Inject constructor(
    pageListProducer: () -> LiveData<PagedList<V>>,
    savedStateHandle: SavedStateHandle
) : BaseListViewModel<V>(pageListProducer) {

    @Inject lateinit var networkObserver: NetworkObserver
    @Inject lateinit var sourceFactory: UntrackedSourceFactory

    private val item: MutableLiveData<UntrackedListItem> = MutableLiveData<UntrackedListItem>()

    init {
        initItem(savedStateHandle.get(ITEM_NAME))
    }

    private fun initItem(data: Parcelable?) {
        doAsynchronously({ Parcels.unwrap(data) }, item::postValue)
    }

    protected fun createPageList(): LiveData<PagedList<UntrackedRatesElementItem>> {
        return createPageListFromDataSourceFactory(createAndroidSourceFactory())
    }

    fun <T> createAndroidSourceFactory(): DataSource.Factory<T, UntrackedRatesElementItem> {
        return object : DataSource.Factory<T, UntrackedRatesElementItem>() {
            override fun create(): DataSource<T, UntrackedRatesElementItem> {
                return sourceFactory.create(
                    Objects.requireNonNull<Any?>(item.getValue()).getReceivingMethod(),
                    networkObserver
                )
            }
        }
    }

    fun getItem(): LiveData<UntrackedListItem> {
        return item
    }

    companion object {
        private const val ITEM_NAME = "untrackedListDescription"
    }
}