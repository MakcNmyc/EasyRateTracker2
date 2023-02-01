package com.example.easyratetracker2.adapters.util

import androidx.recyclerview.widget.RecyclerView
import com.example.easyratetracker2.adapters.StateDisplayAdapter
import java.util.function.Consumer

class LoadElementObserver {
    private val loadElements = ArrayList<RecyclerView.ViewHolder>()
    private val subscribers = ArrayList<Runnable>()

    fun onElementAdd(vh: RecyclerView.ViewHolder) {
        val containsVh = loadElements.contains(vh)
        val isLoadingElement = isLoadVh(vh)
        if (!containsVh && isLoadingElement) {
            loadElements.add(vh)
        } else if (containsVh && !isLoadingElement) {
            clearLoadElement(vh)
        }
    }

    fun onElementDelete(vh: RecyclerView.ViewHolder) {
        if (isLoadVh(vh)) {
            clearLoadElement(vh)
        }
    }

    private fun isLoadVh(vh: RecyclerView.ViewHolder): Boolean {
        return vh.itemViewType == StateDisplayAdapter.LOADING
    }

    private fun clearLoadElement(vh: RecyclerView.ViewHolder) {
        loadElements.remove(vh)
        if (notHaveLoadElement()) subsNotify()
    }

    fun notHaveLoadElement(): Boolean {
        return loadElements.isEmpty()
    }

    private fun subsNotify() {
        subscribers.forEach(Consumer { obj: Runnable -> obj.run() })
    }

    fun subscribe(action: Runnable) {
        subscribers.add(action)
    }
}