package com.example.easyratetracker2.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.easyratetracker2.adapters.util.ItemCallback
import com.example.easyratetracker2.data.models.ListElementModel

abstract class ModelAdapter<V : ListElementModel<*>>(
    itemCallback: ItemCallback<V>,
) : PagingDataAdapter<V, RecyclerView.ViewHolder>(itemCallback) {

    abstract val vhProducer: (parent: ViewGroup) -> ModelViewHolder<V, *>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return vhProducer(parent)
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
            bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> T
        ) : this(parent, parent.inflateFrom(bindingInflater))
    }

    open class ModelViewHolder<in V : ListElementModel<*>, T : ViewDataBinding>(
        parent: ViewGroup,
        bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> T,
        private inline val contentSetter: (model: V, binding: T) -> Unit
    ) : BindingViewHolder<T>(parent, bindingInflater) {
        fun setModel(model: V) {
            contentSetter(model, binding)
            binding.executePendingBindings()
        }
    }

    class ModelProducerVH<in V : ListElementModel<*>, T : ViewDataBinding> (
        parent: ViewGroup,
        bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> T,
        contentSetter: (model: V, binding: T) -> Unit,
        private inline val modelProducer: (() -> V)
    ) : ModelViewHolder<V, T>(parent, bindingInflater, contentSetter) {
        fun setModel() = setModel(modelProducer())
    }

    companion object{
        internal fun <T: ViewDataBinding> ViewGroup.inflateFrom(bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> T) =
            bindingInflater(LayoutInflater.from(this.context), this, false)
    }

}