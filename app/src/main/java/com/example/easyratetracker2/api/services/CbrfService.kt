package com.example.easyratetracker2.api.services

import android.content.Context
import com.example.easyratetracker2.MultilingualSup
import com.example.easyratetracker2.api.CbrfApi
import com.example.easyratetracker2.data.models.external.cbrf.LatestRates
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface CbrfService {

    fun getLatestCurrencyRate(): Flow<LatestRates>

    class CbrfServiceImp @Inject constructor(context: Context): CbrfService {
        @Inject lateinit var cbrfApi: CbrfApi
        var primaryLang: String = MultilingualSup.getPrimaryLanguage(context)
        var latestCurrencyRateMethods: Map<String, () -> Flow<LatestRates>> = mapOf(
            "ru" to {cbrfApi.latestCurrencyRateRU()},
            "en" to {cbrfApi.latestCurrencyRateEn()}
        )

        override fun getLatestCurrencyRate(): Flow<LatestRates> =
            MultilingualSup.takeByLanguageMap(latestCurrencyRateMethods, primaryLang).invoke()

    }
}