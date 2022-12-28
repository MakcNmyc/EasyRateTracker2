package com.example.easyratetracker2.ui.viewmodels.lists

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList

abstract class BaseListViewModel<V> (val pageListProducer: () -> LiveData<PagedList<V>>) {

    var pageList: LiveData<PagedList<V>> = pageListProducer()

    fun createPageListFromDataSourceFactory(
        dataSourceFactory: DataSource.Factory<*, V>,
        config: PagedList.Config = createBaseConfig()
    ): LiveData<PagedList<V>> {
        return LivePagedListBuilder(dataSourceFactory, config).build()
    }

    fun recreatePagedList() {
        pageList = pageListProducer()
    }

    private fun createBaseConfig() : PagedList.Config{
        return PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(PAGING_SIZE)
            .build()
    }

    companion object{
        private const val PAGING_SIZE = 11
    }
}