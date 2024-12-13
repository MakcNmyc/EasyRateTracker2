package com.example.easyratetracker2.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.example.easyratetracker2.data.models.SourceSelectionModel
import com.example.easyratetracker2.data.store.database.AppDatabase.Companion.TABLE_NAME_SELECTABLE_SOURCES
import com.example.easyratetracker2.data.store.room.SelectableSource

@Dao
interface SelectableSourceDao : BaseDao<SelectableSource> {

    @Query("SELECT id, name, description, currency, receiving_method " +
            "FROM $TABLE_NAME_SELECTABLE_SOURCES " +
            "WHERE language = :language")
    fun getAllSourcesForList(language: String): PagingSource<Int, SourceSelectionModel>

    @Query(
        "SELECT " +
                "id != 0 " +
                "FROM " +
                "(SELECT id AS id FROM $TABLE_NAME_SELECTABLE_SOURCES WHERE language = :language " +
                "UNION ALL " +
                "SELECT 0 " +
                "LIMIT 1) "
    )
    suspend fun selectableResourcesIsInitialized(language: String): Boolean
}