package com.example.easyratetracker2.adapters

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.easyratetracker2.adapters.util.ItemCallback
import com.example.easyratetracker2.adapters.util.NetworkObserver
import com.example.easyratetracker2.data.models.ListElementModel
import com.example.easyratetracker2.data.models.ListErrorModel
import com.example.easyratetracker2.databinding.ListErrorElementBinding
import com.example.easyratetracker2.databinding.ListLoadElementBinding

abstract class StateDisplayAdapter<V : ListElementModel<*>>(
    itemCallback: ItemCallback<V>,
    val errorProducer: (parent: ViewGroup, observer: NetworkObserver) -> RecyclerView.ViewHolder = this::defaultErrorProducer,
    val loadProducer: (parent: ViewGroup, observer: NetworkObserver) -> RecyclerView.ViewHolder = {p,_ -> defaultLoadProducer(p) }
) : ModelAdapter<V>(itemCallback) {

    private lateinit var observer: NetworkObserver
    private var recyclerView: RecyclerView? = null

    var hasDecorationItem = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            ITEM -> super.onCreateViewHolder(parent, viewType)
            ERROR -> errorProducer(parent, observer)
            LOADING -> loadProducer(parent, observer)
            else -> throw IndexOutOfBoundsException()
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (hasDecorationItems() && position == itemCount - 1) {
            when (observer.status) {
                NetworkObserver.Status.INIT, NetworkObserver.Status.LOADING -> return LOADING
                NetworkObserver.Status.ERROR -> return ERROR
            }
        }
        return ITEM
    }

    fun setUpObserver(newObserver: NetworkObserver, owner: LifecycleOwner, recyclerView: RecyclerView){
        this.recyclerView = recyclerView
        if(::observer.isInitialized) return
        observer = newObserver
        observer.observeStatus(owner, this::onNetworkStatusChange)
    }

    private fun onNetworkStatusChange(newStatus: Int) {
        observer.previousStatus?.let {
            adapterStatusNotify(newStatus, it)
        }
    }

    private fun adapterStatusNotify(newStatus: Int, previousStatus: Int) {
        hasDecorationItem = hasDecorationItems(newStatus)
        val hasDecorationItems = hasDecorationItem
        val hadDecorationItems = hasDecorationItems(previousStatus)
        val itemCount: Int = itemCount
        if (hadDecorationItems != hasDecorationItems) {
            if (hadDecorationItems) {
                recyclerView?.post{notifyItemRemoved(itemCount)}
            } else {
                recyclerView?.post{notifyItemInserted(itemCount)}
            }
        } else if (hasDecorationItems && newStatus != previousStatus) {
            notifyItemChanged(if (itemCount == 0) 0 else itemCount - 1)
        }
    }

    private fun hasDecorationItems(): Boolean {
        return hasDecorationItems(observer.status)
    }

    private fun hasDecorationItems(networkStatus: Int): Boolean {
        return networkStatus != NetworkObserver.Status.READY
    }

    companion object {
        const val ITEM = 0
        const val LOADING = 1
        const val ERROR = 2

        private fun defaultErrorProducer(parent: ViewGroup, observer : NetworkObserver): ModelProducerVH<ListErrorModel, ListErrorElementBinding> {
            return ModelProducerVH(
                parent,
                ListErrorElementBinding::inflate,
                { model, binding -> binding.model = model },
                {ListErrorModel(observer.errorsDescription)}
            )
        }

        private fun defaultLoadProducer(parent: ViewGroup): BindingViewHolder<ListLoadElementBinding> {
            return BindingViewHolder(parent, ListLoadElementBinding::inflate)
        }
    }
}