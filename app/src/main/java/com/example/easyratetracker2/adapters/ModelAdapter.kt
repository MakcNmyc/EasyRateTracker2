package com.example.easyratetracker2.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.easyratetracker2.adapters.util.ItemCallback
import com.example.easyratetracker2.data.models.Model

open class ModelAdapter<V : Model<*>>(
    itemCallback: ItemCallback<V>,
    val modelVHProducer: (parent: ViewGroup) -> ModelViewHolder<V, *>
) : PagedListAdapter<V, RecyclerView.ViewHolder>(itemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return modelVHProducer(parent)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (viewHolder) {
            is ModelProducerVH<*, *> -> viewHolder.setModel()
            is ModelViewHolder<*, *> -> getItem(position)?.let {
                (viewHolder as ModelViewHolder<V, *>).setModel(it)
            }
        }
    }

    open class BindingViewHolder<T : ViewDataBinding>(
        parent: ViewGroup,
        val binding: T
    ) : RecyclerView.ViewHolder(binding.apply {
        lifecycleOwner = ViewTreeLifecycleOwner.get(parent)
    }.root) {
        constructor(
            parent: ViewGroup,
            inflateBinding: (LayoutInflater, ViewGroup, Boolean) -> T
        ) : this(parent, inflateBinding(LayoutInflater.from(parent.context), parent, false))
    }

    open class ModelViewHolder<in V : Model<*>, T : ViewDataBinding>(
        parent: ViewGroup,
        inflateBinding: (LayoutInflater, ViewGroup, Boolean) -> T,
        private val contentSetter: (model: V, binding: T) -> Unit
    ) : BindingViewHolder<T>(parent, inflateBinding) {

        fun setModel(model: V) {
            contentSetter(model, binding)
            binding.executePendingBindings()
        }
    }

    class ModelProducerVH<in V : Model<*>, T : ViewDataBinding>(
        parent: ViewGroup,
        inflateBinding: (LayoutInflater, ViewGroup, Boolean) -> T,
        contentSetter: (model: V, binding: T) -> Unit,
        private val modelProducer: (() -> V)
    ) : ModelViewHolder<V, T>(parent, inflateBinding, contentSetter) {
        fun setModel() = setModel(modelProducer())
    }
}