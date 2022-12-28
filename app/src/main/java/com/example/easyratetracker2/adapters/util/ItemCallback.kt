package com.example.easyratetracker2.adapters.util

import androidx.recyclerview.widget.DiffUtil
import com.example.easyratetracker2.data.models.Model
import javax.inject.Inject

class ItemCallback<T : Model<*>> @Inject constructor() : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }
}