//package com.example.easyratetracker2.ui
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.databinding.ViewDataBinding
//import androidx.fragment.app.Fragment
//import com.example.easyratetracker2.viewmodels.BaseViewModel
//
//abstract class BaseFragment: Fragment() {
//
//    abstract var binding: ViewDataBinding
//
//    abstract fun setContent()
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//
//        return super.onCreateView(inflater, container, savedInstanceState)
//    }
//
//    fun <T: ViewDataBinding> setUpBinding(inflateBinding: (LayoutInflater, ViewGroup, Boolean) -> T){}
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        binding?.lifecycleOwner = this
//        setContent()
//    }
//}