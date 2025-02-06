package com.example.easyratetracker2.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.easyratetracker2.adapters.util.ItemCallback
import com.example.easyratetracker2.adapters.util.NetworkObserver
import com.example.easyratetracker2.data.models.ListElementModel
import com.example.easyratetracker2.data.models.ListErrorModel
import com.example.easyratetracker2.databinding.ListErrorElementBinding
import com.example.easyratetracker2.databinding.ListLoadElementBinding
import kotlin.math.max

abstract class StateDisplayAdapter<V : ListElementModel<*>, T : ViewDataBinding>(
    itemCallback: ItemCallback<V>,
    val bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> T,
    private inline val contentSetter: (model: V, binding: T) -> Unit,
    val errorProducer: (parent: ViewGroup, observer: NetworkObserver) -> RecyclerView.ViewHolder = this::defaultErrorProducer,
    loadProducer: ((parent: ViewGroup, observer: NetworkObserver) -> RecyclerView.ViewHolder)? = null
) : ModelAdapter<V>(itemCallback) {

    interface ViewHolderHandler<V : ListElementModel<*>, T : ViewDataBinding>{
        fun bindingInflater(a: LayoutInflater, b: ViewGroup, c: Boolean): T
        fun contentSetter(model: V, binding: T)
    }

    constructor(itemCallback: ItemCallback<V>, handler: ViewHolderHandler<V, T>) : this(itemCallback, handler::bindingInflater, handler::contentSetter)

    private lateinit var observer: NetworkObserver
    private var recyclerView: RecyclerView? = null

    override val vhProducer: (parent: ViewGroup) -> ModelViewHolder<V, T> =
        { parent ->
            ModelViewHolder(
                parent,
                bindingInflater,
                contentSetter
            )
        }

    val loadProducer: (parent: ViewGroup, observer: NetworkObserver) -> RecyclerView.ViewHolder = loadProducer ?: {p,_ -> this.defaultLoadProducer(p) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            ITEM -> super.onCreateViewHolder(parent, viewType)
            ERROR -> errorProducer(parent, observer)
            LOADING -> loadProducer(parent, observer)
            else -> throw IndexOutOfBoundsException()
        }
    }

    override fun getItemViewType(position: Int): Int {
        val status = observer.status
        if (status != NetworkObserver.Status.READY && position == itemCount - 1) {
            when (status) {
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
        observer.observeStatusData(owner, this::onNetworkStatusChange)
    }

    private fun onNetworkStatusChange(statusData: NetworkObserver.StatusData) {
        if (statusData.newStatus != statusData.previousStatus
            && statusData.previousStatus == NetworkObserver.Status.LOADING) notifyItemChanged(max(itemCount - 1, 0))
    }

    private fun defaultLoadProducer(parent: ViewGroup): ModelViewHolder<V, ListLoadElementBinding> {
        return ModelViewHolder(
            parent,
            ListLoadElementBinding::inflate,
        ) { model, binding -> binding.elementContainer.addView(
            parent.inflateFrom(bindingInflater).also {
                contentSetter(model, it)
            }.root
        ) }
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

    }
}