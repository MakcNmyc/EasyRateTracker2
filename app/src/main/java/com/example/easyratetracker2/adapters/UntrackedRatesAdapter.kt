package com.example.easyratetracker2.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import com.example.easyratetracker2.adapters.util.ItemCallback
import com.example.easyratetracker2.data.models.OuterDetailsModel
import com.example.easyratetracker2.data.models.RatesElementModel
import com.example.easyratetracker2.databinding.RatesElementBinding
import com.example.easyratetracker2.ui.ContentMainFragmentDirections
import javax.inject.Inject

open class UntrackedRatesAdapter(
    itemCallback: ItemCallback<RatesElementModel>,
    val viewHolderHandler: UntrackedViewHolderHandler
) : StateDisplayAdapter<RatesElementModel, RatesElementBinding>(itemCallback, viewHolderHandler) {

     @Inject constructor(itemCallback: ItemCallback<RatesElementModel>) : this(itemCallback, UntrackedViewHolderHandler())

    var navController by viewHolderHandler::navController

        class UntrackedViewHolderHandler : ViewHolderHandler<RatesElementModel, RatesElementBinding> {

            var navController: NavController? = null

            override fun bindingInflater(
                a: LayoutInflater,
                b: ViewGroup,
                c: Boolean
            ): RatesElementBinding = RatesElementBinding.inflate(a,b,c)

            override fun contentSetter(model: RatesElementModel, binding: RatesElementBinding) {
                binding.model = model

                navController?.let { nc ->
                    binding.root.setOnClickListener {
                        nc.navigate(
                            ContentMainFragmentDirections.actionContentMainToRateDetails(
                                OuterDetailsModel(model)))
                    }
                }
            }

        }


}