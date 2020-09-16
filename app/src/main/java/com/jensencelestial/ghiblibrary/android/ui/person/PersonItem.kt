package com.jensencelestial.ghiblibrary.android.ui.person

import android.view.LayoutInflater
import android.view.ViewGroup
import coil.load
import coil.transform.RoundedCornersTransformation
import com.jensencelestial.ghiblibrary.android.R
import com.jensencelestial.ghiblibrary.android.data.model.Person
import com.jensencelestial.ghiblibrary.android.databinding.AdapterPersonListItemBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem

class PersonItem(val person: Person) : AbstractBindingItem<AdapterPersonListItemBinding>() {

    override val type: Int
        get() = R.id.fastadapter_person_item_id

    override fun bindView(binding: AdapterPersonListItemBinding, payloads: List<Any>) {
        binding.ivThumbnail.load(person.imageUrl) {
            placeholder(R.drawable.shape_image_fallback)
            fallback(R.drawable.shape_image_fallback)
            error(R.drawable.shape_image_fallback)
            crossfade(true)
            transformations(RoundedCornersTransformation(4f))
        }

        binding.tvPersonName.text = person.name
    }

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): AdapterPersonListItemBinding {
        return AdapterPersonListItemBinding.inflate(inflater, parent, false)
    }
}