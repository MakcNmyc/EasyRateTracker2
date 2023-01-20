package com.example.easyratetracker2.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.easyratetracker2.data.models.StoredDetailsModel
import com.example.easyratetracker2.data.store.room.TrackedRate
import com.example.easyratetracker2.data.store.database.AppDatabase.Companion.TABLE_NAME_TRACKED_RATES

@Dao
interface TrackedRateDao : BaseDao<TrackedRate> {
//    @Query("SELECT id, outerId, sourceId FROM tracked_rates LIMIT :limit OFFSET :offset")
//    fun getTrackedRatesForList(limit: Int, offset: Int): List<TrackedIdItem>

    @Query(
        "SELECT id, " +
                "id != 0 AS tracked " +
                "FROM " +
                "(SELECT id AS id FROM $TABLE_NAME_TRACKED_RATES WHERE outerId = :outerId " +
                "UNION ALL " +
                "SELECT 0 " +
                "LIMIT 1) "
    )
    fun getTrackedDetailsItem(outerId: String): LiveData<StoredDetailsModel>
}