package com.example.easyratetracker2.adapters.util

import android.content.Context
import android.os.Looper
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.easyratetracker2.R
import com.example.easyratetracker2.adapters.util.NetworkObserver.*
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NetworkObserverImpl @Inject constructor(): NetworkObserver {

    @ApplicationContext
    @Inject
    lateinit var context: Context
    private val errors: MutableList<Throwable> by lazy{ArrayList()}

    private var _status = MutableLiveData(StatusData(Status.INIT, null))
    override var status
        get() = _status.value!!.newStatus
        set(newStatus) {
            synchronized(this){
                _status.value!!.let { data ->
                    if(data.newStatus == newStatus) return
                    if (newStatus != Status.ERROR && errors.size > 0) errors.clear()
                    if (Looper.myLooper() == Looper.getMainLooper()) {
                        _status.value = StatusData(newStatus, data.newStatus)
                    } else {
                        _status.postValue(StatusData(newStatus, data.newStatus))
                    }
                }
            }
        }

    override fun observeStatusData(lifecycleOwner: LifecycleOwner, lifecycleObserver: Observer<StatusData>) {
        _status.observe(lifecycleOwner, lifecycleObserver)
    }

    override fun observeStatusBeforeTriggered(endTrigger: (StatusData)->Boolean) {
        _status.observeForever(object: Observer<StatusData>{
            override fun onChanged(newValue: StatusData){
                if (endTrigger(newValue)) _status.removeObserver(this)
            }
        })
    }

    override fun addError(e: Throwable) {
        errors.add(e)
        status = Status.ERROR
    }

    override val errorsDescription: String
        get() = StringBuilder(context.getString(R.string.error_introductory)).also {
            errors.forEach { e -> it.append(e) }
        }.toString()
}