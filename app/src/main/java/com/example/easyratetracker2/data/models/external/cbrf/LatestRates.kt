package com.example.easyratetracker2.data.models.external.cbrf

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Root

@Root(name = "ValCurs", strict = false)
data class LatestRates @JvmOverloads constructor(
    @field:Attribute(name = "Valute", required = false)
    @param:Attribute(name = "Valute", required = false)
    var rateList: List<Rate>? = null,
)