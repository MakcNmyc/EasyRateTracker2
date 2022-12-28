package com.example.easyratetracker2.data.models.external.cbrf

import com.example.easyratetracker2.Settings
import com.example.easyratetracker2.api.CbrfApi
import com.example.easyratetracker2.parseValue
import com.example.easyratetracker2.rateValueFormat
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "Valute", strict = false)
data class Rate @JvmOverloads constructor(

    @field:Attribute(name = "ID")
    @param:Attribute(name = "ID", required = false)
    var id: String? = null,

    @field:Element(name = "Value", required = false)
    @param:Element(name = "Value", required = false)
    var value: String? = null,

    @field:Element(name = "Name", required = false)
    @param:Element(name = "Name", required = false)
    var name: String? = null,

    @field:Element(name = "Nominal", required = false)
    @param:Element(name = "Nominal", required = false)
    var denomination: String? = null
){
    // external method that return rate value = value / nominal
    fun getValueForUnit(): String {
        return try {
            (value!!.parseValue(CbrfApi.NUMBER_FORMAT_LOCALE).toFloat() /
                    denomination!!.parseValue(CbrfApi.NUMBER_FORMAT_LOCALE).toFloat()).rateValueFormat()
        } catch (e: Exception) {
            Settings.DEFAULT_RATE_VALUE
        }
    }
}