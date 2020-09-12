package com.jensencelestial.ghiblibrary.android.ui.location

import android.view.LayoutInflater
import android.view.ViewGroup
import coil.api.load
import coil.transform.RoundedCornersTransformation
import com.jensencelestial.ghiblibrary.android.R
import com.jensencelestial.ghiblibrary.android.data.model.Location
import com.jensencelestial.ghiblibrary.android.databinding.AdapterLocationListItemBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem

class LocationItem(val location: Location) : AbstractBindingItem<AdapterLocationListItemBinding>() {

    override val type: Int
        get() = R.id.fastadapter_person_item_id

    override fun bindView(binding: AdapterLocationListItemBinding, payloads: List<Any>) {
        binding.ivThumbnail.load(location.imageUrl) {
            crossfade(true)
            transformations(RoundedCornersTransformation())
        }

        binding.tvLocationName.text = location.name
    }

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): AdapterLocationListItemBinding {
        return AdapterLocationListItemBinding.inflate(inflater, parent, false)
    }
}