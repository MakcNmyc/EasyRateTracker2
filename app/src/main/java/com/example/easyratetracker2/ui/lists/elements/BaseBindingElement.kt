//package com.example.easyratetracker2.ui.lists.elements
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.databinding.ViewDataBinding
//import com.example.easyratetracker2.adapters.ModelAdapter
//import com.example.easyratetracker2.data.models.Identifiable
//import com.example.easyratetracker2.inflateChildBinding
//
//class ModelViewHolder<Model : Identifiable<*>?, Binding : ViewDataBinding> (
//    parent: ViewGroup,
//    inflateBinding: (LayoutInflater, ViewGroup, Boolean) -> Binding,
//    val contentSetter: (model: Model, binding: Binding) -> Unit,
//    private val modelSupplier: (() -> Model)? = null
//): ModelAdapter.BindingViewHolder(element.binding) {
//
//
//    val binding: Binding = parent.inflateChildBinding(inflateBinding)
//
//    fun setData(model: Model) {
//        contentSetter(model, binding)
//        binding.executePendingBindings()
//    }
//
//    fun setData(){if(modelSupplier != null) setData(modelSupplier.invoke())}
//
//    fun isModelNeeded(): Boolean = modelSupplier == null
//}
//
//
