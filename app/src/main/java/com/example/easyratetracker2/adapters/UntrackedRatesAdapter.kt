package com.example.easyratetracker2.adapters

import android.view.ViewGroup
import androidx.navigation.NavController
import com.example.easyratetracker2.adapters.util.ItemCallback
import com.example.easyratetracker2.data.models.OuterDetailsModel
import com.example.easyratetracker2.data.models.UntrackedRatesElementModel
import com.example.easyratetracker2.databinding.UntrackedRatesElementBinding
import com.example.easyratetracker2.ui.ContentMainFragmentDirections
import javax.inject.Inject

open class UntrackedRatesAdapter @Inject constructor(itemCallback: ItemCallback<UntrackedRatesElementModel>) :
    StateDisplayAdapter<UntrackedRatesElementModel>(itemCallback) {

    var navController: NavController? = null

    override val vhProducer: (parent: ViewGroup) -> ModelViewHolder<UntrackedRatesElementModel, UntrackedRatesElementBinding> =
        { parent ->
            ModelViewHolder(
                parent,
                UntrackedRatesElementBinding::inflate,
                this::setUpData
            )
        }

    private fun setUpData(
        model: UntrackedRatesElementModel,
        binding: UntrackedRatesElementBinding
    ) {
        binding.model = model
        val navControllerLocal = navController

        navControllerLocal?.let {
            binding.root.setOnClickListener {
                navControllerLocal.navigate(
                    ContentMainFragmentDirections.actionContentMainToRateDetails(
                        OuterDetailsModel(model)))
            }
        }
    }
}