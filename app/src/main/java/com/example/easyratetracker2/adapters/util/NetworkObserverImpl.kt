package com.example.easyratetracker2.adapters.util

import android.content.Context
import com.example.easyratetracker2.R
import com.example.easyratetracker2.adapters.util.NetworkObserver.Status
import com.example.easyratetracker2.adapters.util.NetworkObserver.StatusData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class NetworkObserverImpl @Inject constructor(): NetworkObserver {

    @ApplicationContext
    @Inject
    lateinit var context: Context
    private val errors: MutableList<Throwable> by lazy{ArrayList()}

    private val _status = MutableStateFlow(StatusData(Status.INIT, null))
    override val status = _status.asStateFlow()

    override fun setStatus(status: Int) {
        _status.value.let { statusData ->
            if (status != Status.ERROR
                && statusData.previousStatus == Status.ERROR
                && errors.size > 0
            ) errors.clear()
            _status.value = statusData.createFrom(status)
        }
    }

    override fun addError(e: Throwable) {
        errors.add(e)
        setStatus(Status.ERROR)
    }

    override val errorsDescription: String
        get() = StringBuilder(context.getString(R.string.error_introductory)).also {
            errors.forEach { e -> it.append(e) }
        }.toString()

}