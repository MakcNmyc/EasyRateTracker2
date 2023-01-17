package com.example.easyratetracker2.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.example.easyratetracker2.adapters.StateDisplayAdapter
import com.example.easyratetracker2.adapters.util.NetworkObserver
import com.example.easyratetracker2.data.models.ListElementModel

inline fun <T : ViewDataBinding> Fragment.createBinding(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    inflateBinding: (LayoutInflater, ViewGroup?, Boolean) -> T
): T = inflateBinding(inflater, parent, false).apply {
    lifecycleOwner = this@createBinding
}

inline fun <T> Fragment.setUpBaseList(
    recyclerView: RecyclerView,
    crossinline pagedListProducer: ()-> LiveData<PagedList<T>>,
    pagedAdapter: PagedListAdapter<T, RecyclerView.ViewHolder>
) {
    this.setUpPagedList( pagedListProducer, pagedAdapter)
    recyclerView.apply {
        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this.context)
        adapter = pagedAdapter
    }
}

inline fun <T> Fragment.setUpPagedList(
    pagedListProducer: ()-> LiveData<PagedList<T>>,
    pagedAdapter: PagedListAdapter<T, RecyclerView.ViewHolder>
){
    pagedListProducer().observe(this) { newPagedList -> pagedAdapter.submitList(newPagedList) }
}

//fun <T> ViewModel.initPagedList(factory: DataSource.Factory<*, T>): LiveData<PagedList<T>> {
//    return PagedList.Config.Builder()
//        .setEnablePlaceholders(false)
//        .setPageSize(Settings.PAGE_SIZE)
//        .build()
//        .let { config ->
//            LivePagedListBuilder(factory, config).build()
//        }
//}

inline fun <T: ListElementModel<*>> Fragment.setUpNetworkList(
    recyclerView: RecyclerView,
    crossinline pagedListProducer: () -> LiveData<PagedList<T>>,
    pagedAdapter: StateDisplayAdapter<T>,
    observer: NetworkObserver,
    swipeRefresh: SwipeRefreshLayout
) {
    setUpBaseList(recyclerView, pagedListProducer, pagedAdapter)
    pagedAdapter.setUpObserver(observer, this)
    swipeRefresh.setOnRefreshListener(OnRefreshListener {
        //skip widget animation
        swipeRefresh.setRefreshing(false)
        if (observer.status != NetworkObserver.Status.LOADING)
            pagedAdapter.submitList(null)
        this.setUpPagedList(pagedListProducer, pagedAdapter)
    })
}
