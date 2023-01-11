package com.example.easyratetracker2.viewmodels

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.easyratetracker2.Settings

fun <V> DataSource.Factory<*, V>.createPageListFromDataSourceFactory(
    config: PagedList.Config = createBaseConfig()
): LiveData<PagedList<V>> {
    return LivePagedListBuilder(this, config).build()
}

private fun createBaseConfig() : PagedList.Config{
    return PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setPageSize(Settings.PAGING_SIZE)
        .build()
}