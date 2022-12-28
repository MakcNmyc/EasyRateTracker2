//package com.example.easyratetracker2.ui.lists.elements
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.databinding.ViewDataBinding
//import androidx.lifecycle.ViewTreeLifecycleOwner
//
//abstract class BindingElement<T : ViewDataBinding?>(
//    parent: ViewGroup,
//    inflateBinding: (LayoutInflater, ViewGroup, Boolean) -> T
//) {
//    var binding: T = inflateBinding(LayoutInflater.from(parent.context), parent, false)!!.apply {
//        lifecycleOwner = ViewTreeLifecycleOwner.get(parent)
//    }
//}