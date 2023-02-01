package com.example.easyratetracker2

import android.content.Context
import android.icu.text.NumberFormat
import java.util.*

class MultilingualSup{

    companion object{

        const val SUPPORTED_LANGUAGE_RU = "ru"
        const val SUPPORTED_LANGUAGE_EN = "en"
        private const val DEFAULT_LANGUAGE = SUPPORTED_LANGUAGE_EN

        val RATE_VALUE_INSTANCE: NumberFormat = NumberFormat.getNumberInstance().apply {
            maximumFractionDigits = 4
        }

        fun getPrimaryLanguage(context: Context): String = context.resources.configuration.locales[0].language

        //easy way find all language link
        fun takeLangByLangCollection(languages: Collection<String>, lang: String): String {
            return if (languages.contains(lang)) {
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


