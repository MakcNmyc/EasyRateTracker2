package com.example.easyratetracker2.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UntrackedListModel(
    var sourceName: String,
    var receivingMethod: Int
) : Parcelable {

    constructor(source: SourceSelectionModel): this(source.name, source.receivingMethod)

//    constructor()
//
//    companion object {
//        fun fromSourceSelectionItem(item: SourceSelectionItem): UntrackedListItem {
//            val result = UntrackedListItem()
//            result.sourceName = item.getName()
//            result.receivingMethod = item.getReceivingMethod()
//            return result
//        }
//    }
}