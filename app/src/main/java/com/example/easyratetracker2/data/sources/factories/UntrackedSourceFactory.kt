package com.example.easyratetracker2.data.sources.factories

import android.content.Context
import androidx.paging.DataSource
import com.example.easyratetracker2.adapters.util.NetworkObserver
import com.example.easyratetracker2.data.models.UntrackedRatesElementModel
import com.example.easyratetracker2.data.sources.executors.ServiceSourceExecutor
import com.example.easyratetracker2.di.AppEntryPoint
import dagger.hilt.android.EntryPointAccessors.fromApplication
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class UntrackedSourceFactory @Inject constructor() {

    @ApplicationContext
    @Inject
    var context: Context? = null

    fun create(
        receivingMethod: Int,
        networkObserver: NetworkObserver
    ): DataSource<*, UntrackedRatesElementModel> {
        return when (receivingMethod) {
            ServiceSourceExecutor.CBRF_SERVICE -> createPositionSource(
                receivingMethod,
                networkObserver
            )
            else -> throw IllegalArgumentException()
        }
    }

    private fun createPositionSource(
        receivingMethod: Int,
        networkObserver: NetworkObserver
    ): UntrackedPositionSource {
        return fromApplication(context!!, AppEntryPoint::class.java)
            .createUntrackedPositionSourceFactory()
            .create(receivingMethod, networkObserver)
    }
}