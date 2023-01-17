package com.example.easyratetracker2.data.sources.executors

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easyratetracker2.data.models.UntrackedRatesElementModel
import com.example.easyratetracker2.di.AppEntryPoint
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class CbrfLatestCurrencyRate @Inject constructor() : PositionalSourceExecutor<UntrackedRatesElementModel>() {

    override fun execute(
        vm: ViewModel,
        startPosition: Int,
        loadSize: Int,
        resultHandler: (result: List<UntrackedRatesElementModel>) -> Unit,
        errorHandler: (e: Throwable) -> Unit
    ) {
        vm.viewModelScope.launch {
            try {
                resultHandler(
                    getService(CBRF_SERVICE, ::createCbrfService).getLatestCurrencyRate()
                        .rateList
                        .asFlow()
                        .drop(startPosition)
                        .take(loadSize)
                        .map { UntrackedRatesElementModel(it) }
                        .toList());
            } catch (e: Throwable) {
                errorHandler(e)
            }
        }
    }

    private fun createCbrfService() = EntryPointAccessors.fromApplication(
        context,
        AppEntryPoint::class.java
    ).createCbrfService()
}