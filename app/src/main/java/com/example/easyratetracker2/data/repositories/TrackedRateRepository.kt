package com.example.easyratetracker2.data.repositories

import androidx.lifecycle.LiveData
import com.example.easyratetracker2.api.services.CbrfService
import com.example.easyratetracker2.data.dao.TrackedRateDao
import com.example.easyratetracker2.data.models.StoredDetailsModel
import com.example.easyratetracker2.data.store.room.TrackedRate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrackedRateRepository @Inject constructor(var trackedDao: TrackedRateDao, var service: CbrfService) :
    RoomRepository<TrackedRate>(trackedDao) {

    fun getTrackedDetailsModel(id: String): LiveData<StoredDetailsModel> {
        return trackedDao.getTrackedDetailsItem(id)
    }

//    fun getTrackedIds(startPosition: Int, loadSize: Int): List<TrackedIdItem> {
//        return trackedDao.getTrackedRatesForList(loadSize, startPosition)
//    }
}