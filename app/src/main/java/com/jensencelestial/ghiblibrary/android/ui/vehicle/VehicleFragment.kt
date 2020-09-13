package com.jensencelestial.ghiblibrary.android.ui.vehicle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import coil.api.load
import com.jensencelestial.ghiblibrary.android.R
import com.jensencelestial.ghiblibrary.android.data.model.Vehicle
import com.jensencelestial.ghiblibrary.android.databinding.FragmentVehicleBinding
import com.jensencelestial.ghiblibrary.android.ui.UIState
import com.jensencelestial.ghiblibrary.android.viewmodel.VehicleViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VehicleFragment : Fragment() {
    private val vehicleViewModel: VehicleViewModel by viewModels()

    private var _binding: FragmentVehicleBinding? = null
    private val binding get() = _binding!!

    private val args: VehicleFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVehicleBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewEvents()
        initObservers()

        vehicleViewModel.getVehicle(args.vehicleId)
    }

    private fun initViewEvents() {
        binding.swltVehicle.setOnRefreshListener {
            vehicleViewModel.getVehicle(args.vehicleId)
        }
    }

    private fun initObservers() {
        vehicleViewModel.vehicle.observe(this.viewLifecycleOwner, VehicleObserver())
    }

    private fun setVehicleContent(vehicle: Vehicle) {
        binding.lytVehicle.tvName.text = vehicle.name

        binding.lytVehicle.tvClass.text = vehicle.vehicleClass

        binding.lytVehicle.tvLength.text = vehicle.length ?: getString(R.string.n_a)

        binding.lytVehicle.ivThumbnail.load(vehicle.imageUrl) {
            crossfade(true)
        }

        binding.lytVehicle.tvDescription.text = vehicle.description
    }

    private inner class VehicleObserver : Observer<UIState<Vehicle>> {
        override fun onChanged(vehicle: UIState<Vehicle>) {
            when (vehicle) {
                is UIState.Loading -> {
                    if (!binding.swltVehicle.isRefreshing) {
                        binding.swltVehicle.isRefreshing = true
                    }
                }
                is UIState.Success -> {
                    binding.swltVehicle.isRefreshing = false

                    setVehicleContent(vehicle.value)
                }
                is UIState.Failure -> {
                    binding.swltVehicle.isRefreshing = false
                }
            }
        }
    }
}