package com.jensencelestial.ghiblibrary.android.ui.film

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.RoundedCornersTransformation
import com.jensencelestial.ghiblibrary.android.R
import com.jensencelestial.ghiblibrary.android.data.model.Film
import com.jensencelestial.ghiblibrary.android.databinding.FragmentFilmBinding
import com.jensencelestial.ghiblibrary.android.ui.UIState
import com.jensencelestial.ghiblibrary.android.viewmodel.FilmViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilmFragment : Fragment() {
    private val filmViewModel: FilmViewModel by viewModels()

    private var _binding: FragmentFilmBinding? = null
    private val binding get() = _binding!!

    private val args: FilmFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFilmBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewEvents()
        initObservers()

        filmViewModel.getFilm(args.filmId)
    }

    private fun initViewEvents() {
        binding.swltFilm.setOnRefreshListener {
            filmViewModel.getFilm(args.filmId)
        }
    }

    private fun initObservers() {
        filmViewModel.film.observe(this.viewLifecycleOwner, FilmObserver())
    }

    private fun setFilmContent(film: Film) {
        binding.lytFilm.tvFilmTitle.text = film.title

        binding.lytFilm.lblYearReleased.tvYearReleased.text = film.releaseDate

        binding.lytFilm.tvDirector.text = film.director

        if (film.producer != film.director) {
            binding.lytFilm.tvProducer.text = film.producer
        } else {
            binding.lytFilm.tvProducer.visibility = View.GONE
        }

        binding.lytFilm.tvRTScore.text = film.rtScore

        binding.lytFilm.ivThumbnail.load(film.imageUrl) {
            placeholder(R.drawable.shape_image_fallback)
            fallback(R.drawable.shape_image_fallback)
            error(R.drawable.shape_image_fallback)
            crossfade(true)
            transformations(RoundedCornersTransformation(4f))
        }

        binding.lytFilm.tvDescription.text = film.description
    }

    private inner class FilmObserver : Observer<UIState<Film>> {
        override fun onChanged(film: UIState<Film>) {
            when (film) {
                is UIState.Loading -> {
                    if (!binding.swltFilm.isRefreshing) {
                        binding.swltFilm.isRefreshing = true
                    }
                }
                is UIState.Success -> {
                    binding.swltFilm.isRefreshing = false

                    setFilmContent(film.value)
                }
                is UIState.Failure -> {
                    binding.swltFilm.isRefreshing = false
                }
            }
        }
    }
}