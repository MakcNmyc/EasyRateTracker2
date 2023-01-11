package com.example.easyratetracker2.data.sources.factories

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.paging.DataSource
import com.example.easyratetracker2.adapters.util.NetworkObserver
import com.example.easyratetracker2.data.models.UntrackedRatesElementModel
import com.example.easyratetracker2.data.sources.PositionRateSource
import com.example.easyratetracker2.data.sources.executors.ServiceSourceExecutor
import com.example.easyratetracker2.di.AppEntryPoint
import dagger.hilt.android.EntryPointAccessors.fromApplication
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class UntrackedSourceFactory @Inject constructor(private val networkObserver: NetworkObserver,
                                                 @ApplicationContext private var context: Context){

    fun create(receivingMethod: Int, vm: ViewModel): DataSource<*, UntrackedRatesElementModel> {
        return when (receivingMethod) {
            ServiceSourceExecutor.CBRF_SERVICE -> PositionRateSource(networkObserver,
                vm,
                fromApplication(context, AppEntryPoint::class.java).createCbrfLatestCurrencyRate())
            else -> throw IllegalArgumentException()
        }
    }
}