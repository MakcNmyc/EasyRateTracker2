package com.example.easyratetracker2.adapters.util

import androidx.recyclerview.widget.RecyclerView
import com.example.easyratetracker2.adapters.StateDisplayAdapter

class LoadViewHolderObserver {

    private val loadElements = ArrayList<RecyclerView.ViewHolder>()
    private val onElementDisappearsSubs = ArrayList<() -> Unit> ()

    fun onElementAdd(vh: RecyclerView.ViewHolder) {
        val containsVh = loadElements.contains(vh)
        val isLoadingElement = isLoadVh(vh)
        if(isLoadingElement) {
            if (containsVh) removeElement(vh) else loadElements.add(vh)
        }
    }

    fun onElementDelete(vh: RecyclerView.ViewHolder) {
        if (isLoadVh(vh)) {
            removeElement(vh)
        }
    }

    private fun isLoadVh(vh: RecyclerView.ViewHolder): Boolean {
        return vh.itemViewType == StateDisplayAdapter.LOADING
    }

    private fun removeElement(vh: RecyclerView.ViewHolder) {
        loadElements.remove(vh)
        if (!haveActiveLoadElements()) onElementDisappear()
    }

    fun haveActiveLoadElements(): Boolean = loadElements.isNotEmpty()

    private fun onElementDisappear() {
        onElementDisappearsSubs.forEach { it() }
    }

    fun onElementDisappear(action: () -> Unit) {
        onElementDisappearsSubs.add(action)
    }
}