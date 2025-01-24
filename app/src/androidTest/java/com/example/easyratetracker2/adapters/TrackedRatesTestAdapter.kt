package com.example.easyratetracker2.adapters

import androidx.recyclerview.widget.RecyclerView
import com.example.easyratetracker2.adapters.util.ItemCallback
import com.example.easyratetracker2.adapters.util.LoadViewHolderObserver
import com.example.easyratetracker2.data.models.RatesElementModel

import javax.inject.Inject

class TrackedRatesTestAdapter @Inject constructor(itemCallback: ItemCallback<RatesElementModel>)
    : TrackedRatesAdapter(itemCallback) {

    val obs: LoadViewHolderObserver = LoadViewHolderObserver()

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        obs.onElementAdd(holder)
        super.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        obs.onElementDelete(holder)
        super.onViewDetachedFromWindow(holder)
    }
}