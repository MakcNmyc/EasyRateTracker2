package com.example.easyratetracker2.viewmodels

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import com.example.easyratetracker2.Settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

fun <K : Any, V : Any> CoroutineScope.createPagingDataFlow(
    initialKey: K?,
    config: PagingConfig = createBaseConfig(),
    pagingSourceFactory: () -> PagingSource<K, V>
): Flow<PagingData<V>> =
    Pager(
        config = config,
        initialKey = initialKey,
        pagingSourceFactory = pagingSourceFactory
    ).flow
        .cachedIn(this)

private fun createBaseConfig() = PagingConfig(
    pageSize = Settings.PAGING_SIZE,
    enablePlaceholders = false
)

