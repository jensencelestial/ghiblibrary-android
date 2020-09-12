package com.jensencelestial.ghiblibrary.android.ui.species

import android.view.LayoutInflater
import android.view.ViewGroup
import coil.api.load
import coil.transform.RoundedCornersTransformation
import com.jensencelestial.ghiblibrary.android.R
import com.jensencelestial.ghiblibrary.android.data.model.Species
import com.jensencelestial.ghiblibrary.android.databinding.AdapterSpeciesListItemBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem

class SpeciesItem(val species: Species) : AbstractBindingItem<AdapterSpeciesListItemBinding>() {

    override val type: Int
        get() = R.id.fastadapter_species_item_id

    override fun bindView(binding: AdapterSpeciesListItemBinding, payloads: List<Any>) {
        binding.ivThumbnail.load(species.imageUrl) {
            crossfade(true)
            transformations(RoundedCornersTransformation())
        }

        binding.tvSpeciesName.text = species.name
    }

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): AdapterSpeciesListItemBinding {
        return AdapterSpeciesListItemBinding.inflate(inflater, parent, false)
    }
}