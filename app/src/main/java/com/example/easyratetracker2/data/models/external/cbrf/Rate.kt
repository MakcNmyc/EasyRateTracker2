package com.example.easyratetracker2.data.models.external.cbrf

import com.example.easyratetracker2.Settings
import com.example.easyratetracker2.api.CbrfApi
import com.example.easyratetracker2.parseValue
import com.example.easyratetracker2.rateValueFormat
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "Valute", strict = false)
data class Rate(

    @field:Attribute(name = "ID")
    @param:Attribute(name = "ID", required = true)
    var id: String,

    @field:Element(name = "Value", required = true)
    @param:Element(name = "Value", required = true)
    var value: String,

    @field:Element(name = "Name", required = true)
    @param:Element(name = "Name", required = true)
    var name: String,

    @field:Element(name = "Nominal", required = true)
    @param:Element(name = "Nominal", required = true)
    var denomination: String
){
    // external method that return rate value = value / nominal
    fun getValueForUnit(): String {
        return try {
            (value.parseValue(CbrfApi.NUMBER_FORMAT_LOCALE).toFloat() /
                    denomination.parseValue(CbrfApi.NUMBER_FORMAT_LOCALE).toFloat()).rateValueFormat()
        } catch (e: Exception) {
            Settings.DEFAULT_RATE_VALUE
        }
    }
}