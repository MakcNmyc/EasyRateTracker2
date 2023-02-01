package com.example.easyratetracker2.viewmodels

import androidx.lifecycle.*
import com.example.easyratetracker2.data.models.OuterDetailsModel
import com.example.easyratetracker2.data.models.StoredDetailsModel
import com.example.easyratetracker2.data.repositories.TrackedRateRepository
import com.example.easyratetracker2.data.repositories.utilities.StorageRequest
import com.example.easyratetracker2.data.store.room.TrackedRate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RateDetailsViewModel @Inject constructor(savedStateHandle: SavedStateHandle): ViewModel(){

    @Inject
    internal lateinit var repository: TrackedRateRepository

    val outerDetails: MutableLiveData<OuterDetailsModel> = MutableLiveData()
    init {
        viewModelScope.launch {
            outerDetails.postValue(savedStateHandle[ITEM_NAME])
        }
    }

    val storedDetails: LiveData<StoredDetailsModel> =
        Transformations.switchMap(outerDetails) { model ->
            repository.getTrackedDetailsModel(model.outerId)
        }

    fun addToTracked(onDone: (StorageRequest<TrackedRate>) -> Unit) {
        val outer = outerDetails.value
        if (outer != null && storedDetails.value?.tracked == false){
            viewModelScope.launch(Dispatchers.IO) {
                StorageRequest(listOf(TrackedRate(outer.outerId, outer.sourceId)))
                    .also {
                        repository.saveToDb(it)
                        onDone(it)
                    }
            }
        }
    }

    fun deleteFromTracked(onDone: (StorageRequest<TrackedRate>) -> Unit) {
        val stored = storedDetails.value
        if (stored != null)
            viewModelScope.launch(Dispatchers.IO) {
                StorageRequest(listOf(TrackedRate(id = stored.id)))
                    .also {
                        repository.deleteFromDb(it)
                        onDone(it)
                    }
            }
    }
    companion object {
        private const val ITEM_NAME = "rateDetails"
    }
}