package com.example.easyratetracker2.data.sources.factories

import android.content.Context
import androidx.paging.PagingSource
import com.example.easyratetracker2.adapters.util.NetworkObserver
import com.example.easyratetracker2.data.models.RatesElementModel
import com.example.easyratetracker2.data.sources.PositionRateSource
import com.example.easyratetracker2.data.sources.executors.ServiceSourceExecutor
import com.example.easyratetracker2.di.AppEntryPoint
import dagger.hilt.android.EntryPointAccessors.fromApplication
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

// TODO: migrate to Concat adapter
class UntrackedSourceFactory @Inject constructor(private val networkObserver: NetworkObserver,
                                                 @ApplicationContext private var context: Context){

    @Suppress("UNCHECKED_CAST")
    fun <K : Any> create(receivingMethod: Int): PagingSource<K, RatesElementModel> =
        when (receivingMethod) {
            ServiceSourceExecutor.CBRF_SERVICE -> PositionRateSource(
                networkObserver,
                fromApplication(
                    context,
                    AppEntryPoint::class.java
                ).createCbrfLatestUntrackedElementExecutor()
            )

            else -> throw IndexOutOfBoundsException()
        } as PagingSource<K, RatesElementModel>


    fun getInitialKey(receivingMethod: Int) : Any =
        when (receivingMethod) {
            ServiceSourceExecutor.CBRF_SERVICE -> PositionRateSource.INITIAL_KEY
            else -> throw IndexOutOfBoundsException()
        }
}