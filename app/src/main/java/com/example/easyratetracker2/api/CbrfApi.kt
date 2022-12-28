package com.example.easyratetracker2.api

import com.example.easyratetracker2.data.models.external.cbrf.LatestRates
import kotlinx.coroutines.flow.Flow
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import retrofit2.http.GET
import java.io.IOException
import java.util.*

interface CbrfApi {

    @GET("scripts/XML_daily.asp")
    fun latestCurrencyRateRU(): Flow<LatestRates>

    @GET("scripts/XML_daily_eng.asp")
    fun latestCurrencyRateEn(): Flow<LatestRates>

    object Builder {
        fun build(okHttpBuilder: OkHttpClient.Builder): CbrfApi {
            return build(
                URL, okHttpBuilder
                    .addInterceptor(Windows1251EncodingInterceptor())
                    .build()
            )
        }

        fun build(url: String, okHttpClient: OkHttpClient): CbrfApi {
            return Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(CbrfApi::class.java)
        }
    }

    class Windows1251EncodingInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalResponse = chain.proceed(chain.request())
            val responseBody = originalResponse.body()
            return if (responseBody != null) {
                val newBody = ResponseBody.create(
                    MediaType.parse("application/xml; charset=windows-1251"),
                    responseBody.bytes()
                )
                originalResponse
                    .newBuilder()
                    .addHeader("Accept", "application/xml")
                    .body(newBody)
                    .build()
            } else {
                originalResponse
            }
        }
    }

    companion object {
        private const val URL = "https://www.cbr.ru/"
        val NUMBER_FORMAT_LOCALE = Locale("ru")
    }
}