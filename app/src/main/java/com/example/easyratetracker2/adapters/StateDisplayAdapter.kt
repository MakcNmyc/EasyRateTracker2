package com.example.easyratetracker2.adapters

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.easyratetracker2.adapters.util.ItemCallback
import com.example.easyratetracker2.adapters.util.NetworkObserver
import com.example.easyratetracker2.data.models.ListErrorModel
import com.example.easyratetracker2.data.models.Model
import com.example.easyratetracker2.databinding.ListErrorElementBinding
import com.example.easyratetracker2.databinding.ListLoadElementBinding

import javax.inject.Inject

class StateDisplayAdapter<V : Model<*>> @Inject constructor(
    itemCallback: ItemCallback<V>,
    vhProducer: (parent: ViewGroup) -> ModelViewHolder<V, *>,
    val errorProducer: (parent: ViewGroup, observer: NetworkObserver) -> RecyclerView.ViewHolder = this::defaultErrorProducer,
    val loadProducer: (parent: ViewGroup, observer: NetworkObserver) -> RecyclerView.ViewHolder = this::defaultLoadProducer
) : ModelAdapter<V>(itemCallback, vhProducer) {

    @Inject lateinit var observer: NetworkObserver

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            ERROR -> errorProducer(parent, observer)
            LOADING -> loadProducer(parent, observer)
            else -> super.onCreateViewHolder(parent, viewType)
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

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasDecorationItems()) 1 else 0
    }

    fun setUpObserver(newObserver: NetworkObserver, owner: LifecycleOwner){
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
        val hasDecorationItems = hasDecorationItems()
        val hadDecorationItems = hasDecorationItems(previousStatus)
        val itemCount: Int = itemCount
        if (hadDecorationItems != hasDecorationItems) {
            if (hadDecorationItems) {
                notifyItemRemoved(itemCount)
            } else {
                notifyItemInserted(itemCount)
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

        private fun defaultLoadProducer(parent: ViewGroup, observer : NetworkObserver): BindingViewHolder<ListLoadElementBinding> {
            return BindingViewHolder(parent, ListLoadElementBinding::inflate)
        }
    }
}