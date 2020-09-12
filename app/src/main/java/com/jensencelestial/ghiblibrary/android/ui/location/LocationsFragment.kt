package com.jensencelestial.ghiblibrary.android.ui.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jensencelestial.ghiblibrary.android.data.model.Location
import com.jensencelestial.ghiblibrary.android.databinding.FragmentLocationsBinding
import com.jensencelestial.ghiblibrary.android.ui.UIState
import com.jensencelestial.ghiblibrary.android.viewmodel.LocationsViewModel
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.IAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationsFragment : Fragment() {
    private val locationsViewModel: LocationsViewModel by viewModels()

    private var _binding: FragmentLocationsBinding? = null
    private val binding get() = _binding!!

    private lateinit var fastAdapter: FastAdapter<LocationItem>
    private lateinit var locationsAdapter: ItemAdapter<LocationItem>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLocationsBinding.inflate(inflater, container, false)
        val view = binding.root

        locationsAdapter = ItemAdapter()
        fastAdapter = FastAdapter.with(listOf(locationsAdapter))

        binding.rvLocations.apply {
            layoutManager = LinearLayoutManager(view.context)
            adapter = fastAdapter
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewEvents()
        initObservers()
    }

    private fun initViewEvents() {
        binding.swltLocations.setOnRefreshListener {
            locationsViewModel.getLocations()
        }

        fastAdapter.onClickListener =
            { view: View?, iAdapter: IAdapter<LocationItem>, locationItem: LocationItem, i: Int ->
                findNavController().navigate(
                    LocationsFragmentDirections.actionLocationsFragmentToLocationFragment(
                        locationItem.location.id
                    )
                )
                false
            }
    }

    private fun initObservers() {
        locationsViewModel.locations.observe(this.viewLifecycleOwner, LocationsObserver())
    }

    private inner class LocationsObserver : Observer<UIState<List<Location>>> {
        override fun onChanged(locations: UIState<List<Location>>) {
            when (locations) {
                is UIState.Loading -> {
                    if (!binding.swltLocations.isRefreshing) {
                        binding.swltLocations.isRefreshing = true
                    }
                }
                is UIState.Success -> {
                    binding.swltLocations.isRefreshing = false

                    locationsAdapter.clear()
                    locationsAdapter.add(locations.value.map { LocationItem(it) })
                }
                is UIState.Failure -> {
                    binding.swltLocations.isRefreshing = false
                }
            }
        }
    }
}