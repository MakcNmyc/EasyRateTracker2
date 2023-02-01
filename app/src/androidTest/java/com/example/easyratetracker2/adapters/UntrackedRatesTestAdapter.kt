package com.example.easyratetracker2.adapters

import androidx.recyclerview.widget.RecyclerView
import com.example.easyratetracker2.adapters.util.ItemCallback
import com.example.easyratetracker2.adapters.util.LoadElementObserver
import com.example.easyratetracker2.data.models.UntrackedRatesElementModel
import javax.inject.Inject

class UntrackedRatesTestAdapter @Inject constructor(itemCallback: ItemCallback<UntrackedRatesElementModel>)
    : UntrackedRatesAdapter(itemCallback) {

    private val obs: LoadElementObserver = LoadElementObserver()

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        obs.onElementAdd(holder)
        super.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        obs.onElementDelete(holder)
        super.onViewDetachedFromWindow(holder)
    }

    fun getObs(): LoadElementObserver {
        return obs
    }
}