package com.example.easyratetracker2.adapters.util

import android.content.Context
import android.os.Looper
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.easyratetracker2.R
import com.example.easyratetracker2.adapters.util.NetworkObserver.Status
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NetworkObserverImpl @Inject constructor(): NetworkObserver {

    @ApplicationContext
    @Inject
    lateinit var context: Context
    private val errors: MutableList<Throwable> by lazy{ArrayList()}
    override var previousStatus: Int? = null

    private var _status: MutableLiveData<Int> = MutableLiveData(Status.INIT)
    override var status
        get() = _status.value!!
        set(newValue) {
            synchronized(this){
                _status.value.let { currentValue ->
                    if (currentValue != Status.ERROR && newValue == Status.ERROR) errors.clear()
                    previousStatus = currentValue
                    if (Looper.myLooper() == Looper.getMainLooper()) {
                        _status.value = newValue
                    } else {
                        _status.postValue(newValue)
                    }
                }
            }
        }

    override fun observeStatus(lifecycleOwner: LifecycleOwner, lifecycleObserver: Observer<Int>) {
        _status.observe(lifecycleOwner, lifecycleObserver)
    }

    override fun observeStatusOnce(endTrigger: (Int)->Boolean) {
        _status.observeForever(object: Observer<Int>{
            override fun onChanged(newValue: Int){
                if (endTrigger(newValue)) _status.removeObserver(this)
            }
        })
    }

    override fun addError(e: Throwable) {
        status = Status.ERROR
        errors.add(e)
    }

    override val errorsDescription: String
        get() = StringBuilder(context.getString(R.string.error_introductory)).also {
            errors.forEach { e -> it.append(e) }
        }.toString()
}