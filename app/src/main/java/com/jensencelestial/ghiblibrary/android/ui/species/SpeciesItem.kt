package com.jensencelestial.ghiblibrary.android.ui.species

import android.view.LayoutInflater
import android.view.ViewGroup
import coil.load
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
            placeholder(R.drawable.shape_image_fallback)
            fallback(R.drawable.shape_image_fallback)
            error(R.drawable.shape_image_fallback)
            crossfade(true)
            transformations(RoundedCornersTransformation(4f))
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