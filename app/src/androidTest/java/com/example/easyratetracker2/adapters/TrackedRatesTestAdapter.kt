package com.example.easyratetracker2.adapters

import androidx.recyclerview.widget.RecyclerView
import com.example.easyratetracker2.adapters.util.ItemCallback
import com.example.easyratetracker2.adapters.util.LoadElementObserver
import com.example.easyratetracker2.data.models.TrackedRatesElementModel
import javax.inject.Inject

class TrackedRatesTestAdapter @Inject constructor(itemCallback: ItemCallback<TrackedRatesElementModel>)
    : TrackedRatesAdapter(itemCallback) {

    val obs: LoadElementObserver = LoadElementObserver()

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        obs.onElementAdd(holder)
        super.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        obs.onElementDelete(holder)
        super.onViewDetachedFromWindow(holder)
    }
}