package com.example.easyratetracker2.data.repositories

import androidx.lifecycle.LiveData
import com.example.easyratetracker2.data.dao.TrackedRateDao
import com.example.easyratetracker2.data.models.StoredDetailsModel
import com.example.easyratetracker2.data.models.TrackedIdModel
import com.example.easyratetracker2.data.store.room.TrackedRate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class TrackedRateRepository @Inject constructor(private var trackedDao: TrackedRateDao) :
    RoomRepository<TrackedRate>(trackedDao) {

    fun getTrackedDetailsModel(id: String): LiveData<StoredDetailsModel> {
        return trackedDao.getTrackedDetailsItem(id)
    }

    open fun getTrackedIds(startPosition: Int, loadSize: Int): List<TrackedIdModel> {
        return trackedDao.getTrackedRatesForList(loadSize, startPosition)
    }
}