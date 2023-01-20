package com.example.easyratetracker2.api.services

import android.content.Context
import com.example.easyratetracker2.MultilingualSup
import com.example.easyratetracker2.MultilingualSup.Companion.SUPPORTED_LANGUAGE_EN
import com.example.easyratetracker2.MultilingualSup.Companion.SUPPORTED_LANGUAGE_RU
import com.example.easyratetracker2.api.CbrfApi
import com.example.easyratetracker2.data.models.external.cbrf.LatestRates
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface CbrfService {

    suspend fun getLatestCurrencyRate(): LatestRates

    class CbrfServiceImp @Inject constructor(@ApplicationContext context: Context) : CbrfService {
        @Inject lateinit var cbrfApi: CbrfApi
        var primaryLang: String = MultilingualSup.getPrimaryLanguage(context)
        private var supportedLanguages: List<String> = listOf(SUPPORTED_LANGUAGE_RU, SUPPORTED_LANGUAGE_EN)

        override suspend fun getLatestCurrencyRate(): LatestRates =
            when (MultilingualSup.takeLangByLangCollection(supportedLanguages, primaryLang)) {
                SUPPORTED_LANGUAGE_RU -> cbrfApi.latestCurrencyRateRU()
                SUPPORTED_LANGUAGE_EN -> cbrfApi.latestCurrencyRateEn()
                else -> throw IndexOutOfBoundsException()
            }
    }


}