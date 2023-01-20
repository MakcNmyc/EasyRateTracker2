package com.example.easyratetracker2.data.models

import androidx.room.ColumnInfo

data class SourceSelectionModel(
    override val id: String,
    var name: String,
    var description: String,
    var currency: String,
    @ColumnInfo(name = "receiving_method") var receivingMethod: Int
) : ListElementModel<String>