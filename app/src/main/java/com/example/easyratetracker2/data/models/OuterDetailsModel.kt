package com.example.easyratetracker2.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OuterDetailsModel(
    val outerId: String,
    val headline: String,
    val currentRate: String,
    val sourceId: Int
): Parcelable {
    constructor(el: UntrackedRatesElementModel) : this(
        el.id,
        el.headline,
        el.rate,
        el.sourceId
    )

    constructor(el: TrackedRatesElementModel) : this(
        el.id,
        el.headline,
        el.rate,
        el.sourceId
    )
}