package com.example.easyratetracker2.data.models

import com.example.easyratetracker2.data.models.external.cbrf.Rate
import com.example.easyratetracker2.data.sources.executors.ServiceSourceExecutor

data class UntrackedRatesElementModel(
    override val id: String,
    val headline: String,
    val rate: String,
    val sourceId: Int
) : ListElementModel<String> {

    constructor(v: Rate): this(
        v.id,
        v.name,
        v.getValueForUnit(),
        ServiceSourceExecutor.CBRF_SERVICE)
}