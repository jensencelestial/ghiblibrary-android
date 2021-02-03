package com.jensencelestial.ghiblibrary.android.ui.location

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
import com.jensencelestial.ghiblibrary.android.data.model.Location
import com.jensencelestial.ghiblibrary.android.databinding.FragmentLocationBinding
import com.jensencelestial.ghiblibrary.android.ui.UIState
import com.jensencelestial.ghiblibrary.android.viewmodel.LocationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationFragment : Fragment() {
    private val locationViewModel: LocationViewModel by viewModels()

    private lateinit var binding: FragmentLocationBinding

    private val args: LocationFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLocationBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewEvents()
        initObservers()

        locationViewModel.getLocation(args.locationId)
    }

    private fun initViewEvents() {
        binding.swltLocation.setOnRefreshListener {
            locationViewModel.getLocation(args.locationId)
        }
    }

    private fun initObservers() {
        locationViewModel.location.observe(this.viewLifecycleOwner, LocationObserver())
    }

    private fun setLocationContent(location: Location) {
        binding.lytPerson.tvName.text = location.name

        binding.lytPerson.tvClimate.text = location.climate

        binding.lytPerson.tvTerrain.text = location.terrain

        binding.lytPerson.tvSurfaceWater.text = location.surfaceWater

        binding.lytPerson.ivThumbnail.load(location.imageUrl) {
            placeholder(R.drawable.shape_image_fallback)
            fallback(R.drawable.shape_image_fallback)
            error(R.drawable.shape_image_fallback)
            crossfade(true)
            transformations(RoundedCornersTransformation(4f))
        }
    }

    private inner class LocationObserver : Observer<UIState<Location>> {
        override fun onChanged(location: UIState<Location>) {
            when (location) {
                is UIState.Loading -> {
                    if (!binding.swltLocation.isRefreshing) {
                        binding.swltLocation.isRefreshing = true
                    }
                }
                is UIState.Success -> {
                    binding.swltLocation.isRefreshing = false

                    setLocationContent(location.value)
                }
                is UIState.Failure -> {
                    binding.swltLocation.isRefreshing = false
                }
            }
        }
    }
}