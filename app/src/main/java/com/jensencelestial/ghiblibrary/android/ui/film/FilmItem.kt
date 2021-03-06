package com.jensencelestial.ghiblibrary.android.ui.film

import android.view.LayoutInflater
import android.view.ViewGroup
import coil.load
import coil.transform.RoundedCornersTransformation
import com.jensencelestial.ghiblibrary.android.R
import com.jensencelestial.ghiblibrary.android.data.model.Film
import com.jensencelestial.ghiblibrary.android.databinding.AdapterFilmListItemBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem

class FilmItem(val film: Film) : AbstractBindingItem<AdapterFilmListItemBinding>() {

    override val type: Int
        get() = R.id.fastadapter_film_item_id

    override fun bindView(binding: AdapterFilmListItemBinding, payloads: List<Any>) {
        binding.ivThumbnail.load(film.imageUrl) {
            placeholder(R.drawable.shape_image_fallback)
            fallback(R.drawable.shape_image_fallback)
            error(R.drawable.shape_image_fallback)
            crossfade(true)
            transformations(RoundedCornersTransformation(4f))
        }

        binding.tvTitle.text = film.title

        binding.lblYearReleased.tvYearReleased.text = film.releaseDate
    }

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): AdapterFilmListItemBinding {
        return AdapterFilmListItemBinding.inflate(inflater, parent, false)
    }
}