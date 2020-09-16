package com.jensencelestial.ghiblibrary.android.ui.vehicle

import android.view.LayoutInflater
import android.view.ViewGroup
import coil.load
import coil.transform.RoundedCornersTransformation
import com.jensencelestial.ghiblibrary.android.R
import com.jensencelestial.ghiblibrary.android.data.model.Vehicle
import com.jensencelestial.ghiblibrary.android.databinding.AdapterVehicleListItemBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem

class VehicleItem(val vehicle: Vehicle) : AbstractBindingItem<AdapterVehicleListItemBinding>() {

    override val type: Int
        get() = R.id.fastadapter_film_item_id

    override fun bindView(binding: AdapterVehicleListItemBinding, payloads: List<Any>) {
        binding.ivThumbnail.load(vehicle.imageUrl) {
            placeholder(R.drawable.shape_image_fallback)
            fallback(R.drawable.shape_image_fallback)
            error(R.drawable.shape_image_fallback)
            crossfade(true)
            transformations(RoundedCornersTransformation(4f))
        }

        binding.tvVehicleName.text = vehicle.name
    }

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): AdapterVehicleListItemBinding {
        return AdapterVehicleListItemBinding.inflate(inflater, parent, false)
    }
}