package com.example.easyratetracker2

import android.content.Context
import android.icu.text.NumberFormat
import java.util.*

class MultilingualSup{

    companion object{

        private const val DEFAULT_LANGUAGE = "en"

        val RATE_VALUE_INSTANCE: NumberFormat = NumberFormat.getNumberInstance().apply {
            maximumFractionDigits = 4
        }


        fun getPrimaryLanguage(context: Context) = context.resources.configuration.locales[0].language

        //easy way find all language link
        fun <T> takeByLanguageMap(map: Map<String, T>, primaryLang: String): T {
            return map[takeLangByLangMap(map, primaryLang)] ?: throw IndexOutOfBoundsException()
        }

        fun takeLangByLangMap(map: Map<String, *>, lang: String): String {
            return if (map.containsKey(lang)) {
                lang
            } else {
                DEFAULT_LANGUAGE
            }
        }
    }
}

fun String.parseValue(locale: Locale): Number =
    NumberFormat.getNumberInstance(locale).parse(this)

fun Number.rateValueFormat(): String = MultilingualSup.RATE_VALUE_INSTANCE.format(this)


