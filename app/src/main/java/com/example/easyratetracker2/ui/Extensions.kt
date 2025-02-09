package com.example.easyratetracker2.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.easyratetracker2.adapters.StateDisplayAdapter
import com.example.easyratetracker2.adapters.util.NetworkObserver
import com.example.easyratetracker2.adapters.util.NetworkObserver.Status
import com.example.easyratetracker2.data.models.ListElementModel
import com.example.easyratetracker2.databinding.StateDisplayListBinding
import com.example.easyratetracker2.di.AppEntryPoint
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

fun Context.createAppEntryPoint() = EntryPointAccessors.fromApplication(this, AppEntryPoint::class.java)

inline fun <T : ViewDataBinding> Fragment.createBinding(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    inflateBinding: (LayoutInflater, ViewGroup?, Boolean) -> T
): T = inflateBinding(inflater, parent, false).apply {
    lifecycleOwner = this@createBinding
}

inline fun <T: ListElementModel<*>> Fragment.setUpStateDisplayList(
    binding: StateDisplayListBinding,
    dataProducer: Flow<Flow<PagingData<T>>?>,
    pagedAdapter: StateDisplayAdapter<T, *>,
    observer: NetworkObserver,
    swipeRefresh: SwipeRefreshLayout,
    crossinline pagedListRefresher: () -> Unit,
) {
    setUpBaseList(binding.list, dataProducer, pagedAdapter)
    this.view
    pagedAdapter.setUpObserver(observer, viewLifecycleOwner, binding.list)
    swipeRefresh.setOnRefreshListener {
        //skip widget animation
        swipeRefresh.isRefreshing = false
        pagedListRefresher()
        this.setUpPagedList(dataProducer, pagedAdapter)
    }

    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED){
            binding.let {
                observer.status.collectLatest { statusData ->
                    when(statusData.currentStatus){
                        Status.LOADING -> {
                            if(statusData.previousStatus != Status.READY) it.loadingBar.visibility = View.VISIBLE
                        }
                        Status.ERROR -> {
                            it.loadingBar.visibility = View.GONE
                            it.list.visibility = View.GONE
                            it.errorText.visibility = View.VISIBLE
                            it.errorText.text = observer.errorsDescription
                        }
                        Status.READY -> it.loadingBar.visibility = View.GONE
                    }
                }
            }
        }
    }
}

fun <T : Any> Fragment.setUpBaseList(
    recyclerView: RecyclerView,
    dataProducer: Flow<Flow<PagingData<T>>?>,
    adapter: PagingDataAdapter<T, RecyclerView.ViewHolder>,
    layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this.context)
) {
    this.setUpPagedList(dataProducer, adapter)
    recyclerView.apply {
        setHasFixedSize(true)
        this.layoutManager = layoutManager
        this.adapter = adapter
    }
}

fun <T : Any> Fragment.setUpPagedList(
    dataProducer: Flow<Flow<PagingData<T>>?>,
    adapter: PagingDataAdapter<T, RecyclerView.ViewHolder>
){
    this.viewLifecycleOwner.lifecycleScope.launch {
        dataProducer.collectLatest { dataFlow ->
            dataFlow?.collectLatest { data ->
                adapter.submitData(data)
            }
        }
    }
}


