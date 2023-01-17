package com.example.easyratetracker2.data.repositories

import com.example.easyratetracker2.data.dao.BaseDao
import com.example.easyratetracker2.data.repositories.utilities.StorageRequest
import com.example.easyratetracker2.data.store.room.RoomDataObj

abstract class RoomRepository<T : RoomDataObj>(var dao: BaseDao<T>) {

    private class SavingObjects<T : RoomDataObj>(objList: List<T>) {
        val objectsForInsert: List<T> = objList.filter { v -> !v.itExistInStorage() }
        val objectsForUpdate: List<T> = objList.filter { v -> v.itExistInStorage() }
    }

    private fun updateObjects(savingObjects: SavingObjects<T>, request: StorageRequest<T>) {
        if (!request.isDeny
            && savingObjects.objectsForUpdate.isNotEmpty()
            && dao.update(savingObjects.objectsForUpdate) != savingObjects.objectsForUpdate.size
        ) {
            request.addError(StorageRequest.UPDATE_ERROR)
        }
    }

    private fun insertObjects(savingObjects: SavingObjects<T>, request: StorageRequest<T>) {
        if (!request.isDeny
            && savingObjects.objectsForInsert.isNotEmpty()
        ) {
            val ids: List<Long> = dao.insert(savingObjects.objectsForInsert)
            if (ids.size != savingObjects.objectsForInsert.size) {
                request.addError(StorageRequest.INSERT_ERROR)
            }else{
                savingObjects.objectsForInsert.forEachIndexed { index, v -> v.id = ids[index] }
            }
        }
    }

    /** Separates objects from list by "for insert" and "for update",
     * inserts and updates them. After insert set new ids
     *
     */
    fun saveToDb(operations: StorageRequest<T>): RoomRepository<T> {
        if (!operations.isDeny){
            val savingObjects = SavingObjects(operations.objects)
            insertObjects(savingObjects, operations)
            updateObjects(savingObjects, operations)
        }
        return this
    }

    fun deleteFromDb(operations: StorageRequest<T>): RoomRepository<T> {
        if (!operations.isDeny) {
            dao.remove(operations.objects)
        }
        return this
    }

    companion object {

        const val DEFAULT_ID: Long = 0
        const val STRING_STUB: String = ""

        fun itNewObjId(id: Long?): Boolean {
            return id == null || id == DEFAULT_ID
        }
    }
}