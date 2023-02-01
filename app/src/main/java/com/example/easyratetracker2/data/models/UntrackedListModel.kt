package com.example.easyratetracker2.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UntrackedListModel(
    var sourceName: String,
    var receivingMethod: Int
) : Parcelable {
    constructor(source: SourceSelectionModel): this(source.name, source.receivingMethod)
}