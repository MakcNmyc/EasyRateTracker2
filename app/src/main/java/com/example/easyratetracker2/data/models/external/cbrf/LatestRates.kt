package com.example.easyratetracker2.data.models.external.cbrf

import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "ValCurs", strict = false)
data class LatestRates constructor(
    @field:ElementList(name = "Valute", inline = true, required = true)
    @param:ElementList(name = "Valute", inline = true, required = true)
    var rateList: List<Rate>,
)

